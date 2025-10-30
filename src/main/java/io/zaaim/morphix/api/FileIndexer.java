package io.zaaim.morphix.api;

import io.zaaim.morphix.core.Index;
import io.zaaim.morphix.core.Indexer;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

public class FileIndexer implements Indexer<Path> {
    @Override
    public Index index(List<Path> files) throws IOException {
        return null;
    }
}
