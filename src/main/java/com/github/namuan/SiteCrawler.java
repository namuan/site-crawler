package com.github.namuan;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

public class SiteCrawler {

    // Controls how deep that we would like the crawler to navigate the tree
    private int depthToCrawlPages = 1;

    Map<String, Set<String>> siteMap = new HashMap<>();
    PageCrawler pageCrawler = new PageCrawler();

    public SiteCrawler() {
        setHtmlPageReader(new JSoupHtmlPageReader());
    }

    public SiteCrawler(int depthToCrawlPages) {
        this();
        this.depthToCrawlPages = depthToCrawlPages;
    }

    /**
     * Overrides the page reader
     * @param htmlPageReader
     */
    public void setHtmlPageReader(HtmlPageReader htmlPageReader) {
        pageCrawler.setHtmlPageReader(htmlPageReader);
    }

    /**
     * Returns the sitemap
     * @param siteToCrawl
     * @return
     */
    public Map<String, Set<String>> buildSiteMapFor(String siteToCrawl) {
        crawlPage(siteToCrawl, 1);
        return siteMap;
    }

    /**
     * Recursively crawls page and stops when it hits the depth set in this class
     * @param pageUrl
     * @param level
     */
    public void crawlPage(String pageUrl, int level) {
        if (siteMap.get(pageUrl) != null) {
            return;
        }

        System.out.println("<- " + pageUrl);

        Set<String> pageChildrens = pageCrawler.buildLinkForPage(pageUrl);
        siteMap.put(pageUrl, pageChildrens);

        for (String childPageUrl : pageChildrens) {
            System.out.println("==> Level: " + level + " -> " + childPageUrl);
            if (level <= depthToCrawlPages && childPageBelongsToSameDomain(pageUrl, childPageUrl)) {
                crawlPage(childPageUrl, level + 1);
            }
        }
    }

    private boolean childPageBelongsToSameDomain(String pageUrl, String childPageUrl) {
        URL children = null;
        URL page = null;
        try {
            page = new URL(pageUrl);
            children = new URL(childPageUrl);
        } catch (MalformedURLException e) {
            // cant do much
        }

        return children != null && Objects.equals(page.getHost(), children.getHost());

    }
}
