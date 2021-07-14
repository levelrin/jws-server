/*
 * Copyright (c) 2020 Rin (https://www.levelrin.com)
 *
 * This file has been created under the terms of the MIT License.
 * See the details at https://github.com/levelrin/jws-server/blob/main/LICENSE
 */

package com.levelrin.jwsserver.binary;

/**
 * A decorator to validate {@link BinarySource#nextBits(int)}.
 * It will throw an exception if the length of the nextBits is less than 1.
 */
public final class CheckLength implements BinarySource {

    /**
     * We will use this if the length was valid.
     */
    private final BinarySource origin;

    /**
     * Constructor.
     * @param origin See {@link CheckLength#origin}.
     */
    public CheckLength(final BinarySource origin) {
        this.origin = origin;
    }

    /**
     * It will throw an exception is the length is not greater than 0.
     * @param length The amount of bits. It should be greater than 0.
     * @return Next bits from the {@link CheckLength#origin}.
     */
    @Override
    public String nextBits(final int length) {
        if (length < 1) {
            throw new IllegalArgumentException(
                String.format(
                    "The length must be greater than 0, but it was %d",
                    length
                )
            );
        }
        return this.origin.nextBits(length);
    }

}
