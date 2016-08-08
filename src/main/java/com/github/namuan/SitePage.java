package com.github.namuan;

import java.util.ArrayList;
import java.util.List;

public class SitePage {
    private final String page;
    private List<SitePage> childPages = new ArrayList<>();

    public SitePage(String page) {

        this.page = page;
    }

    public void add(SitePage sitePage) {
        childPages.add(sitePage);
    }

    public List<SitePage> getChildPages() {
        return childPages;
    }

    public String getPage() {
        return page;
    }

    @Override
    public String toString() {
        return "SitePage{" +
                "page='" + page + '\'' +
                '}';
    }
}
