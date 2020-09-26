package io.albert_gee.robort.entities;

import io.albert_gee.robort.interfaces.Buffer;
import io.albert_gee.robort.interfaces.URI;

public class Tel implements URI {
    private final String uri;

    public Tel(String uri) {
        if (uri == null || uri.trim().isEmpty()) {
            throw new IllegalArgumentException("Tel URI must be not empty");
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
        return "tel";
    }

    public void process(Buffer buffer) {
    }

    @Override
    public void update() {

    }
}
