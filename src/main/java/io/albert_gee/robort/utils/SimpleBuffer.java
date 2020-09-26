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
    public void add(String uri, String parent) {
        if (uri == null || uri.trim().isEmpty()) {
            throw new IllegalArgumentException("URI can't be null");
        }

        // URI is not in the buffer yet
        if (!uris.contains(uri)) {
            uris.add(uri);

            URI uriInstance = createUriInstance(uri, parent);
            uriInstances.put(uri, uriInstance);
            uriInstance.process(this);
        }
        // URI is already in the buffer
        else {
            uriInstances.get(uri).update(parent);
        }
    }

    private URI createUriInstance(String uri, String parent) {
        URI uriInstance;
        try {
            uriInstance = uriFactory.create(uri, parent);
            uriInstance = (uriInstance == null) ? new UnknownUri(uri, parent) : uriInstance;
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException | InstantiationException e) {
            uriInstance = new UnknownUri(uri, parent);
        }
        return uriInstance;
    }

    public Map<String, URI> getUriInstances() {
        return uriInstances;
    }

    public boolean isInHosts(String uri) {
        uri = URIHelper.parseAuthority(uri);
        return this.hosts.contains(uri);
    }

    /**
     * Add a URI to the buffer
     * @param uris URI
     */
    public void addHosts(List<String> uris) {
        for (String uri : uris) {
            add(uri, null);
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
