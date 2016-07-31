package com.github.namuan;

import org.junit.Test;
import static org.junit.Assert.*;

import java.io.IOException;
import java.util.Map;
import java.util.Set;

public class SiteCrawlerFunctionalTest {
    @Test public void shouldCrawlSite() throws IOException {
        // given
        String siteToCrawl = "http://wiprodigital.com";
        int numberOfLevelsToCrawl = 1;

        SiteCrawler crawler = new SiteCrawler(numberOfLevelsToCrawl);

        // when
        final Map<String, Set<String>> siteMap = crawler.buildSiteMapFor(siteToCrawl);

        // then
        assertTrue("Should crawl the given site", siteMap.get(siteToCrawl) != null);
    }
}
