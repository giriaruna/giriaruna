package com.crawler;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import com.google.gson.Gson;
import java.io.IOException;

// controller acts as the bridge between the website and the crawl engine
@WebServlet("/api/crawl")
public class CrawlController extends HttpServlet {
    private final Gson gson = new Gson();

    // accepts: http 
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        // get the url parameter from the frontend
        String url = req.getParameter("url");
        if (url == null || url.isEmpty()) return;

        // add https protocol if the user forgot to type it
        if (!url.startsWith("http")) url = "https://" + url;

        String domain = url.replaceAll("https?://(www\\.)?", "").split("/")[0];

        // initialize results 
        CrawlResult results = new CrawlResult();
        new CrawlerEngine(domain, results).run(url);

        // return the results
        resp.setContentType("application/json");
        resp.getWriter().print(gson.toJson(results));
    }
}
