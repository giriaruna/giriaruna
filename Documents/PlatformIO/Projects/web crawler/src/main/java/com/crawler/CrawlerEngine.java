package com.crawler;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import java.util.Set;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

// handles the multi-threaded parallel crawl logic
public class CrawlerEngine {
    private final String domain;
    private final Set<String> visited = ConcurrentHashMap.newKeySet();
    private final CrawlResult results;
    private final ExecutorService pool;
    private final AtomicInteger activeTasks = new AtomicInteger(0);
    private final CountDownLatch latch = new CountDownLatch(1);

    // accepts: string domain and crawlresult results
    // returns: an initialized engine with 10 worker threads
    public CrawlerEngine(String domain, CrawlResult results) {
        this.domain = domain;
        this.results = results;
        this.pool = Executors.newFixedThreadPool(10);
    }

    // accepts: starting url string
    public void run(String startUrl) {
        submitTask(new UrlDepth(startUrl, 0));
        try {
            // wait for active tasks to hit zero or timeout after 10s
            latch.await(10, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } finally {
            pool.shutdownNow();
        }
    }

    // accepts: urldepth task object
    private void submitTask(UrlDepth task) {
        if (pool.isShutdown() || task.depth > 1 || visited.contains(task.url) || !task.url.contains(domain)) {
            checkDone();
            return;
        }
        visited.add(task.url);
        activeTasks.incrementAndGet();

        pool.execute(() -> {
            try {
                // we use a chrome user-agent so websites don't block 
                Document doc = Jsoup.connect(task.url)
                    .timeout(5000)
                    .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/91.0.4472.124 Safari/537.36")
                    .get();

                // extract images
                for (Element img : doc.select("img[src]")) {
                    String src = img.absUrl("src").split("\\?")[0];
                    if (src.toLowerCase().contains("logo")) results.logos.add(src);
                    else results.images.add(src);
                }

                // recursive links
                if (task.depth < 1) {
                    for (Element link : doc.select("a[href]")) {
                        String nextUrl = link.absUrl("href");
                        if (nextUrl.startsWith("http")) {
                            submitTask(new UrlDepth(nextUrl, task.depth + 1));
                        }
                    }
                }
            } catch (Exception ignored) {
                // skip failed page loads
            } finally {
                activeTasks.decrementAndGet();
                checkDone();
            }
        });
    }

    // checks if all active are done to trigger shutdown
    private void checkDone() {
        if (activeTasks.get() <= 0) latch.countDown();
    }
}
