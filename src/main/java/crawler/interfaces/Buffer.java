package crawler.interfaces;

import java.util.List;

/**
 * This interface describes a buffer where all links added to be analyzed.
 * Buffer exists in order to avoid duplicating same links.
 */
public interface Buffer {

    /**
     * Adds new unique URI to HashMap
     * @param uris - unlimited number of URI objects
     */
    void addUri(String... uris);

    /**
     * @return Set of unique URIs
     */
    List<String> getURIs();
}
