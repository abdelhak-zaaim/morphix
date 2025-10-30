package io.zaaim.morphix.core;

import java.util.Map;

public interface Vectorizer<T> {
    Map<String, Map<String, Float>> vectorize(T t);
}
