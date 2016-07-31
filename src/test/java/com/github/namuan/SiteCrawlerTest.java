package com.github.namuan;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import java.util.Map;
import java.util.Set;

public class SiteCrawlerTest {

    @Test
    public void shouldCrawlSiteForTopLevelLinks() {
        // setup
        SiteCrawler siteCrawler = new SiteCrawler(1);

        String page = "http://www.w3schools.com";
        String pageFragment = "<!DOCTYPE html>\n" +
                "<html>\n" +
                "<body>\n" +
                "\n" +
                "<p>An absolute URL: <a href=\"http://www.w3schools.com/something.asp\">Something</a></p>\n" +
                "\n" +
                "</body>\n" +
                "</html>\n";
        MockHtmlPageReader mockHtmlPageReader = new MockHtmlPageReader();
        mockHtmlPageReader.pageFragment = pageFragment;
        siteCrawler.setHtmlPageReader(mockHtmlPageReader);

        // when
        final Map<String, Set<String>> siteMap = siteCrawler.buildSiteMapFor(page);

        // then
        assertTrue("Should contain one child link from page", siteMap.get(page).size() == 1);
    }

    @Test
    public void shouldFollowLinks() {
        // setup
        SiteCrawler siteCrawler = new SiteCrawler(3);

        String page = "http://www.w3schools.com";
        String pageFragmentForTopLevelPage = "<!DOCTYPE html>\n" +
                "<html>\n" +
                "<body>\n" +
                "\n" +
                "<p>An absolute URL: <a href=\"http://www.w3schools.com/html/default.asp\">Learn Html</a></p>\n" +
                "\n" +
                "</body>\n" +
                "</html>\n";

        String pageFragmentForChildPage = "<!DOCTYPE html>\n" +
                "<html>\n" +
                "<body>\n" +
                "\n" +
                "<p>An absolute URL: <a href=\"http://www.w3schools.com/aboutus.asp\">About Us</a></p>\n" +
                "\n" +
                "</body>\n" +
                "</html>\n";

        MockHtmlPageReader mockHtmlPageReader = new MockHtmlPageReader();
        siteCrawler.setHtmlPageReader(mockHtmlPageReader);


        mockHtmlPageReader.pageFragment = pageFragmentForTopLevelPage;
        mockHtmlPageReader.pageFragmentForChildPage = pageFragmentForChildPage;

        // when
        final Map<String, Set<String>> siteMap = siteCrawler.buildSiteMapFor(page);

        // then

        assertTrue("Should contain three entries in sitemap, found:" + siteMap.size(), siteMap.size() == 3);
        assertTrue("Should contain child page", siteMap.get(page).contains("http://www.w3schools.com/html/default.asp"));
        assertTrue("Should contain child page", siteMap.get("http://www.w3schools.com/html/default.asp").contains("http://www.w3schools.com/aboutus.asp"));
        assertTrue("Leaf page shouldn't contain any child pages", siteMap.get("http://www.w3schools.com/aboutus.asp").size() == 0);
    }

    @Test
    public void shouldNotFollowLinksForExternalPages() {
        // given
        SiteCrawler siteCrawler = new SiteCrawler(1);

        String page = "http://www.w3schools.com";
        String pageFragment = "<!DOCTYPE html>\n" +
                "<html>\n" +
                "<body>\n" +
                "\n" +
                "<p>An absolute URL: <a href=\"http://www.google.com/something.asp\">Google Something</a></p>\n" +
                "\n" +
                "</body>\n" +
                "</html>\n";
        MockHtmlPageReader mockHtmlPageReader = new MockHtmlPageReader();
        mockHtmlPageReader.pageFragment = pageFragment;
        siteCrawler.setHtmlPageReader(mockHtmlPageReader);

        // when
        final Map<String, Set<String>> siteMap = siteCrawler.buildSiteMapFor(page);

        // then
        assertFalse(mockHtmlPageReader.visitedPages.contains("http://www.google.com/something.asp"));
    }

    @Test
    public void shouldIncludeLinksForExternalPages() {
        // given
        SiteCrawler siteCrawler = new SiteCrawler(1);

        String page = "http://www.w3schools.com";
        String pageFragment = "<!DOCTYPE html>\n" +
                "<html>\n" +
                "<body>\n" +
                "\n" +
                "<p>An absolute URL: <a href=\"http://www.google.com/something.asp\">Google Something</a></p>\n" +
                "\n" +
                "</body>\n" +
                "</html>\n";
        MockHtmlPageReader mockHtmlPageReader = new MockHtmlPageReader();
        mockHtmlPageReader.pageFragment = pageFragment;
        siteCrawler.setHtmlPageReader(mockHtmlPageReader);

        // when
        final Map<String, Set<String>> siteMap = siteCrawler.buildSiteMapFor(page);

        // then
        assertTrue("Should contain external links from page", siteMap.get(page).size() == 1);
    }

}
