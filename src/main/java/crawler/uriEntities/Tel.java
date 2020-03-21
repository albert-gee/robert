package crawler.uriEntities;

import crawler.interfaces.URI;

import java.util.HashSet;
import java.util.Set;

/**
 * This class describes a tel URI,
 * e.g. <a href="tel:555 55 55">...</a>
 */
public class Tel implements URI {

    private String uri;
    private Set<URI> parents;

    public Tel(String uriString) {
        this.setUri(uriString);
        this.parents = new HashSet<>();
    }

    @Override
    public String getUri() {
        return this.uri;
    }

    @Override
    public Set<URI> getParents() {
        return this.parents;
    }

    @Override
    public void setUri(String uriString) {
        if (uriString != null && uriString.trim().length() > 0) {
            this.uri = uriString.trim();
        } else {
            throw new IllegalArgumentException("Attempt to set empty URI");
        }
    }

    @Override
    public void updateUri(URI uriObject) {
        if (uriObject.getUri().equals(this.getUri())) {
            this.getParents().addAll(uriObject.getParents());
        }
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
