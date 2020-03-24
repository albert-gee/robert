package crawler.interfaces;

import crawler.Crawler;

import java.util.Map;

/**
 * This interface describes a buffer where all links added to be analyzed.
 * Buffer exists in order to avoid duplicating same links.
 */
public interface BufferInterface {

    /**
     * Adds new unique URI to HashMap
     * @param uris - unlimited number of URI objects
     */
    void addUri(UriInterface... uris);

    /**
     * @return Set of unique URIs
     */
    Map<String, UriInterface> getURIs();

    /**
     * @return
     */
    Crawler getCrawler();

    /**
     * @param crawler - Crawler object
     */
    void setCrawler(Crawler crawler);
}
