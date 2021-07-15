/*
 * Copyright (c) 2020 Rin (https://www.levelrin.com)
 *
 * This file has been created under the terms of the MIT License.
 * See the details at https://github.com/levelrin/jws-server/blob/main/LICENSE
 */

package com.levelrin.jwsserver.binary;

import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

/**
 * It's responsible for getting binary data from the socket.
 * Since the socket provides data in bytes, it may cache the leftover data.
 */
public final class SocketBinary implements BinarySource {

    /**
     * We will get the binary data from this.
     */
    private final Socket socket;

    /**
     * It's for handling the {@link IOException} from the socket.
     */
    private final Consumer<IOException> onError;

    /**
     * We will store the leftover binary data from the socket.
     * For example, let's say the user asks the next 1 bit.
     * We will get a byte (8 bit) from the socket.
     * Let's say the value is this: 01001000.
     * Since the first bit is 0, we will give 0 to the user.
     * Now, we have the following leftover data: 1001000.
     * In that case, we will store those data like this: [1][0][0][1][0][0][0].
     * For the subsequent request, we will give the leftover data
     * before reading the data from the socket.
     */
    private final List<Byte> cache = new ArrayList<>();

    /**
     * Constructor.
     * @param socket See {@link SocketBinary#socket}.
     * @param onError See {@link SocketBinary#onError}.
     */
    public SocketBinary(final Socket socket, final Consumer<IOException> onError) {
        this.socket = socket;
        this.onError = onError;
    }

    @Override
    public String nextBits(final int length) {
        byte[] bits = new byte[length];
        if (length <= this.cache.size()) {
            // when there are enough bits in the cache
            bits = this.allFromCache(length);
        } else {
            // when the cache is short or empty
            if (this.socket.isClosed()) {
                // source is not available
                bits = new byte[0];
            } else {
                // take all the bits from the cache
                // and calculate the remaining bits that we need to obtain from the socket
                for (int index = 0; index < this.cache.size(); index = index + 1) {
                    bits[index] = this.cache.get(index);
                }
                final int remainingBits = length - this.cache.size();
                this.cache.clear();
                // read the bytes from the socket
                final int[] bytes = this.readNBytes(remainingBits);
                // bytes from the socket can be empty if exception occurs
                if (bytes.length > 0) {
                    // put everything into cache first
                    for (final int oneByte : bytes) {
                        final String binary = String.format(
                            "%8s",
                            Integer.toBinaryString(oneByte)
                        ).replace(' ', '0');
                        final int eightBits = 8;
                        for (int index = 0; index < eightBits; index = index + 1) {
                            this.cache.add(
                                Byte.parseByte(
                                    String.valueOf(
                                        binary.charAt(index)
                                    )
                                )
                            );
                        }
                    }
                    // take remaining bits out of cache
                    for (int index = 0; index < remainingBits; index = index + 1) {
                        bits[index] = this.cache.get(0);
                        this.cache.remove(0);
                    }
                }
            }
        }
        return this.bitsToString(bits);
    }

    /**
     * Take bits from the {@link SocketBinary#cache}.
     * We will use this when there are enough bits in the cache.
     * @param length The amount of bits we need to take from the cache.
     * @return Bits from the cache.
     */
    private byte[] allFromCache(final int length) {
        final byte[] bits = new byte[length];
        for (int index = 0; index < length; index = index + 1) {
            bits[index] = this.cache.get(0);
            this.cache.remove(0);
        }
        return bits;
    }

    /**
     * Read bytes from the socket.
     * It may return empty byte array if exception occurs.
     * We will use this after taking all the bits from the cache and still need more bits.
     * @param remainingBits The amount of bits that we still need
     *                      after taking all the bits from the cache.
     * @return Bytes from the socket.
     */
    private int[] readNBytes(final int remainingBits) {
        final int eightBits = 8;
        final int bytesAmount = (int) Math.ceil((double) remainingBits / eightBits);
        int[] bytes = new int[bytesAmount];
        try {
            for (int index = 0; index < bytesAmount; index = index + 1) {
                // We've tried using this.socket.getInputStream().readNBytes(int len);
                // It turned out to be wrong because the number was changed
                // when the type int was casted into byte.
                // For example, the value from the first read was supposed to be 129.
                // However, we got -127 when we used readNBytes(int len);
                final int next = this.socket.getInputStream().read();
                if (next == -1) {
                    bytes = new int[0];
                    break;
                } else {
                    bytes[index] = next;
                }
            }
        } catch (final IOException ex) {
            bytes = new int[0];
            this.onError.accept(ex);
        }
        return bytes;
    }

    /**
     * Convert the byte array into String.
     * For example, let's say we have a byte array like this: [0][1][1].
     * It will return the String 011.
     * @param bits The byte array.
     * @return Binary number in String.
     */
    private String bitsToString(final byte[] bits) {
        String result = "";
        for (final byte bit : bits.clone()) {
            result = String.format("%s%d", result, bit);
        }
        return result;
    }

}
