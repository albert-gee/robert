package crawler;

import crawler.interfaces.Buffer;
import crawler.interfaces.URI;

import crawler.uriEntities.File;
import crawler.uriEntities.Http;
import crawler.uriEntities.Mailto;
import crawler.uriEntities.Tel;

import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

/**
 * This class describes a crawler that fetches all website URIs and analyzed them
 */
public class Crawler {

    private static UriFactory   uriFactory;
    private static String       hostProtocol;
    private static String       host; // must not contain protocol, e.g. "www.google.com" without "https://"
    private static Buffer       buffer;

    public static void main(String... args) {
        setHostProtocol("https");
        setHost("hocbr.creativepace.com");
        setUriFactory();
        buffer = SimpleBuffer.create();
        buffer.addUri(uriFactory.createUriObject(getHostProtocol() + "://" + getHost()));

        printResult();

    }

    /**
     * @return Baffer singleton
     */
    public static Buffer getBuffer() {
        return buffer;
    }

    /**
     * Host of website to be analyzed.
     * URLs belonging to another host won't be analyzed
     * @return host
     */
    public static String getHost() {
        return host;
    }

    /**
     * @return host protocol
     */
    public static String getHostProtocol() {
        return hostProtocol;
    }

    /**
     * @return UriFactory singleton
     */
    public static UriFactory getUriFactory() {
        return uriFactory;
    }

    /**
     * Creates UriFactory instance and sets rules
     */
    private static void setUriFactory() {
        uriFactory = UriFactory.getInstance();
        Map<String, Class<?>> rules = new HashMap<>();
        rules.put("tel",    Tel.class);
        rules.put("mailto", Mailto.class);
        rules.put("http",   Http.class);
        rules.put("https",  Http.class);
        rules.put("file",   File.class);


        uriFactory.setRules(rules);
    }

    /**
     * Sets hosts without protocol
     * @param hostString - host string
     */
    private static void setHost(String hostString) {

        if (hostString != null && hostString.trim().length() != 0) {
            String hostWithoutProtocol = hostString;

            // Gets scheme of URI, eg http or mailto
            int firstOccurrenceOfSlashes = hostString.indexOf("://");

            if (firstOccurrenceOfSlashes != -1) {
                hostWithoutProtocol = hostString.substring(firstOccurrenceOfSlashes + 3);
            }

            host = hostWithoutProtocol;
        } else {
            throw new IllegalArgumentException("Empty host was provided");
        }
    }

    /**
     * Sets host protocol
     * @param protocol - host protocol
     */
    private static void setHostProtocol(String protocol) {
        Crawler.hostProtocol = protocol;
    }

    private static void printResult() {
        // Print result
        Classifier classifier = new Classifier();
        Map<String, Map<String, URI>> schemes = classifier.getClassifiedURIs();

        if (schemes != null) {
            for (String schemeKey : schemes.keySet()) {
                System.out.println("SCHEME " + schemeKey);

                Map<String, URI> schemeUris = schemes.get(schemeKey);
                Map<String, URI> sorted = new TreeMap<>(schemeUris);

                for (String uriString : sorted.keySet()) {
                    if (schemeUris.get(uriString) instanceof  Http) {
                        Http http = (Http) schemeUris.get(uriString);
                        System.out.println(http.getUri() + " " + http.getHttpStatusCode());
                    } else {
                        System.out.println(uriString);
                    }
                }
            }
        } else {
            System.out.println("There are no URIs");
        }
    }
}
