package com.github.namuan;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class SiteCrawler {

    // Controls how deep that we would like the crawler to navigate the tree
    private int depthToCrawlPages = 1;

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

    Set<String> linksAlreadyTraversed = new HashSet<>();

    /**
     * Returns the sitemap
     * @param siteToCrawl
     * @return
     */
    public SiteMap buildSiteMapUsingTreeFor(String siteToCrawl) {
        SitePage page = new SitePage(siteToCrawl);
        findLinksForPage(page, 1);
        return new SiteMap(siteToCrawl, page);
    }

    /**
     * Recursively crawls page and stops when it hits the depth set in this class
     * @param page
     * @param level
     */
    public void findLinksForPage(SitePage page, int level) {
        if (level > depthToCrawlPages) {
            System.out.println("Hit crawl limit:" + level);
            return;
        }

        if (linksAlreadyTraversed.contains(page.getPage())) {
            System.out.println("Page already traversed:" + page.getPage());
            return;
        }

        System.out.println("Finding links for " + page);
        final Set<String> linksForChildPage = pageCrawler.buildLinkForPage(page.getPage());
        linksAlreadyTraversed.add(page.getPage());

        linksForChildPage.stream()
                .map(childPageLink -> {
                    SitePage childSitePage = new SitePage(childPageLink);
                    page.add(childSitePage);
                    return childSitePage;
                })
                .filter(childSitePage -> childPageBelongsToSameDomain(page.getPage(), childSitePage.getPage()))
                .forEach(childSitePage -> findLinksForPage(childSitePage, level + 1));
    }
}
