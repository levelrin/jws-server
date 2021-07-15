/*
 * Copyright (c) 2020 Rin (https://www.levelrin.com)
 *
 * This file has been created under the terms of the MIT License.
 * See the details at https://github.com/levelrin/jws-server/blob/main/LICENSE
 */

package com.levelrin.jwsserver.binary;

import org.hamcrest.CoreMatchers;
import org.hamcrest.MatcherAssert;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Tests.
 */
final class SocketBinaryTest {

    @Test
    public void shouldReadFromSocketIfCacheDoesNotHaveEnoughData() throws IOException {
        // 33 is the same as 00100001 in binary.
        final int firstData = 33;
        // 44 is the same as 00101100 in binary.
        final int secondData = 44;
        try (
            FakeInputStream inputStream = new FakeInputStream(
                Arrays.asList(firstData, secondData)
            )
        ) {
            try (Socket socket = Mockito.mock(Socket.class)) {
                Mockito.doReturn(inputStream).when(socket).getInputStream();
                // 9 bits are 1 bit over of one byte
                // so it should read the data from the socket twice.
                final int nextBits = 9;
                MatcherAssert.assertThat(
                    new SocketBinary(
                        socket,
                        ex -> Assertions.fail(ex.getMessage())
                    ).nextBits(nextBits),
                    CoreMatchers.equalTo("001000010")
                );
            }
            final int expectedCount = 2;
            MatcherAssert.assertThat(
                inputStream.takenCount(),
                CoreMatchers.equalTo(expectedCount)
            );
        }
    }

    @Test
    public void shouldNotReadSocketIfCacheHasEnoughData() throws IOException {
        // 55 is the same as 00110111 in binary.
        final int firstData = 55;
        try (
            FakeInputStream inputStream = new FakeInputStream(
                Collections.singletonList(firstData)
            )
        ) {
            final BinarySource source;
            try (Socket socket = Mockito.mock(Socket.class)) {
                Mockito.doReturn(inputStream).when(socket).getInputStream();
                source = new SocketBinary(
                    socket,
                    ex -> Assertions.fail(ex.getMessage())
                );
            }
            // We will read 2 bits. The cache should have 6 bits after reading.
            final int firstRead = 2;
            MatcherAssert.assertThat(
                source.nextBits(firstRead),
                CoreMatchers.equalTo("00")
            );
            // We will read 3 bits. The cache should have 3 bits after reading.
            final int secondRead = 3;
            MatcherAssert.assertThat(
                source.nextBits(secondRead),
                CoreMatchers.equalTo("110")
            );
            final int expectedCount = 1;
            MatcherAssert.assertThat(
                inputStream.takenCount(),
                CoreMatchers.equalTo(expectedCount)
            );
        }
    }

    /**
     * We will use this to simulate the client's data.
     */
    private static final class FakeInputStream extends InputStream {

        /**
         * It represents the data from the client.
         * We will return an item one by one when {@link FakeInputStream#read()} is called.
         */
        private final List<Integer> data;

        /**
         * It represents the number of items taken from the socket.
         */
        private final AtomicInteger count = new AtomicInteger(0);

        /**
         * Constructor.
         * @param data See {@link FakeInputStream#data}.
         */
        FakeInputStream(final List<Integer> data) {
            super();
            this.data = data;
        }

        @Override
        public int read() {
            return this.data.get(this.count.getAndIncrement());
        }

        /**
         * We can use this to check how many times we read data from the socket.
         * @return See {@link FakeInputStream#count}.
         */
        public int takenCount() {
            return this.count.get();
        }

    }

}
