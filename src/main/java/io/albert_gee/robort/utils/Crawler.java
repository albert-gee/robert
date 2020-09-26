package io.albert_gee.robort.utils;

import io.albert_gee.robort.interfaces.Buffer;

import java.util.*;

public class Crawler {

    private final Buffer buffer;

    public Crawler(Buffer buffer) {
        this.buffer = buffer;
    }

    public void handle(String... uris) {
        buffer.setHosts(uris);
        buffer.addAll(Arrays.asList(uris));

        while(buffer.hasNext()) {
            buffer.next();
        }
    }
}
