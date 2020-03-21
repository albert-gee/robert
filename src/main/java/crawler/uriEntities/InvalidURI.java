package crawler.uriEntities;

import crawler.interfaces.URI;
import java.util.HashSet;
import java.util.Set;

/**
 * This class describes a URI with unknown or empty scheme or empty URI
 */
public class InvalidURI implements URI {

    private String URI;
    private Set<URI> parents;

    public InvalidURI(String uriString) {
        this.parents = new HashSet<>();
        this.setUri(uriString);
    }

    @Override
    public String getUri() {
        return this.URI;
    }

    @Override
    public Set<URI> getParents() {
        return this.parents;
    }

    @Override
    public void setUri(String uriString) {
        this.URI = uriString;
    }

    @Override
    public void updateUri(URI uriObject) {
        this.getParents().addAll(uriObject.getParents());
    }

    @Override
    public String toString() {
        return this.getUri();
    }

    @Override
    public int compareTo(URI urlLink) {
        return this.getUri().compareTo(urlLink.getUri());
    }

    @Override
    public void actionAfterUriAddedToBuffer() {}
}
