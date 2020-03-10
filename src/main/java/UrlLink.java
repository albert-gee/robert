import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.util.HashSet;

/**
 * This class represents a structured data of a URL string such as response HTTP code and where particular URL was found
 *
 * @author Albert Gainutdinov
 * @version 1.0
 */
public class UrlLink implements Comparable<UrlLink> {

    // Found url address
    private String url;

    // Where URL was found
    private UrlLink parent;

    // URL connection
    private HttpURLConnection connection;

    // Connection to web page
    private Connection.Response connectionResponse;

    // HTTP response code
    private int httpStatusCode;

    // Analyze of web page
    private String analyze;

    // Parsed HTML of web page
    private Document document;

    public UrlLink(String url, UrlLink foundOn, String host) throws IOException {
        setUrl(url, host);
        setParent(foundOn);

        if (!this.isMailOrTel()) {
            setConnection();
            setHttpStatusCode();
            setConnectionResponse();
            setDocument();
        }

        setAnalyze();
    }

    /**
     * @return current URL string
     */
    public String getUrl() {
        return this.url;
    }

    /**
     * @return parent URL string where current URL was found
     */
    public UrlLink getParent() {
        return this.parent;
    }

    /**
     * @return connection to current URL
     */
    public HttpURLConnection getConnection() {
        return this.connection;
    }

    /**
     * @return connection response to current URL
     */
    public Connection.Response getConnectionResponse() {
        return this.connectionResponse;
    }

    /**
     * @return HTTP status code
     */
    public int getHttpStatusCode() {
        return this.httpStatusCode;
    }

    /**
     * @return
     */
    public String getAnalyze() {
        return this.analyze;
    }

    /**
     * @return parsed HTML of web page
     */
    public Document getDocument() {
        return this.document;
    }

    /**
     * Sets current URL string
     * @param url - URL string
     */
    private void setUrl(String url, String host) {

        if (url != null && url.trim().length()> 0 && host != null && host.trim().length() > 0) {

            String trimmedUrl = url.trim();
            String trimmedHost = host.trim().toLowerCase();
            String preparedUrl;

            // If URL is "tel:" or "mailto:"
            if (this.isMailOrTel(url)) {
                preparedUrl = trimmedUrl;

            } else {
                // Trim slashes at the end and make url absolute
                String urlWithoutLastSlash = trimmedUrl.substring(url.length() - 1).equals("/") ? trimmedUrl.substring(0, url.length() - 1) : trimmedUrl;
                String hostWithoutLastSlash = trimmedHost.substring(host.length() - 1).equals("/") ? trimmedHost.substring(0, host.length() - 1) : trimmedHost;

                // Make URL absolute if it's not
                if (isMailOrTel(urlWithoutLastSlash) || urlWithoutLastSlash.startsWith("http://") || urlWithoutLastSlash.startsWith("https://")) {
                    preparedUrl = urlWithoutLastSlash;
                } else {
                    preparedUrl = hostWithoutLastSlash + "/" + urlWithoutLastSlash;
                }
            }

            this.url = preparedUrl;

        } else {
            throw new IllegalArgumentException("URL can not be set because provided link or host is empty");
        }
    }

    /**
     * Sets parent URL string where current URL was found
     * @param parent - parent URL string
     */
    private void setParent(UrlLink parent) {
        this.parent = parent;
    }

    /**
     * Sets connection to URL
     */
    private void setConnection() {
        if (this.getConnection() == null) {
            HttpURLConnection connection;

            try {
                URL url = new URL(this.getUrl());
                HttpURLConnection c = (HttpURLConnection) url.openConnection();
                c.setRequestMethod("GET");
                c.connect();
                connection = c;

            } catch (IOException ex) {
                connection = null;
            }

            this.connection = connection;
        }
    }

    /**
     * Sets HTTP status code
     */
    private void setHttpStatusCode() throws IOException {
        if (this.getConnection() != null) {
            this.httpStatusCode = this.getConnection().getResponseCode();
        }
    }

    /**
     * Connects to URL
     * @throws IOException if connection failed
     */
    private void setConnectionResponse() throws IOException {

        try {
            if (this.connectionResponse == null && this.isValid() && this.isWebPage()) {
                this.connectionResponse = Jsoup.connect(this.getUrl())
                        .userAgent("Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/535.21 (KHTML, like Gecko) Chrome/19.0.1042.0 Safari/535.21")
                        .timeout(10 * 1000)
                        .execute();
            }
        } catch (SocketTimeoutException ex) {
            this.connectionResponse = null;
        }
    }

    /**
     * Parses HTML of web page
     */
    private void setDocument() throws IOException {
        if (this.connectionResponse != null) {
            this.document = this.connectionResponse.parse();
        }
    }

    /**
     * Performs SEO analyze of provided web page and records it to analyze instance variable
     */
    private void setAnalyze() {

        String parent = (this.getParent() == null) ? "" : " found on " + this.getParent().toString();
        String result;

        if (this.getConnectionResponse() != null && this.getConnectionResponse().contentType().contains("text/html")) {

            String title = this.getDocument().title();
            int numberOfH1 = this.getDocument().select("h1").size();

            result = String.format("Link: %s \t%s \tTitle: %s, \tCode: %s, \th1 tags: %s",
                    this, parent, title, this.getHttpStatusCode(), numberOfH1);
        } else {
            result = String.format("Link: %s \t%s, \tCode: %s",
                    this, parent, this.getHttpStatusCode());
        }

        this.analyze = result;
    }

    /**
     * Checks is link is "tel:" or "mailto:"
     * @return boolean
     */
    public boolean isMailOrTel() {
        return isMailOrTel(this.getUrl());
    }

    /**
     * Checks is link is "tel:" or "mailto:"
     * @return boolean
     */
    private boolean isMailOrTel(String url) {
        return url.contains("tel:") ||
                url.contains("mailto:");
    }

    /**
     * Checks if provided link is not empty, is absolute, and returns status code < 400
     * @return boolean
     */
    public boolean isValid() {
        if (this.getConnection() == null) return false;

        boolean valid;
        try {
            valid = this.getConnection().getResponseCode() < 400;
        } catch (IOException e) {
            valid = false;
        }

        return valid;
    }

    /**
     * Checks if URL belongs to host
     * @param host - current host
     * @return boolean
     */
    public boolean isInternal(String host) {
        String hostWithoutProtocol = host.replace("https://", "").replace("http://", "").replace("//", "");
        String linkWithoutProtocol = this.getUrl().replace("https://", "").replace("http://", "");

        return linkWithoutProtocol.startsWith(hostWithoutProtocol);
    }

    /**
     * Checks if current URL is a web page
     * @return boolean
     */
    public boolean isWebPage() {
        if (this.getConnection() == null) return false;

        String contentType = this.getConnection().getContentType();
        if (contentType == null) return false;

        return contentType.contains("text/html") || contentType.contains("application/xml") || contentType.contains("application/xml+xhtml");
    }

    /**
     * Opens a web page, gets all HTML elements with attribute [href], and fetches their values
     * @return HashSet of links found on provided page
     */
    public HashSet<String> getPageLinks() {

        HashSet<String> links = new HashSet<>();

        if (this.getDocument() != null) {
            // Parse the HTML and get all links to other URLs
            Elements elements = this.getDocument().select("[src]");
            Elements hrefs = this.getDocument().select("[href]");
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

    @Override
    public String toString() {
        return this.getUrl();
    }

    @Override
    public int compareTo(UrlLink urlLink) {
        return this.getUrl().compareTo(urlLink.getUrl());
    }
}
