package io.albert_gee.robort.entities;

import io.albert_gee.robort.helpers.URLHelper;
import io.albert_gee.robort.interfaces.Buffer;
import io.albert_gee.robort.interfaces.Document;
import io.albert_gee.robort.interfaces.URI;
import io.albert_gee.robort.utils.HttpClient;
import io.albert_gee.robort.utils.WebPagesBuffer;

import java.io.IOException;
import java.util.List;

public class Http implements URI {

    private String url;
    private String host;

    public Http(String url, String host) {
        setUrl(url);
        setHost(host);
    }

    /**
     * Set URL
     * @param url URL
     */
    private void setUrl(String url) {
        if (url == null || url.trim().isEmpty()) {
            throw new IllegalArgumentException("HTTP URI must be not empty");
        } else {
            this.url = URLHelper.makeUrlAbsolute(url, this.getHost());
        }
    }

    /**
     * Set host
     * @param host Host
     */
    private void setHost(String host) {
        if (host == null || host.trim().length() == 0) {
            throw new IllegalArgumentException("Host can not be empty");
        }
    }

    /**
     * Get URI
     * @return URI
     */
    public String getUri() {
        return url;
    }

    /**
     * Get host
     * @return URI
     */
    public String getHost() {
        return host;
    }

    /**
     * URI scheme
     * @return URI scheme
     */
    public String getScheme() {
        return "http";
    }

    /**
     * Process URI
     * @param buffer Buffer where URIs are stored
     */
    public void process(Buffer buffer) {
        try {
            HttpClient client = new HttpClient(getUri());

            System.out.println("Processing URI: " + client.getStatusCode() + " " + getUri());
            if (client.getStatusCode() >= 300 && client.getStatusCode() <= 399) {
                return;
            }
            if (client.getStatusCode() == 200) {
                Document document = client.getDocument();

                // Parse only internal html documents
                if (document instanceof HtmlDocument && buffer.isInHosts(this.getHost())) {
                    WebPagesBuffer webPagesBuffer = WebPagesBuffer.getInstance();
                    webPagesBuffer.add((HtmlDocument) document);

                    HtmlDocument htmlDocument = (HtmlDocument) document;
                    List<String> urls =  htmlDocument.getUrls();
                    buffer.addAll(urls);
                }
            }
        } catch (IOException e) {
            System.out.println("COULDN't CONNECT: (" + getUri() + ") ");
        }
    }

    @Override
    public void update() {

    }
}
