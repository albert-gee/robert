package crawler;

import crawler.interfaces.Buffer;

import java.util.ArrayList;
import java.util.List;

/**
 * Singleton collecting unique URIs
 */
public class SimpleBuffer implements Buffer {

    private static SimpleBuffer instance;
    private List<String> URIs;

    /**
     * This constructor is private. It is called when the create() method is executed
     */
    private SimpleBuffer(String... uris) {
        this.URIs = new ArrayList<>();
        this.addUri(uris);
    }

    /**
     * Creates singleton instance
     */
    public static SimpleBuffer create(String... uris) {
        if (SimpleBuffer.instance == null) {
            SimpleBuffer.instance = new SimpleBuffer(uris);
        }

        return instance;
    }

    /**
     * Adds new unique URI to HashMap
     * @param uris - unlimited number of URI objects
     */
    public void addUri(String... uris) {

        if (uris.length > 0) {
            for (String uri : uris) {

                if (this.getURIs().contains(uri)) continue;

                // URI string is not empty
                if (uri != null && uri.trim().length() > 0) {
                    String trimmedUri = uri.trim();
                    this.URIs.add(trimmedUri);
                } else {
                    throw new IllegalArgumentException("Attempt to add empty URI to buffer");
                }
            }
        } else {
            throw new IllegalArgumentException("Provided array of URIs is empty");
        }
    }

    /**
     * @return Set of unique URIs
     */
    public List<String> getURIs() {
        return this.URIs;
    }

    /**
     * Check if link is in buffer
     * @param link - link
     * @return boolean
     */
    public boolean isLinkInBuffer(String link) {
        return this.URIs.contains(link);
    }
}
