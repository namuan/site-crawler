package com.github.namuan;

import org.jsoup.Jsoup;
import org.jsoup.select.Elements;

import java.util.HashSet;
import java.util.Set;

public class MockHtmlPageReader implements HtmlPageReader {

    public String pageFragment = "";
    public String pageFragmentForChildPage = "";

    public Set<String> visitedPages = new HashSet<>();

    @Override
    public Elements findLinks(String pageUrl) {

        visitedPages.add(pageUrl);

        if (pageUrl.equals("http://www.w3schools.com/html/default.asp")) {
            return Jsoup.parseBodyFragment(pageFragmentForChildPage).getAllElements();
        }

        if (pageUrl.equals("http://www.w3schools.com/aboutus.asp")) {
            return new Elements();
        }

        return Jsoup.parseBodyFragment(pageFragment).getAllElements();
    }
}
