package crawler;

import crawler.interfaces.Buffer;
import crawler.interfaces.URI;
import crawler.uriEntities.InvalidURI;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Singleton collecting unique URIs
 */
public class SimpleBuffer implements Buffer {

    private static SimpleBuffer     instance;
    private Map<String, URI>        URIs;

    /**
     * This constructor is private. It is called when the create() method is executed
     * @param uris
     */
    private SimpleBuffer(URI... uris) {
        this.URIs = new LinkedHashMap<>();

        if (uris != null) {
            this.addUri(uris);
        }
    }

    /**
     * Creates singleton instance
     */
    public static SimpleBuffer create(URI... uris) {
        if (SimpleBuffer.instance == null) {
            SimpleBuffer.instance = new SimpleBuffer(uris);
        }

        return instance;
    }

    /**
     * Adds new unique URI to HashMap
     * @param uris - unlimited number of URI objects
     */
    public void addUri(URI... uris) {

        if (uris.length > 0) {
            for (URI uri : uris) {
                if (uri != null) {
                    // If URI is in buffer already, just update it
                    if (isUriInBuffer(uri)) {
                        URI uriFromBuffer = this.findUriInBuffer(uri.getUri());
                        uriFromBuffer.updateUri(uri);

                    // If URI is not in buffer, add it
                    } else {
                        this.URIs.put(uri.getUri(), uri);
                        System.out.println("New URI has been added to the buffer: " + uri.getUri());
                        uri.actionAfterUriAddedToBuffer();

                    }
                } else {
                    throw new IllegalArgumentException("Attempt to add empty URI to buffer");
                }
            }
        }
    }

    /**
     * @return Set of unique URIs
     */
    public Map<String, URI> getURIs() {
        return this.URIs;
    }

    /**
     * Check if link is in buffer
     * @param uri - URI object
     * @return boolean
     */
    public boolean isUriInBuffer(URI uri) {
        if (uri == null) throw new IllegalArgumentException("Provided URI is null");
        return this.getURIs().containsKey(uri.getUri());
    }

    /**
     * @param uriString - uri string
     * @return URI object from buffer or NULL
     */
    public URI findUriInBuffer(String uriString) {
        Map<String, URI> urisInBuffer = this.getURIs();
        if (urisInBuffer == null) return null;

        return urisInBuffer.get(uriString);
    }
}
