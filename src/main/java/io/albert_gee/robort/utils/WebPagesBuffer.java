package io.albert_gee.robort.utils;

import io.albert_gee.robort.entities.HtmlDocument;

import java.util.ArrayList;

public class WebPagesBuffer {

    private static WebPagesBuffer instance;

    private ArrayList<HtmlDocument> pages;

    private WebPagesBuffer() {
        pages = new ArrayList<>();
    }

    public static WebPagesBuffer getInstance() {
        if (instance == null) {
            instance = new WebPagesBuffer();
        }

        return instance;
    }

    public void add(HtmlDocument document) {
        if (document == null) {
            throw new IllegalArgumentException("Added HTML document is null");
        }

        pages.add(document);
    }

    public ArrayList<HtmlDocument> getPages() {
        return pages;
    }
}
