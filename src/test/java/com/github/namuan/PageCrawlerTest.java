package com.github.namuan;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

import java.util.Set;

public class PageCrawlerTest {

    PageCrawler pageCrawler = new PageCrawler();

    @Test
    public void shouldBuildLinksForPage() {
        // given
        String page = "http://www.w3schools.com";
        String pageFragment = "<!DOCTYPE html>\n" +
                "<html>\n" +
                "<body>\n" +
                "\n" +
                "<p>An absolute URL: <a href=\"http://www.w3schools.com\">W3Schools</a></p>\n" +
                "\n" +
                "</body>\n" +
                "</html>\n";
        MockHtmlPageReader mockHtmlPageReader = new MockHtmlPageReader();
        mockHtmlPageReader.pageFragment = pageFragment;

        pageCrawler.setHtmlPageReader(mockHtmlPageReader);

        // when
        final Set<String> pages = pageCrawler.buildLinkForPage(page);

        // then
        assertTrue("Should contain one child link from page", pages.size() == 1);
    }
}