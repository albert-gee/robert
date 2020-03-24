package crawler;

import crawler.interfaces.BufferInterface;
import crawler.interfaces.HandlerInterface;
import crawler.interfaces.UriFactoryInterface;
import crawler.interfaces.UriInterface;
import crawler.uriRuleEntities.Http;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

/**
 * This class describes a crawler
 */
public class Crawler {

    private BufferInterface         buffer;
    private UriFactoryInterface     uriFactory;
    private HandlerInterface        handler;
    private Set<String>             hosts; // First URIs added to the buffer in Crawler class


    public Crawler(BufferInterface buffer, UriFactoryInterface uriFactory, HandlerInterface handler) {
        // Sets UriFactory
        if (uriFactory != null) this.uriFactory = uriFactory;
        else throw new IllegalArgumentException("URI factory class was not provided");

        // Sets Buffer
        if (buffer != null) this.buffer = buffer;
        else throw new IllegalArgumentException("Buffer was not provided");

        // Sets Handler
        if (handler != null) this.handler = handler;
        else throw new IllegalArgumentException("Handler class was not provided");

        // Sets hosts
        this.hosts = new HashSet<>();
    }

    /**
     * @return buffer of URIs
     */
    public BufferInterface getBuffer() {
        return this.buffer;
    }

    /**
     * @return factory object that creates URIs
     */
    public UriFactoryInterface getUriFactory() {
        return this.uriFactory;
    }

    /**
     * @return handler object that manipulates URIs from buffer
     */
    public HandlerInterface getHandler() {
        return this.handler;
    }

    /**
     * @return Set of unique hosts
     */
    public Set<String> getHosts() {
        return this.hosts;
    }


    /**
     * Manipulates with URIs from buffer
     */
    public void handle() {
        this.getHandler().handle(this.getBuffer());
    }

    /**
     * This method can be called only ones
     * @param uris
     */
    public void addUris(String... uris) {

        if (this.getHosts().size() > 0) throw new IllegalArgumentException("You can add hosts only once");

        if (uris.length > 0) {

            this.getBuffer().setCrawler(this);

            for (String uri : uris) {
                if (uri != null && uri.trim().length() > 0) {
                    UriInterface uriObject = this.getUriFactory().create(uri.trim(), null);
                    this.getBuffer().addUri(uriObject);

                    // If provided URI is URL add it to set of hosts
                    if (uriObject.getScheme().equals("https") || uriObject.getScheme().equals("http")) {
                        Http httpObject = (Http) uriObject;
                        this.getHosts().add(httpObject.getUri().trim());
                        try {
                            httpObject.setDocument();
                            httpObject.parseWebPageLinks(this);
                        } catch (IOException ex) {
                            System.out.println(ex.getMessage());
                        }
                    } else {
                        System.out.println("Invalid host was not added: " + uriObject.getUri() + " Invalid scheme is " + uriObject.getScheme());
                    }

                } else {
                    throw new IllegalArgumentException("Attempt to add empty URI to buffer");
                }
            }
        } else {
            throw new IllegalArgumentException("URIs were not provided");
        }
    }
}
