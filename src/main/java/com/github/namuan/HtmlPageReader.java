package com.github.namuan;

import org.jsoup.select.Elements;

/**
 * Abstracting for reading html pages
 */
public interface HtmlPageReader {
    Elements findLinks(String pageUrl);
}
