package com.carlgira.soda.model;

public class Page {

    private Integer pages;

    private Boolean isCover;

    public Page() {
    }

    public Page(Integer content, Boolean isCover) {
        this.pages = content;
        this.isCover = isCover;
    }

    public Integer getPages() {
        return pages;
    }

    public void setPages(Integer pages) {
        this.pages = pages;
    }

    public Boolean getCover() {
        return isCover;
    }

    public void setCover(Boolean cover) {
        isCover = cover;
    }
}
