package io.albert_gee.robort.entities;

import io.albert_gee.robort.interfaces.Buffer;
import io.albert_gee.robort.interfaces.URI;

public class UnknownUri implements URI {
    private final String uri;

    public UnknownUri(String uri) {
        this.uri = uri;
    }

    public String getUri() {
        return uri;
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
    public void update() {

    }
}
