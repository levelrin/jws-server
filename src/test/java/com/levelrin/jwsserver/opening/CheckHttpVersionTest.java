/*
 * Copyright (c) 2020 Rin (https://www.levelrin.com)
 *
 * This file has been created under the terms of the MIT License.
 * See the details at https://github.com/levelrin/jws-server/blob/main/LICENSE
 */

package com.levelrin.jwsserver.opening;

import org.hamcrest.CoreMatchers;
import org.hamcrest.MatcherAssert;
import org.junit.jupiter.api.Test;
import java.util.Arrays;

/**
 * Tests.
 */
final class CheckHttpVersionTest {

    @Test
    public void shouldFailIfHttpVersionIsOlderThan1Dot1() {
        final OpeningResult result = new CheckHttpVersion(
            Arrays.asList(
                """
                GET /chat HTTP/1.0
                Host: server.example.com
                Upgrade: websocket
                Connection: Upgrade
                """.split("\n")
            ),
            () -> {
                throw new IllegalStateException(
                    "It should not run the encapsulated object in this test."
                );
            }
        ).handshake();
        MatcherAssert.assertThat(
            result.success(),
            CoreMatchers.equalTo(false)
        );
        MatcherAssert.assertThat(
            result.message(),
            CoreMatchers.equalTo(
                "HTTP version must be 1.1 or higher."
            )
        );
    }

    @Test
    public void shouldSucceedIfHttpVersionIs1Dot1OrLater() {
        final OpeningResult result = new CheckHttpVersion(
            Arrays.asList(
                """
                GET /chat HTTP/1.1
                Host: server.example.com
                Upgrade: websocket
                Connection: Upgrade
                """.split("\n")
            ),
            () -> new OpeningResult() {

                @Override
                public boolean success() {
                    return true;
                }

                @Override
                public String message() {
                    return "Success!";
                }

            }
        ).handshake();
        MatcherAssert.assertThat(
            result.success(),
            CoreMatchers.equalTo(true)
        );
        MatcherAssert.assertThat(
            result.message(),
            CoreMatchers.equalTo(
                "Success!"
            )
        );
    }

}
