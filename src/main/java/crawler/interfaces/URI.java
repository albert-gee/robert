package crawler.interfaces;

import java.util.Set;

/**
 * This interface describes a URI
 */
public interface URI extends Comparable<URI> {

    /**
     * @return URI string
     */
    String getUri();

    /**
     * Where URI was found
     * @return
     */
    Set<URI> getParents();

    /**
     * Sets URI string
     * @param uriString - URI string
     */
    void setUri(String uriString);

    /**
     * @param uriObject - URI object
     */
    void updateUri(URI uriObject);

    /**
     * This method is supposed to be executed after object was added to buffer
     */
    void actionAfterUriAddedToBuffer();
}
