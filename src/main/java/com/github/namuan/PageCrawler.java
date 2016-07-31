package com.github.namuan;

import org.jsoup.select.Elements;

import java.util.Set;
import java.util.stream.Collectors;

public class PageCrawler {

    private HtmlPageReader htmlPageReader;

    public void setHtmlPageReader(HtmlPageReader htmlPageReader) {
        this.htmlPageReader = htmlPageReader;
    }

    public Set<String> buildLinkForPage(String pageUrl) {
        final Elements links = this.htmlPageReader.findLinks(pageUrl);

        return links
                .stream()
                .map(link -> link.attr("abs:href"))
                .filter(link -> !link.isEmpty())
                .collect(Collectors.toSet());
    }
}
