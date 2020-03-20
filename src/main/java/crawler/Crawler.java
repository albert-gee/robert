package crawler;

import crawler.interfaces.Buffer;
import crawler.interfaces.URI;

import crawler.uriEntities.File;
import crawler.uriEntities.Http;
import crawler.uriEntities.Mailto;
import crawler.uriEntities.Tel;

import java.lang.reflect.InvocationTargetException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

/**
 * This class describes a crawler that fetches all website URIs and analyzed them
 */
public class Crawler {

    private static Buffer buffer;
    private static String host; // must not contain protocol, e.g. "www.google.com" without "https://"

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

    public static void main(String... args) {

        host = "messino.creativepace.com";

        buffer = SimpleBuffer.create("https://messino.creativepace.com");

        Map<String, Class<?>> rules = new HashMap<>();
        rules.put("tel",    Tel.class);
        rules.put("mailto", Mailto.class);
        rules.put("http",   Http.class);
        rules.put("https",  Http.class);
        rules.put("file",   File.class);

        Classifier classifier = null;
        classifier = new Classifier(buffer, rules);
        Map<String, Map<String, URI>> classifiedURIs = classifier.getClassifiedURIs();

        System.out.println("***********");
        Map<String, URI> webPages = classifiedURIs.get("https");
        webPages.putAll(classifiedURIs.get("http"));
        Map<String, URI> sorted = new TreeMap<>(webPages);
        sorted.forEach((uri, uriObject) -> {
            Http httpObject = (Http) uriObject;
            System.out.println(httpObject.getAnalysis());
        });
    }
}
