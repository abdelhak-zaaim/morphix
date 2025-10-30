package io.zaaim.morphix.core;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

public interface Indexer<T> {
    Index index(List<T> type) throws IOException;
}
