package io.zaaim.morphix.core;

import java.io.IOException;
import java.nio.file.Path;

public interface Indexer<T> {
    Index index(T type) throws IOException;
}
