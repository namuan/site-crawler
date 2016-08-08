package com.github.namuan;

import static org.junit.Assert.assertNotNull;

import org.junit.Test;

import java.io.IOException;
import java.util.List;

public class SiteCrawlerFunctionalTest {

    @Test public void shouldCrawlSiteUsingTree() throws IOException {
        // given
        String siteToCrawl = "http://example.com";
        int numberOfLevelsToCrawl = 1;

        SiteCrawler crawler = new SiteCrawler(numberOfLevelsToCrawl);

        // when
        final SiteMap siteMap = crawler.buildSiteMapUsingTreeFor(siteToCrawl);

        // then
        traverseSite(siteMap);

        // and
        assertNotNull(siteMap.getTopLevelSitePage());
    }

    private void traverseSite(SiteMap siteMap) {
        System.out.println("Crawling site:" + siteMap.toString());
        SitePage topLevel = siteMap.getTopLevelSitePage();
        System.out.println("Page:" + topLevel);
        traverseSitePage(topLevel);
    }

    private void traverseSitePage(SitePage sitePage) {
        List<SitePage> childPages = sitePage.getChildPages();
        for (SitePage page : childPages) {
            System.out.println("Page:" + page.toString());
            traverseSitePage(page);
        }
    }
}
