package io.albert_gee.robort.interfaces;

import java.lang.reflect.InvocationTargetException;
import java.util.Map;

public interface UriFactory {

    void addRule(String scheme, Class<?> ruleClass);

    Map<String, Class<?>> getRules();

    URI create(String uri) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException;
}
