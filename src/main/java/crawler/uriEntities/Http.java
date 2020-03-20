package crawler.uriEntities;

import crawler.Crawler;
import crawler.interfaces.Buffer;
import crawler.interfaces.URI;
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
import java.util.HashSet;
import java.util.Set;

/**
 * This class describes a link to web page
 */
public class Http implements URI {

    private String              uri;
    private Set<URI>            parents;
    private HttpURLConnection   connection;
    private int                 httpStatusCode;
    private Document            document; // Parsed HTML of web page

    // Analysis of web page
    private String              analysis;

    public Http(String uriString) {
        this.parents = new HashSet<>();
        this.setUri(uriString);

        try {
            this.setConnection();
            this.setHttpStatusCode();

            if (this.isSuccessfulConnection()) {

                if (this.isWebPage() && this.isInternal()) {
                    this.setDocument();

                    // Adds child URIs to buffer
                    Buffer buffer = Crawler.getBuffer();

                    for (String uri : this.getPageLinks()) {
                        buffer.addUri(uri);
                    }
                }
                this.setAnalysis();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String getUri() {
        return this.uri;
    }

    @Override
    public Set<URI> getParents() {
        return this.parents;
    }

    @Override
    public void setUri(String uriString) {

        if (uriString != null && uriString.trim().length() > 0) {
            this.uri = uriString.trim();
        } else {
            throw new IllegalArgumentException("Attempt to set empty URI");
        }
    }

    @Override
    public void updateUri(URI uriObject) {
        if (uriObject.getUri().equals(this.getUri())) {
            this.getParents().addAll(uriObject.getParents());
        }
    }

    /**
     * @return connection to current URL
     */
    public HttpURLConnection getConnection() {
        return this.connection;
    }

    /**
     * @return HTTP status code
     */
    public int getHttpStatusCode() {
        return this.httpStatusCode;
    }

    /**
     * @return analysis of web page
     */
    public String getAnalysis() {
        return this.analysis;
    }

    /**
     * @return parsed HTML of web page
     */
    public Document getDocument() {
        return this.document;
    }

    /**
     * Sets connection to URL
     */
    private void setConnection() {
        if (this.getConnection() == null) {
            HttpURLConnection connection;

            try {
                URL url = new URL(this.getUri());
                HttpURLConnection c = (HttpURLConnection) url.openConnection();
                c.setRequestMethod("GET");
                c.connect();
                connection = c;

            } catch (IOException ex) {
                System.out.println(ex.getMessage());
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
     * Parses HTML of web page
     */
    private void setDocument() throws IOException {
        if (this.getConnection() != null) {

            BufferedReader in = new BufferedReader(new InputStreamReader(this.getConnection().getInputStream()));

            String line;
            StringBuilder sb = new StringBuilder();

            while ((line = in.readLine()) != null) {
                sb.append(line);
                sb.append(System.lineSeparator());
            };
            in.close();

            this.document = Jsoup.parse(sb.toString());
        }
    }

    /**
     * Performs SEO analyze of provided web page and records it to analyze instance variable
     */
    private void setAnalysis() {

        StringBuilder sb = new StringBuilder();
        sb.append(this.getUri());

        // Parents
        if (this.getParents() != null && this.getParents().size() > 0) {
            sb.append(" found on: ");
            for (URI parent : this.getParents()) {
                sb.append(parent.getUri());
            }
        }

        if (this.getConnection() != null && this.isInternal() && this.isWebPage()) {
            String title = this.getDocument().title();
            int numberOfH1 = this.getDocument().select("h1").size();

            sb.append(String.format("\tTitle: %s, \tCode: %s, \th1 tags: %s",
                    title, this.getHttpStatusCode(), numberOfH1));
        } else {
            sb.append(String.format("\tCode: %s", this.getHttpStatusCode()));
        }

        this.analysis = sb.toString();
    }

    /**
     * Checks if provided link is not empty, is absolute, and returns status code < 400
     * @return boolean
     */
    public boolean isSuccessfulConnection() {
        if (this.getConnection() == null) return false;

        boolean isSuccess;
        try {
            isSuccess = this.getConnection().getResponseCode() < 300;
        } catch (IOException e) {
            isSuccess = false;
        }
        return isSuccess;
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
     * Checks if URL belongs to host
     * @return boolean
     */
    public boolean isInternal() {

        String cleanedHost = Crawler.getHost();
        String cleanedLink = this.getUri();

        // Trims host protocol
        if (cleanedHost.startsWith("//")) {
            cleanedHost = cleanedHost.trim().substring(2);
        } else if (cleanedHost.startsWith("http://")) {
            cleanedHost = cleanedHost.substring(7);
        } else if (cleanedHost.startsWith("https://")) {
            cleanedHost = cleanedHost.substring(8);
        }
        // Trims URL protocol
        if (cleanedLink.startsWith("//")) {
            cleanedLink = cleanedLink.trim().substring(2);
        } else if (cleanedLink.startsWith("http://")) {
            cleanedLink = cleanedLink.substring(7);
        } else if (cleanedLink.startsWith("https://")) {
            cleanedLink = cleanedLink.substring(8);
        }

        return cleanedLink.startsWith(cleanedHost);
    }

    /**
     * Opens a web page, gets all HTML elements with attribute [href], and fetches their values
     * @return HashSet of links found on provided page
     */
    public ArrayList<String> getPageLinks() {

        ArrayList<String> links = new ArrayList<>();

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
                    System.out.println(href);

                }

            }
        }

        return links;
    }

    @Override
    public String toString() {
        return this.getUri();
    }

    @Override
    public int compareTo(URI urlLink) {
        return this.getUri().compareTo(urlLink.getUri());
    }
}
