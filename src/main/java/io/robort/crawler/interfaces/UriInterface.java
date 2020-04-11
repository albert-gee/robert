package io.robort.crawler.interfaces;

import io.robort.crawler.Crawler;

import java.util.Set;

/**
 * This interface describes a URI
 */
public interface UriInterface extends Comparable<UriInterface> {

    /**
     * @return URI string
     */
    String getUri();

    /**
     * @return URI scheme
     */
    String getScheme();

    /**
     * @return set of URI objects where current URI was found
     */
    Set<UriInterface> getParents();

    /**
     * Sets URI string
     * @param uriString - URI string
     */
    void setUri(String uriString);

    /**
     * Sets URI scheme
     * @param scheme - URI scheme
     */
    void setScheme(String scheme);

    /**
     * @param uriObject - URI object
     */
    void updateUri(UriInterface uriObject);

    /**
     * This action is performed after URI is added to buffer
     * @param crawler - Crawler object
     */
    void actionAfterUriAddedToBuffer(Crawler crawler);
}
