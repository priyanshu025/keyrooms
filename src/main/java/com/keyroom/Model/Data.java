package com.keyroom.Model;

import com.google.gson.annotations.Expose;

public class Data {

    @Expose
    Integer id;
    @Expose
    String title;
    @Expose
    String flage;
    @Expose
    String slug;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getFlage() {
        return flage;
    }

    public void setFlage(String flage) {
        this.flage = flage;
    }

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }
}

