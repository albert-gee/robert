package crawler;

import crawler.interfaces.Buffer;
import crawler.interfaces.URI;

import java.util.*;


/**
 * Classifies URI objects from buffer according to their scheme
 */
public class Classifier {

    private Map<String, Map<String, URI>>   classifiedURIs; // Map key is scheme and value is Map object where key is URI and value is URI object

    public Classifier() {

        this.setClassifiedURIs();

        this.classify();
    }

    /**
     * Sets Map of classified URIs
     */
    private void setClassifiedURIs() {
        this.classifiedURIs = new HashMap<>();
    }

    /**
     * @return Buffer
     */
    public Buffer getBuffer() {
        return Crawler.getBuffer();
    }

    /**
     * Loops through URIs in buffer and separates them according to scheme.
     * Records results into {@link #classifiedURIs} instance variable
     */
    private void classify() {
        if (this.getBuffer().getURIs() != null) {

            LinkedHashMap<String, URI> linkedHashMap = (LinkedHashMap<String, URI>) this.getBuffer().getURIs();

            linkedHashMap.forEach((uriString, uriObject) -> {
                String scheme = Crawler.getUriFactory().determineScheme(uriObject.getUri());

                this.addClassifiedUri(scheme, uriObject);
            });
        }
    }




    /**
     * Adds classified URI according to its scheme
     * @param scheme - URI scheme
     * @param uriObject - URI object
     */
    private void addClassifiedUri(String scheme, URI uriObject) {

        if (!this.classifiedURIs.containsKey(scheme)) {
            Map<String, URI> uriMap = new HashMap<>();
            uriMap.put(uriObject.getUri(), uriObject);
            this.classifiedURIs.put(scheme, uriMap);
        } else {
            // Gets Map of all URIs with same scheme
            Map<String, URI> uriMap = this.classifiedURIs.get(scheme);

            if (uriMap.containsKey(uriObject.getUri())) {
                URI existingUri = uriMap.get(uriObject.getUri());
                existingUri.updateUri(uriObject);
            } else {
                uriMap.put(uriObject.getUri(), uriObject);
            }
        }
    }

    /**
     * @return Map of classified URIs, where the key is a scheme and the value is a Map of URIs
     */
    public Map<String, Map<String, URI>> getClassifiedURIs() {
        return this.classifiedURIs;
    }
}
