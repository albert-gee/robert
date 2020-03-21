package crawler;

import crawler.interfaces.URI;
import crawler.uriEntities.InvalidURI;

import java.lang.reflect.InvocationTargetException;
import java.util.Map;

/**
 * Creates URI object according to provided rules (instances of classes belonging to uriEntities)
 *
 */
public class UriFactory {

    private static UriFactory       singleton;
    private Map<String, Class<?>>   rules; // Map key is scheme and value is rule class name

    private UriFactory() {

    }

    /**
     * Creates singleton instance of the class
     */
    public static UriFactory getInstance() {
        if (singleton == null) {
            singleton = new UriFactory();
        }

        return singleton;
    }

    /**
     * Checks and sets rules
     * @param rules - Map where key is name of scheme and value is name of class representing URI
     */
    public void setRules(Map<String, Class<?>> rules) {
        if (rules != null) {
            rules.forEach((scheme, className) -> {
                if (scheme == null || scheme.trim().length() == 0 || className == null) {
                    throw new IllegalArgumentException("Some of your rules are empty");
                }
            });

            this.rules = rules;
            this.rules.put(null, InvalidURI.class); // For invalid URIs
        } else {
            throw new IllegalArgumentException("Attempt to set rules twice");
        }
    }

    /**
     * Determines scheme of provided URI
     * @param uriString - URI
     * @return scheme of provided URI or NULL if such scheme doesn't exist or URI doesn't contain scheme
     */
    public String determineScheme(String uriString) {

        if (uriString == null) {
            throw new IllegalArgumentException("Couldn't determine scheme - provided URI is null");
        } else {

            String scheme = null;
            if (uriString.trim().length() != 0) {
                // Gets scheme of URI, eg http or mailto
                int firstOccurrenceOfColon = uriString.indexOf(":");

                if (firstOccurrenceOfColon != -1) {
                    scheme = uriString.substring(0, firstOccurrenceOfColon);
                }
            }

            return scheme;
        }
    }

    /**
     * Factory method that creates a URI object of provided URI string
     * @param uriString - URI string
     * @return URI object
     */
    public URI createUriObject(String uriString) {

        if (uriString == null || uriString.trim().length() == 0) throw new IllegalArgumentException("Attempt to create a object of URI which is empty or NULL");
        URI uriObject = new InvalidURI(uriString);

        try {
            String scheme = determineScheme(uriString);

            if (scheme != null) {
                Class<?> schemeClass = rules.get(scheme);
                uriObject = (URI) schemeClass.getConstructor(String.class).newInstance(uriString);
            }
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException | InstantiationException ex) {
            ex.printStackTrace();
        }
        return uriObject;
    }
}
