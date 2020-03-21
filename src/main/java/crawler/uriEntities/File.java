package crawler.uriEntities;

import crawler.interfaces.URI;

import java.util.HashSet;
import java.util.Set;


/**
 * This class describes a file URI
 */
public class File implements URI {
    private String URI;
    private Set<URI> parents;

    public File(String uriString) {
        this.setUri(uriString);
        this.parents = new HashSet<>();
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
        this.URI = URI;
    }

    @Override
    public void updateUri(URI uriObject) {
        this.getParents().addAll(uriObject.getParents());
    }

    @Override
    public void actionAfterUriAddedToBuffer() {}

    @Override
    public String toString() {
        return this.getUri();
    }

    @Override
    public int compareTo(URI urlLink) {
        return this.getUri().compareTo(urlLink.getUri());
    }
}
