package io.albert_gee.robort.utils;

import io.albert_gee.robort.helpers.URIHelper;
import io.albert_gee.robort.interfaces.URI;
import io.albert_gee.robort.interfaces.UriFactory;
import lombok.Getter;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

/**
 * This class describes how to create a URI instance according to provided rules
 */
public class SimpleUriFactory implements UriFactory {

    @Getter
    private final Map<String, Class<?>> rules; // Map key is scheme and value is rule class implementing UriInterface

    public SimpleUriFactory() {
        rules = new HashMap<>();
    }

    @Override
    public void addRule(String scheme, Class<?> ruleClass) {
        rules.put(scheme, ruleClass);
    }

    /**
     * Factory method that creates a URI object of provided URI string
     * @param uri - URI string
     * @return URI object
     */
    public URI create(String uri, String parent) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        String scheme = URIHelper.parseScheme(uri);
        Class<?> schemeClass = rules.get(scheme);

        return (schemeClass != null) ? (URI) schemeClass.getConstructor(String.class, String.class).newInstance(uri, parent) : null;
    }
}
