import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashSet;


public class UrlService {

    /**
     * Check URL and creates UrlLink object
     * @param url - url
     * @param foundOn - where url was found
     * @return UrlLink object
     * @throws IOException in case if Url is invalid and UrlLink object can't be created
     */
    public static UrlLink prepareLink(String url, UrlLink foundOn, UrlLink host) throws IOException {

        if (host == null) throw new IllegalArgumentException("Provided host is null");

        String link;

        if (UrlValidator.isLinkInternal(host.getUrl().toString(), new URL(url))) {
            link = UrlValidator.isAbsoluteUrl(url) ? url : host.getUrl().toString() + url;
        } else {
            link = UrlValidator.isAbsoluteUrl(url) ? url : "http://" + url;
        }

        return new UrlLink(link, foundOn);
    }

    /**
     * Opens a web page, gets all HTML elements with attribute [href], and fetches their values
     * @param urlLink - URL of a page to be analyzed
     * @return HashSet of links found on provided page
     * @throws IOException if connection to provided page failed
     */
    public static HashSet<String> getPageLinks(UrlLink urlLink) throws IOException {

        HashSet<String> links = new HashSet<>();

        // Fetch the HTML code
        Document document = Jsoup.connect(urlLink.getUrl().toString()).get();

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

        return links;
    }

    /**
     * Performs SEO analyze of provided web page
     * @param urlLink - UrlLink object related to the page to be analyzed
     * @return information about particular web page
     * @throws IOException - if connection to provided URL can't be established
     */
    public static String analyzePage(UrlLink urlLink) throws IOException {

        // Fetch the HTML code
        Document document = Jsoup.connect(urlLink.getUrl().toString()).get();

        // Analyze code
        return String.format("Link: %s, Title: %s, Code: %s, h1 tags: %s",
                urlLink.getUrl().toString(), document.title(), urlLink.getHttpStatusCode(), document.select("h1").size());
    }

    /**
     * Makes Get request to provided link and returns HTTP status code
     * @param url - link
     * @return - HTTP status code
     * @throws IOException if connection to provided link failed
     */
    public static int getUrlStatusCode(URL url) throws IOException {
        if (url == null) throw new IllegalArgumentException("You are trying to get status code of empty link");

        URLConnection connection = url.openConnection();

        HttpURLConnection c = (HttpURLConnection) connection;
        c.setRequestMethod("GET");
        c.connect();
        return c.getResponseCode();
    }
}
