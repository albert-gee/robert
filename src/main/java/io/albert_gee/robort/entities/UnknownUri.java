package io.albert_gee.robort.entities;

import io.albert_gee.robort.interfaces.Buffer;
import io.albert_gee.robort.interfaces.URI;

public class UnknownUri implements URI {
    private final String uri;
    private final String parent;

    public UnknownUri(String uri, String parent) {
        this.uri = uri;
        this.parent = parent;
    }

    /**
     * Get URI
     * @return URI
     */
    public String getUri() {
        return uri;
    }

    /**
     * Get host
     * @return URI
     */
    public String getParent() {
        return parent;
    }

    /**
     * URI scheme
     * @return URI scheme
     */
    public String getScheme() {
        return "unknown";
    }

    public void process(Buffer buffer) {
    }

    @Override
    public void update(String parent) {
    }
}
