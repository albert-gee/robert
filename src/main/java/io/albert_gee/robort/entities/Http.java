package io.albert_gee.robort.entities;

import io.albert_gee.robort.helpers.URLHelper;
import io.albert_gee.robort.interfaces.Buffer;
import io.albert_gee.robort.interfaces.Document;
import io.albert_gee.robort.interfaces.URI;
import io.albert_gee.robort.utils.HttpClient;
import io.albert_gee.robort.utils.WebPagesBuffer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Http implements URI {

    private String url;
    private String parent;
    private ArrayList<String> foundOn;

    public Http(String url, String parent) {
        setUrl(url);
        setParent(parent);
        foundOn = new ArrayList<>();

        if (parent != null) {
            foundOn.add(parent);
        }
    }

    /**
     * Set URL
     * @param url URL
     */
    private void setUrl(String url) {
        if (url == null || url.trim().isEmpty()) {
            throw new IllegalArgumentException("HTTP URI must be not empty");
        } else {
            if (URLHelper.isAbsolute(url)) {
                this.url = url;
            } else {
                if (this.getParent() == null) {
                    throw new IllegalArgumentException("Relative URL without parent can't be added");
                }
                this.url = URLHelper.makeUrlAbsolute(url, this.getParent());
            }
        }
    }

    /**
     * Set parent
     * @param parent Parent
     */
    private void setParent(String parent) {
        this.parent = parent;
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
    public String getParent() {
        return parent;
    }

    public ArrayList<String> getFoundOn() {
        return foundOn;
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
            if (client.getStatusCode() >= 200 && client.getStatusCode() <= 299) {
                Document document = client.getDocument();

                // Parse only internal html documents or hosts (without parent)
                if (
                        document instanceof HtmlDocument &&
                        (this.getParent() == null || buffer.isInHosts(URLHelper.parseHost(getParent())))
                ) {
                    WebPagesBuffer webPagesBuffer = WebPagesBuffer.getInstance();
                    webPagesBuffer.add((HtmlDocument) document);

                    HtmlDocument htmlDocument = (HtmlDocument) document;
                    List<String> urls =  htmlDocument.getUrls();

                    for (String url : urls) {
                        buffer.add(URLHelper.makeUrlAbsolute(url, getUri()), getUri());
                    }
                }
            }
        } catch (IOException e) {
            System.out.println("COULDN't CONNECT: (" + getUri() + ") ");
        }
    }

    @Override
    public void update(String parent) {
        foundOn.add(parent);
    }
}
