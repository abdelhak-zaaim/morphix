package io.zaaim.morphix.core;

import java.io.DataOutput;
import java.io.IOException;

public interface Writable {
    public void write(DataOutput dataOutput) throws IOException;
}
