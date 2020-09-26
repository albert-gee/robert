package io.albert_gee.robort;

import io.albert_gee.robort.entities.*;
import io.albert_gee.robort.utils.SimpleUriFactory;
import io.albert_gee.robort.interfaces.Buffer;
import io.albert_gee.robort.interfaces.UriFactory;
import io.albert_gee.robort.utils.Crawler;
import io.albert_gee.robort.utils.SimpleBuffer;
import io.albert_gee.robort.utils.WebPagesBuffer;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * This is the entry point of the application
 */
public class Driver {

    public static void main(String[] args) {
        String[] uris = promptURIs();
        scan(uris);
    }

    private static String[] promptURIs() {
        List<String> uris = new ArrayList<>();
        String response = "";
        while(!response.equals("scan")) {
            System.out.println("Please enter a URI or scan to start scanning:");
            Scanner input = new Scanner(System.in);
            response = input.next();

            if(!response.equals("scan")) {
                uris.add(response);
            }
        }

        return uris.toArray(new String[0]);
    }

    /**
     *
     * @param uri
     */
    private static void scan(String... uri) {
        System.out.println("Start scanning");

        UriFactory uriFactory = initUriFactory();
        Buffer buffer = initBuffer(uriFactory);

        Crawler crawler = new Crawler(buffer);
        crawler.handle(uri);

        WebPagesBuffer webPagesBuffer = WebPagesBuffer.getInstance();
        System.out.println("*************Printing web pages");
        for (HtmlDocument htmlDocument : webPagesBuffer.getPages()) {
            System.out.println(htmlDocument.getUri() + " (" + htmlDocument.getStatusCode() + ") - " + htmlDocument.getLinks().size() + " links");
        }

    }

    /**
     * Initializes UriFactory instance and adds rules
     * @return UriFactory
     */
    private static UriFactory initUriFactory() {
        UriFactory uriFactory = new SimpleUriFactory();
        uriFactory.addRule("tel", Tel.class);
        uriFactory.addRule("mailto", Mailto.class);
        uriFactory.addRule("http",   Http.class);
        uriFactory.addRule("https",  Https.class);
        uriFactory.addRule(null,     UnknownUri.class);

        return uriFactory;
    }

    /**
     * Initializes the Buffer
     * @return Buffer
     */
    private static Buffer initBuffer(UriFactory uriFactory) {
        return new SimpleBuffer(uriFactory);
    }
}
