package io.robort.crawler;

import io.robort.crawler.interfaces.BufferInterface;
import io.robort.crawler.interfaces.HandlerInterface;
import io.robort.crawler.interfaces.UriInterface;
import io.robort.crawler.uriRuleEntities.Http;

import java.util.Map;
import java.util.HashMap;
import java.util.TreeMap;


/**
 * Classifies URI objects from buffer according to their scheme
 */
public class Classifier implements HandlerInterface {

    private Map<String, Map<String, UriInterface>>   schemes; // Map where key is scheme and value is Map object where key is URI and value is URI object

    public Classifier() {
        this.schemes = new HashMap<>();
    }

    /**
     * Loops through URIs in buffer and separates them according to scheme.
     * Prints result.
     * Records results into {@link #schemes} instance variable
     */
    public void handle(BufferInterface buffer) {

        if (buffer.getURIs() != null) {
            buffer.getURIs().forEach((uriString, uriObject) -> this.addClassifiedUri(uriObject.getScheme(), uriObject));
        } else {
            throw new IllegalArgumentException("The buffer is empty");
        }

        printResult();
    }

    /**
     * Adds classified URI according to its scheme
     * @param scheme - URI scheme
     * @param uriObject - URI object
     */
    private void addClassifiedUri(String scheme, UriInterface uriObject) {

        // If there are no URIs belonging to such scheme, creates a new Map
        if (!this.schemes.containsKey(scheme)) {
            Map<String, UriInterface> schemeMap = new HashMap<>();
            schemeMap.put(uriObject.getUri(), uriObject);
            this.schemes.put(scheme, schemeMap);

        // Gets Map of all URIs with same scheme
        } else {

            // If such URI already exists, updates it
            if (this.schemes.get(scheme).containsKey(uriObject.getUri())) {
                UriInterface existingUri = this.schemes.get(scheme).get(uriObject.getUri());
                existingUri.updateUri(uriObject);

            // Adds new URI
            } else {
                this.schemes.get(scheme).put(uriObject.getUri(), uriObject);
            }
        }
    }

    /**
     * @return Map of classified URIs, where the key is a scheme and the value is a Map of URIs
     */
    public Map<String, Map<String, UriInterface>> getClassifiedURIs() {
        return this.schemes;
    }

    /**
     * Prints classified URIs
     */
    private void printResult() {
        if (this.getClassifiedURIs().size() == 0) System.out.println("There is nothing to handle");
        else {
            for (String scheme : this.getClassifiedURIs().keySet()) {
                System.out.println("*****************");

                if (scheme != null) {
                    System.out.println(scheme.toUpperCase() + " URIs");
                } else {
                    System.out.println("Undefined scheme URIs");
                }

                Map<String, UriInterface> schemeUris = this.getClassifiedURIs().get(scheme);
                TreeMap<String, UriInterface> sortedSchemeUris = new TreeMap<>(schemeUris);

                for (String uriString : sortedSchemeUris.keySet()) {
                    UriInterface uriObject = schemeUris.get(uriString);

                    if (uriObject instanceof Http) System.out.println(((Http) uriObject).getAnalysis());
                    else System.out.println(uriString);
                }
            }
        }
    }
}
