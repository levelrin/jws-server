/*
 * Copyright (c) 2020 Rin (https://www.levelrin.com)
 *
 * This file has been created under the terms of the MIT License.
 * See the details at https://github.com/levelrin/jws-server/blob/main/LICENSE
 */

package com.levelrin.jwsserver.io;

import org.jetbrains.annotations.NotNull;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * It's for testing.
 * We can use this to check what data was sent to the output stream.
 */
public final class CheckableOutputStream extends OutputStream {

    /**
     * We will store the data sent to the output stream.
     */
    private final List<byte[]> dataCache = new ArrayList<>(1);

    @Override
    public void write(final int data) {
        throw new UnsupportedOperationException("Unexpected call.");
    }

    @Override
    public void write(final byte @NotNull [] data) {
        this.dataCache.add(data);
    }

    @Override
    public void close() throws IOException {
        // Do nothing.
    }

    /**
     * We can use this to check the data sent to the output stream.
     * @return Data sent in bytes.
     */
    public byte[] sentData() {
        return this.dataCache.get(0);
    }

}
