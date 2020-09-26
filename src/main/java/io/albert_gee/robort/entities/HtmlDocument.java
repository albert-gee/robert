package io.albert_gee.robort.entities;

import lombok.Getter;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;

public class HtmlDocument implements io.albert_gee.robort.interfaces.Document {

    private final String html;
    private Document document;

    @Getter
    private String uri;

    @Getter
    private int statusCode;

    public HtmlDocument(String html, String uri, int statusCode) {
        this.html = html;
        this.uri = uri;
        this.statusCode = statusCode;
        document = Jsoup.parse(html);

    }

    public ArrayList<String> getUrls() {

        ArrayList<String> links = new ArrayList<>();

        if (document != null) {
            // Parse the HTML and get all links to other URLs
            Elements elements = document.select("[src]");
            Elements hrefs = document.select("[href]");
            elements.addAll(hrefs);

            for (Element element : elements) {

                if (element.hasAttr("src")) {
                    String src = element.attr("src");
                    if (src != null && src.trim().length() > 0) links.add(src);
                }
                if (element.hasAttr("href")) {
                    String href = element.attr("href");
                    if (href != null && href.trim().length() > 0) links.add(href);
                }
            }
        }

        return links;
    }

    public ArrayList<String> getLinks() {
        ArrayList<String> urls = new ArrayList<>();

        if (document != null) {
            // Parse the HTML and get all links to other URLs
            Elements hrefs = document.select("a[href]");

            for (Element href : hrefs) {

                String hrefString = href.attr("href");
                urls.add(hrefString);
            }
        }

        return urls;
    }
}
