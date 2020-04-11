package io.robort.crawler.helpers;

import io.robort.crawler.Crawler;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Set;

public class HttpHelper {

    public static String makeUrlAbsolute(String url, String host) {
        if (url == null || host == null || host.trim().length() == 0) throw new IllegalArgumentException("URL of host were not provided");

        url = url.trim();
        String absoluteUrl;
        if (url.startsWith("//")) {
            absoluteUrl = "http:" + url;
        } else if (url.startsWith("/")) {
            absoluteUrl = host + url;
        } else {
            absoluteUrl = host + "/" + url;
        }
        return absoluteUrl;
    }


    /**
     * Checks if current URL is a web page
     * @return boolean
     */
    public static boolean isWebPage(HttpURLConnection httpURLConnection) {
        if (httpURLConnection == null) return false;

        String contentType = httpURLConnection.getContentType();
        if (contentType == null) return false;

        return contentType.contains("text/html") || contentType.contains("application/xml") || contentType.contains("application/xml+xhtml");
    }

    /**
     *
     * @param urlString
     * @return
     */
    public static String getHostFromUrl(String urlString) {
        if (urlString != null && urlString.trim().length() > 0 && isUrl(urlString)) {

            String host;

            int startingPositionOfUrlAuthority;
            if (urlString.startsWith("https://")) {
                startingPositionOfUrlAuthority = 8;
            } else if (urlString.startsWith("http://")) {
                startingPositionOfUrlAuthority = 7;
            } else {
                startingPositionOfUrlAuthority = 0;
            }

            int positionOfClosingSlash = urlString.indexOf("/", startingPositionOfUrlAuthority);

            if (positionOfClosingSlash == -1) host = urlString;
            else host = urlString.substring(0, positionOfClosingSlash);

            return host;

        } else {
            return null;
        }
    }

    /**
     * Opens a web page, gets all HTML elements with attribute [href], and fetches their values
     * @return HashSet of links found on provided page
     */
    public static ArrayList<String> getPageLinks(Document htmlDocument) {

        ArrayList<String> links = new ArrayList<>();

        if (htmlDocument != null) {
            // Parse the HTML and get all links to other URLs
            Elements elements = htmlDocument.select("[src]");
            Elements hrefs = htmlDocument.select("[href]");
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

    public static HttpURLConnection connectToUrl(String uriString) {
        HttpURLConnection connection;

        try {
            System.out.println("Trying to connect to " + uriString);
            URL url = new URL(uriString);
            HttpURLConnection c = (HttpURLConnection) url.openConnection();
            c.setRequestMethod("GET");
            c.connect();
            connection = c;

        } catch (IOException ex) {
            System.out.println(ex.getMessage());
            connection = null;
        }

        return connection;
    }

    public static Document parseHtmlDocument(HttpURLConnection connection) throws IOException {
        BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));

        String line;
        StringBuilder sb = new StringBuilder();

        while ((line = in.readLine()) != null) {
            sb.append(line);
            sb.append(System.lineSeparator());
        };
        in.close();

        return Jsoup.parse(sb.toString());
    }

    public static boolean isUrl(String uriString) {
        if (uriString == null) throw new IllegalArgumentException("Couldn't verify empty URI");
        else {
            if (uriString.trim().length() != 0) {
                int firstOccurrenceOfDot = uriString.indexOf("."); // Finds position of first occurrence of colon in uriString
                if (firstOccurrenceOfDot != -1) {
                    String partOfUriBeforeDot = uriString.substring(0, firstOccurrenceOfDot); // Gets part of URI before first dot
                    if (!partOfUriBeforeDot.contains(":")) return true;
                    else {
                        return partOfUriBeforeDot.startsWith("http://") || partOfUriBeforeDot.startsWith("https://");
                    }
                }
            }
            return false;
        }
    }

    /**
     * Checks if URL belongs to host
     * @return boolean
     */
    public static boolean isInternal(Crawler crawler, String uriString) {

        if (crawler == null || uriString == null) throw new IllegalArgumentException("Provided Crawler object or URI string is null");
        Set<String> hosts = crawler.getHosts();

        for (String host : hosts) {
            if (uriString.startsWith(host)) return true;
        }

        return false;
    }

    /**
     * @return true if URL is absolute, false if relative
     */
    public static boolean isUrlAbsolute(String uriString) {
        return uriString.startsWith("http://") || uriString.startsWith("https://");
    }
}
