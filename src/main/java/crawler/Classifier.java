package crawler;

import crawler.uriEntities.InvalidURI;
import crawler.interfaces.Buffer;
import crawler.interfaces.URI;

import java.lang.reflect.InvocationTargetException;
import java.util.*;

/**
 * This class describes the web crawler.
 * It can fetch links recursively of a website any depth.
 */
public class Classifier {

    private Map<String, Class<?>>           rules; // Map key is scheme and value is rule class name
    private Buffer                          buffer;
    private Map<String, Map<String, URI>>   classifiedURIs; // Map key is scheme and value is Map object where key is URI and value is URI object

    /**
     *
     * @param buffer - buffer
     * @param rules - HashMap, where key is scheme and value is class name
     */
    public Classifier(Buffer buffer, Map<String, Class<?>> rules) {

        this.setBuffer(buffer);
        this.setRules(rules);
        this.setClassifiedURIs();

        this.classify();
    }

    /**
     * @return Buffer
     */
    public Buffer getBuffer() {
        return this.buffer;
    }
    /**
     * Sets buffer
     * @param buffer - buffer
     */
    private void setBuffer(Buffer buffer) {
        if (buffer != null) this.buffer = buffer;
        else throw new IllegalArgumentException("Buffer was not provided");
    }

    /**
     * Checks and sets rules
     * @param rules - Map where key is name of scheme and value is name of class representing URI
     */
    private void setRules(Map<String, Class<?>> rules) {
        if (rules != null) {
            rules.forEach((scheme, className) -> {
                if (scheme == null || scheme.trim().length() == 0 || className == null) {
                    throw new IllegalArgumentException("Some of your rules are empty");
                }
            });

            this.rules = rules;
            this.rules.put(null, InvalidURI.class); // For invalid URIs
        }
    }

    /**
     * Sets Map of classified URIs
     */
    private void setClassifiedURIs() {
        this.classifiedURIs = new HashMap<>();

        this.rules.forEach((schema, className) -> {
            this.classifiedURIs.put(schema, new HashMap<>());
        });
    }

    /**
     * Loops through URIs in buffer and separates them according to scheme.
     * Records results into {@link #classifiedURIs} instance variable
     */
    private void classify() {

        if (this.buffer.getURIs() != null) {

            // Loops through HashSet of URIs in buffer
            for (int i = 0; i < this.getBuffer().getURIs().size(); i++) {
                String uriString = this.getBuffer().getURIs().get(i);
                // Creates URI object according to URI scheme and provided rules
                String scheme = this.determineScheme(uriString);
                URI uriObject = this.createUriObject(scheme, uriString);

                this.addClassifiedUri(scheme, uriObject);
            }
        }


    }

    /**
     * Determines scheme of provided URI
     * @param uriString - URI
     * @return scheme of provided URI or NULL
     */
    private String determineScheme(String uriString) {

        String scheme = null;

        if (uriString != null && uriString.trim().length() != 0) {
            // Gets scheme of URI, eg http or mailto
            int firstOccurrenceOfColon = uriString.indexOf(":");

            String partOfUriBeforeColon = "";
            if (firstOccurrenceOfColon != -1) {
                partOfUriBeforeColon = uriString.substring(0, firstOccurrenceOfColon);
            }

            // Checks if there is a rule corresponding such URI
            if (this.rules.containsKey(partOfUriBeforeColon)) {
                scheme = partOfUriBeforeColon;
            }
        }
        return scheme;
    }

    /**
     * Factory method that creates a URI object of provided URI string
     * @param uriString - URI string
     * @return URI object
     */
    private URI createUriObject(String scheme, String uriString) {
        URI uriObject = null;

        try {

            if (scheme != null && scheme.length() > 0 && uriString != null && uriString.length() > 0) {

                Class<?> schemeClass = this.rules.get(scheme);
                uriObject = (URI) schemeClass.getConstructor(String.class)
                        .newInstance(uriString);

            } else {
                uriObject = new InvalidURI(uriString);
            }

        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException | InstantiationException ex) {
            ex.printStackTrace();
        }
        return uriObject;

    }

    /**
     * Adds classified URI according to its scheme
     * @param scheme - URI scheme
     * @param uriObject - URI object
     */
    private void addClassifiedUri(String scheme, URI uriObject) {

        // Gets Map of all URIs with same scheme
        Map<String, URI> schemeUris = this.classifiedURIs.get(scheme);

        if (!schemeUris.containsKey(uriObject.getUri())) {
            schemeUris.put(uriObject.getUri(), uriObject);

        } else {
            System.out.println("Update" + uriObject);

            schemeUris.get(uriObject.getUri()).updateUri(uriObject);
        }
    }

    /**
     * @return Map of classified URIs, where the key is a scheme and the value is a Map of URIs
     */
    public Map<String, Map<String, URI>> getClassifiedURIs() {
        return this.classifiedURIs;
    }
}
