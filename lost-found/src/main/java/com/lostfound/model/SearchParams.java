package com.lostfound.model;

import lombok.Data;

@Data
public class SearchParams {
    private String category;
    private String location;
    private String name;

    public SearchParams() {
    }

    public SearchParams(String category, String location, String name) {
        this.category = category;
        this.location = location;
        this.name = name;
    }
}