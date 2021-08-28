/*
 * Copyright (c) 2020 Rin (https://www.levelrin.com)
 *
 * This file has been created under the terms of the MIT License.
 * See the details at https://github.com/levelrin/jws-server/blob/main/LICENSE
 */

package com.levelrin.jwsserver.io;

import org.hamcrest.CoreMatchers;
import org.hamcrest.MatcherAssert;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import java.io.IOException;

/**
 * Tests.
 */
final class CheckableOutputStreamTest {

    @Test
    @SuppressWarnings("MagicNumber")
    public void shouldBeAbleToCheckSentBytes() {
        try (CheckableOutputStream stream = new CheckableOutputStream()) {
            stream.write(
                new byte[] {
                    1, 2, 3,
                }
            );
            MatcherAssert.assertThat(
                stream.sentData(),
                CoreMatchers.equalTo(
                    new byte[] {
                        1, 2, 3,
                    }
                )
            );
        } catch (final IOException ioException) {
            throw new IllegalStateException(
                "This test should not raise an exception.",
                ioException
            );
        }
    }

    @Test
    public void shouldNotSupportWritingIntYet() {
        Assertions.assertThrows(
            UnsupportedOperationException.class,
            () -> new CheckableOutputStream().write(0)
        );
    }

}
