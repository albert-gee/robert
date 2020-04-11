package io.robort.crawler;

import io.robort.crawler.interfaces.BufferInterface;
import io.robort.crawler.interfaces.UriInterface;

import java.util.HashMap;
import java.util.Map;

/**
 * Singleton collecting unique URIs
 */
public class SimpleBuffer implements BufferInterface {

    private Map<String, UriInterface>   URIs;
    private Crawler                     crawler;

    public SimpleBuffer() {
        this.URIs = new HashMap<>();
    }

    @Override
    public Map<String, UriInterface> getURIs() {
        return this.URIs;
    }

    /**
     * @return Crawler object
     */
    public Crawler getCrawler() {
        return this.crawler;
    }

    /**
     * Sets Crawler
     * @param crawler - Crawler object
     */
    public void setCrawler(Crawler crawler) {
        if (crawler == null) throw new IllegalArgumentException("Provided Crawler object is null");
        this.crawler = crawler;
    }

    /**
     * Adds one or more unique URIs to buffer. If URI already exists, updates it
     * @param uris - one or more URI objects
     */
    public void addUri(UriInterface... uris) {

        if (uris.length > 0) {
            for (UriInterface uriObject : uris) {
                if (uriObject != null) {
                    // If URI is in buffer already, just update it
                    if (isUriInBuffer(uriObject)) {
                        UriInterface uriObjectFromBuffer = this.findUriInBuffer(uriObject.getUri());
                        uriObjectFromBuffer.updateUri(uriObject);

                    // If URI is not in buffer, add it
                    } else {
                        this.URIs.put(uriObject.getUri(), uriObject);
                        uriObject.actionAfterUriAddedToBuffer(this.getCrawler());
                    }
                } else {
                    throw new IllegalArgumentException("Attempt to add empty URI to buffer");
                }
            }
        } else throw new IllegalArgumentException("You are trying to add new URIs to buffer but you didn't pass anything");
    }

    /**
     * Check if link is in buffer
     * @param uriInterface - URI object
     * @return boolean
     */
    public boolean isUriInBuffer(UriInterface uriInterface) {
        if (uriInterface == null) throw new IllegalArgumentException("Provided URI is null");
        return this.getURIs().containsKey(uriInterface.getUri());
    }

    /**
     * @param uriString - uri string
     * @return URI object from buffer or NULL
     */
    public UriInterface findUriInBuffer(String uriString) {
        return this.getURIs().get(uriString);
    }
}
