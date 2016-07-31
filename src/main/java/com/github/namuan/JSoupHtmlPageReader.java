package com.github.namuan;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;

public class JSoupHtmlPageReader implements HtmlPageReader {
    @Override
    public Elements findLinks(String pageUrl) {

        Document document = null;

        try {
            document = Jsoup.connect(pageUrl).get();
        } catch (IOException e) {
            System.out.println("Unable to read url:" + pageUrl + " - " + e.getMessage());
        }

        if (null == document) {
            return new Elements();
        }

        return document.select("a[href]");

    }
}
