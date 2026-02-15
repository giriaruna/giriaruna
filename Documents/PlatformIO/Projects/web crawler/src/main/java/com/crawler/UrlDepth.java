package com.crawler;

// acts as a data container to keep a url and its depth level together
public class UrlDepth {
    public final String url;
    public final int depth;

    // accepts: string url, int depth
    // returns: a new urldepth object
    public UrlDepth(String url, int depth) {
        this.url = url;
        this.depth = depth;
    }
}