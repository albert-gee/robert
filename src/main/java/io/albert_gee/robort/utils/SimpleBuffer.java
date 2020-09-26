package io.albert_gee.robort.utils;

import io.albert_gee.robort.entities.UnknownUri;
import io.albert_gee.robort.helpers.URIHelper;
import io.albert_gee.robort.interfaces.Buffer;
import io.albert_gee.robort.interfaces.URI;
import io.albert_gee.robort.interfaces.UriFactory;
import lombok.Getter;

import java.lang.reflect.InvocationTargetException;
import java.util.*;

/**
 * This class represents a simple buffer where URI are stored
 */
public class SimpleBuffer implements Buffer {

    @Getter
    private final Queue<String> uris;
    private final Map<String, URI> uriInstances;
    private final UriFactory uriFactory;
    private final Set<String> hosts;


    public SimpleBuffer(UriFactory uriFactory) {
        uris = new LinkedList<>();
        uriInstances = new HashMap<>();
        this.uriFactory = uriFactory;
        this.hosts = new HashSet<>();
    }

    /**
     * Add a URI to the buffer
     * @param uri URI
     */
    public void add(String uri) {
        if (uri == null) {
            throw new IllegalArgumentException("URI can't be null");
        }

        if (!uris.contains(uri)) {
            uris.add(uri);

            URI uriInstance;
            try {
                uriInstance = uriFactory.create(uri);
                uriInstance = (uriInstance == null) ? new UnknownUri(uri) : uriInstance;
            } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException | InstantiationException e) {
                uriInstance = new UnknownUri(uri);
            }

            uriInstances.put(uriInstance.getScheme(), uriInstance);

            uriInstance.process(this);
        } else {
//            uriInstances.get(uri).update();
        }
    }

    public boolean isInHosts(String uri) {
        uri = URIHelper.parseAuthority(uri);
        return this.hosts.contains(uri);
    }

    /**
     * Add a URI to the buffer
     * @param uris URI
     */
    public void addAll(List<String> uris) {
        for (String uri : uris) {
            add(uri);
        }
    }

    /**
     * Retrieves and removes the first element
     * @return First URI
     */
    public String next() {
        return uris.poll();
    }

    public boolean hasNext() {
        return uris.size() > 0;
    }

    public void setHosts(String[] hosts) {
        for (String host : hosts) {
            host = URIHelper.parseAuthority(host);
            this.hosts.add(host);
            System.out.println("Host added: " + host);
        }
    }

}
