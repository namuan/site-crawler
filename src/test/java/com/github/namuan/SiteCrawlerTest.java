package com.github.namuan;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import java.util.Optional;

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
        final SiteMap siteMap = siteCrawler.buildSiteMapUsingTreeFor(page);

        // then
        assertEquals("Should contain one child link from page", siteMap.getTopLevelSitePage().getPage(), "http://www.w3schools.com");
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
        final SiteMap siteMap = siteCrawler.buildSiteMapUsingTreeFor(page);

        // then
        assertTrue("Should contain child page", siteMap.getTopLevelSitePage().getChildPages().size() == 1);
        Optional<SitePage> maybeFirstChildPage = siteMap.getTopLevelSitePage().getChildPages()
                .stream()
                .filter(sitePage -> sitePage.getPage().equals("http://www.w3schools.com/html/default.asp"))
                .findFirst();

        assertTrue("Should contain child page", maybeFirstChildPage.isPresent());
        final Optional<SitePage> maybeChildNode = maybeFirstChildPage.get().getChildPages()
                .stream()
                .filter(sitePage -> sitePage.getPage().equals("http://www.w3schools.com/aboutus.asp"))
                .findFirst();
        assertTrue("Should contain child page", maybeChildNode.isPresent());
        assertTrue("Leaf page shouldn't contain any child pages", maybeChildNode.get().getChildPages().size() == 0);
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
        siteCrawler.buildSiteMapUsingTreeFor(page);

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
        final SiteMap siteMap = siteCrawler.buildSiteMapUsingTreeFor(page);

        // then
        assertTrue("Should contain external links from page", siteMap.getTopLevelSitePage().getChildPages().size() == 1);
    }

}
