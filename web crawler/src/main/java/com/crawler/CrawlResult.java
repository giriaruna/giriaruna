package com.crawler;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

// encapsulates all discovered assets to prevent data leaks between requests
public class CrawlResult {
    // using concurrent hash maps for better performance O(1)
    public final Set<String> images = ConcurrentHashMap.newKeySet();
    public final Set<String> logos = ConcurrentHashMap.newKeySet();

    // accepts: nothing
    // returns: an empty result container
    public CrawlResult() {}
}
