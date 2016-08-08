package com.github.namuan;

public class SiteMap {
    private final String site;
    private SitePage topLevelSitePage;

    public SiteMap(String site, SitePage topLevelSitePage) {
        this.site = site;
        this.topLevelSitePage = topLevelSitePage;
    }

    public SitePage getTopLevelSitePage() {
        return topLevelSitePage;
    }

    @Override
    public String toString() {
        return "SiteMap{" +
                "site='" + site + '\'' +
                '}';
    }
}
