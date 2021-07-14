/*
 * Copyright (c) 2020 Rin (https://www.levelrin.com)
 *
 * This file has been created under the terms of the MIT License.
 * See the details at https://github.com/levelrin/jws-server/blob/main/LICENSE
 */

package com.levelrin.jwsserver.binary;

/**
 * It represents the source of binary data such as {@link java.net.Socket}.
 */
public interface BinarySource {

    /**
     * It returns the next bits in String.
     * For example, let's say we have the following binary data: 101001000.
     * The next bits with a length of 5 would be 10100.
     * It will return an empty String if the source is unavailable.
     * For example, it will return an empty String if the socket is closed.
     * @param length The amount of bits. It should be greater than 0.
     * @return Next nth bits.
     */
    String nextBits(int length);

}
