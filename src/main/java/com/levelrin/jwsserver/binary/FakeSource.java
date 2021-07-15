/*
 * Copyright (c) 2020 Rin (https://www.levelrin.com)
 *
 * This file has been created under the terms of the MIT License.
 * See the details at https://github.com/levelrin/jws-server/blob/main/LICENSE
 */

package com.levelrin.jwsserver.binary;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * It's for simulating the incoming data from the socket in unit tests.
 */
public final class FakeSource implements BinarySource {

    /**
     * We can inject fake bits.
     */
    private final List<String> fakeBits;

    /**
     * We will use this to return the fake bits one after another.
     */
    private final AtomicInteger index = new AtomicInteger(0);

    /**
     * Secondary constructor.
     * @param fakeBits Effectively same as {@link FakeSource#fakeBits}.
     */
    public FakeSource(final String... fakeBits) {
        this(Arrays.asList(fakeBits.clone()));
    }

    /**
     * Primary constructor.
     * @param fakeBits See {@link FakeSource#fakeBits}.
     */
    public FakeSource(final List<String> fakeBits) {
        this.fakeBits = fakeBits;
    }

    @Override
    public String nextBits(final int length) {
        return this.fakeBits.get(this.index.getAndIncrement());
    }

}
