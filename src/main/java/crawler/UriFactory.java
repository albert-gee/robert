package crawler;

import crawler.interfaces.UriFactoryInterface;
import crawler.interfaces.UriInterface;
import crawler.uriRuleEntities.UnknownUri;

import java.lang.reflect.InvocationTargetException;
import java.util.Map;

/**
 * Creates URI object according to provided rules (instances of classes belonging to uriEntities)
 */
public class UriFactory implements UriFactoryInterface {

    private Map<String, Class<?>> rules; // Map key is scheme and value is rule class implementing UriInterface

    public UriFactory(Map<String, Class<?>> rules) {
        if (rules != null) {
            rules.forEach((scheme, className) -> {
                if (className == null) throw new IllegalArgumentException("Some of your rules are empty");
            });
            this.rules = rules;
        } else {
            throw new IllegalArgumentException("Rules for UriFactory were not provided");
        }
    }

    /**
     * Determines scheme of provided URI
     * @param uriString - URI
     * @return scheme of provided URI or NULL if such scheme doesn't exist or URI doesn't contain scheme
     */
    private String determineScheme(String uriString) {
        if (uriString == null) throw new IllegalArgumentException("Couldn't determine scheme - provided URI is null");
        else {
            String scheme = null;
            if (uriString.trim().length() != 0) {
                int firstOccurrenceOfColon = uriString.indexOf(":"); // Finds position of first occurrence of colon in uriString
                if (firstOccurrenceOfColon != -1) scheme = uriString.substring(0, firstOccurrenceOfColon); // Gets scheme of URI, eg http or mailto
            }
            return scheme;
        }
    }

    /**
     * Factory method that creates a URI object of provided URI string
     * @param uriString - URI string
     * @return URI object
     */
    public UriInterface create(String uriString, UriInterface parent) {
        if (uriString == null || uriString.trim().length() == 0) throw new IllegalArgumentException("Attempt to create an object of empty URI");
        UriInterface uriObject = null;

        try {
            String scheme = determineScheme(uriString);

            if (rules.get(scheme) == null) {
                uriObject = new UnknownUri(scheme, uriString, parent);
                System.out.println("Unknown URI found: " + uriString + " Scheme: " + scheme);
            }
            else {
                Class<?> schemeClass = rules.get(scheme);
                uriObject = (UriInterface) schemeClass.getConstructor(String.class, String.class, UriInterface.class).newInstance(scheme, uriString, parent);
            }
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException | InstantiationException ex) {
            ex.printStackTrace();
        }
        return uriObject;
    }
}
