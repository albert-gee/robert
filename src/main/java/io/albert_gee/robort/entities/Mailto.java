package io.albert_gee.robort.entities;

import io.albert_gee.robort.interfaces.Buffer;
import io.albert_gee.robort.interfaces.URI;

public class Mailto implements URI {
    private final String uri;

    public Mailto(String uri) {
        if (uri == null || uri.trim().isEmpty()) {
            throw new IllegalArgumentException("Mailto URI must be not empty");
        } else {
            this.uri = uri;
        }
    }

    public String getUri() {
        return uri;
    }

    /**
     * URI scheme
     * @return URI scheme
     */
    public String getScheme() {
        return "mailto";
    }

    public void process(Buffer buffer) {
    }

    @Override
    public void update() {

    }
}
