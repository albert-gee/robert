package io.robort.crawler.uriRuleEntities;

import io.robort.crawler.Crawler;
import io.robort.crawler.helpers.HttpHelper;
import io.robort.crawler.interfaces.UriInterface;

import org.jsoup.nodes.Document;

import java.io.IOException;

import java.net.HttpURLConnection;

import java.util.HashSet;
import java.util.Set;

/**
 * This class describes a link to web page
 */
public class Http implements UriInterface {

    private String              scheme;
    private String              uri;
    private Set<UriInterface>   parents;

    private HttpURLConnection   connection;
    private int                 httpStatusCode;
    private Document            document; // Parsed HTML of web page
    private String              analysis; // Analysis of web page

    public Http(String scheme, String uriString, UriInterface parent) {
        this.setScheme(scheme);
        this.setUri(uriString);
        this.parents = new HashSet<>();
        this.parents.add(parent);

        try {
            this.setConnection();
            this.setHttpStatusCode();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String getScheme() {
        return this.scheme;
    }

    @Override
    public String getUri() {
        return this.uri;
    }

    @Override
    public Set<UriInterface> getParents() {
        return this.parents;
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
     * @return parsed HTML of web page
     */
    public Document getDocument() {
        return this.document;
    }

    /**
     * @return analysis of web page
     */
    public String getAnalysis() {
        return this.analysis;
    }


    @Override
    public void setScheme(String scheme) {
        if (scheme == null || scheme.trim().length() == 0) throw new IllegalArgumentException("Scheme of Http object is empty");
        else this.scheme = scheme.trim();
    }

    @Override
    public void setUri(String uriString) {
        if (uriString != null && uriString.trim().length() > 0) this.uri = uriString.trim();
        else throw new IllegalArgumentException("Attempt to set empty URI");
    }

    @Override
    public void updateUri(UriInterface uriObject) {
        if (uriObject == null) throw new IllegalArgumentException("Empty URI provided for update");

        if (uriObject.getUri().equals(this.getUri())) {
            this.getParents().addAll(uriObject.getParents());
        } else throw new IllegalArgumentException("Incorrect URI provided for update: " + uriObject.getUri() + " Current one is " + this.getUri());
    }

    /**
     * Sets connection to URL
     */
    private void setConnection() {
        if (this.getConnection() == null) {
            this.connection = HttpHelper.connectToUrl(this.getUri());
        }
    }

    /**
     * Sets HTTP status code
     */
    private void setHttpStatusCode() throws IOException {
        if (this.getConnection() != null) this.httpStatusCode = this.getConnection().getResponseCode();
    }

    /**
     * Parses HTML of web page
     */
    public void setDocument() throws IOException {
        if (this.getConnection() != null && this.getDocument() == null) {
            this.document = HttpHelper.parseHtmlDocument(this.getConnection());
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
            for (UriInterface parent : this.getParents()) {
                if (parent == null) sb.append("null");
                else sb.append("\n\t").append(parent.getUri());
            }
        }

        if (this.getConnection() != null && HttpHelper.isWebPage(this.getConnection()) && this.getDocument() != null) {
            String title = this.getDocument().title();
            int numberOfH1 = this.getDocument().select("h1").size();

            sb.append(String.format("\tTitle: %s, \tCode: %s, \th1 tags: %s",
                    title, this.getHttpStatusCode(), numberOfH1));
        } else {
            sb.append(String.format("\tCode: %s", this.getHttpStatusCode()));
        }
        this.analysis = sb.toString();
    }

    @Override
    public void actionAfterUriAddedToBuffer(Crawler crawler) {
        System.out.println("New HTTP URI has been added to the buffer: " + this.getUri());

        // Parse HTML and sets analysis
        if (this.getHttpStatusCode() >= 200 && this.getHttpStatusCode() < 300) {
            if (HttpHelper.isWebPage(this.getConnection()) && HttpHelper.isInternal(crawler, this.getUri())) {
                try {
                    this.setDocument();
                    parseWebPageLinks(crawler);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            this.setAnalysis();
        }
    }

    /**
     * Parses HTML document and gets internal links and adds them to buffer
     * @param crawler
     */
    public void parseWebPageLinks(Crawler crawler) {
        // Adds child URIs to buffer
        for (String uri : HttpHelper.getPageLinks(this.getDocument())) {

            // If found URI is URL and not absolute, make it absolute
            if (HttpHelper.isUrl(uri) && !HttpHelper.isUrlAbsolute(uri)) {
                if (!HttpHelper.isUrlAbsolute(uri)) {
                    uri = HttpHelper.makeUrlAbsolute(uri, HttpHelper.getHostFromUrl(this.getUri()));
                }
            }

            // If found URI exists in buffer, update it; otherwise add it to buffer
            UriInterface uriFromBuffer = crawler.getBuffer().getURIs().get(uri);
            if (uriFromBuffer != null) {
                uriFromBuffer.getParents().add(this);
            }
            else crawler.getBuffer().addUri(crawler.getUriFactory().create(uri, this));
        }
    }


    @Override
    public String toString() {
        return this.getUri();
    }

    @Override
    public int compareTo(UriInterface urlLink) {
        return this.getUri().compareTo(urlLink.getUri());
    }
}
