package io.albert_gee.robort.interfaces;

public interface URI {

    String getUri();

    String getScheme();

    void process(Buffer buffer);

    void update(String parent);
}
