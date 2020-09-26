package io.albert_gee.robort.interfaces;

import java.util.List;

public interface Buffer {

    void add(String uri);

    void addAll(List<String> uris);

    String next();

    boolean hasNext();

    void setHosts(String[] hosts);

    boolean isInHosts(String uri);
}
