package io.robort.crawler.uriRuleEntities;

import io.robort.crawler.Crawler;
import io.robort.crawler.interfaces.UriInterface;

import java.util.HashSet;
import java.util.Set;

/**
 * This class describes a URI with unknown or empty scheme or empty URI
 */
public class UnknownUri implements UriInterface {

    private String              scheme;
    private String              uri;
    private Set<UriInterface>   parents;

    public UnknownUri(String scheme, String uriString, UriInterface parent) {
        this.setScheme(scheme);
        this.setUri(uriString);
        this.parents = new HashSet<>();
        this.parents.add(parent);
    }

    @Override
    public String getScheme() {
        return this.scheme;
    }

    @Override
    public String getUri() {
        return this.uri;
    }

    @Override
    public Set<UriInterface> getParents() {
        return this.parents;
    }


    @Override
    public void setScheme(String scheme) {
        this.scheme = scheme;
    }

    @Override
    public void setUri(String uriString) {
        if (uriString == null) throw new IllegalArgumentException("Unknown URI is null");
        this.uri = uriString.trim();
    }

    @Override
    public void updateUri(UriInterface uriObject) {
        if (uriObject == null) throw new IllegalArgumentException("Empty URI provided for update");

        if (uriObject.getUri().equals(this.getUri())) {
            this.getParents().addAll(uriObject.getParents());
        } else throw new IllegalArgumentException("Incorrect URI provided for update");
    }

    @Override
    public void actionAfterUriAddedToBuffer(Crawler crawler) {
        System.out.println("New unknown URI has been added to the buffer: " + this.getUri());
    }


    @Override
    public String toString() {
        return this.getUri();
    }

    @Override
    public int compareTo(UriInterface urlLink) {
        return this.getUri().compareTo(urlLink.getUri());
    }
}
