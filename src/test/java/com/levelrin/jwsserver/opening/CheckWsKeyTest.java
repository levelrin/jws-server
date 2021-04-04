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
import java.util.HashMap;
import java.util.Map;

/**
 * Tests.
 */
final class CheckWsKeyTest {

    @Test
    public void shouldFailIfWsKeyIsNot16Bytes() {
        final Map<String, String> headers = new HashMap<>();
        headers.put("SEC-WEBSOCKET-KEY", "eW9pIHlvaQ==");
        final OpeningResult result = new CheckWsKey(
            headers,
            () -> {
                throw new IllegalStateException(
                    "It should not use encapsulated object in this test."
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
                "The length of Sec-WebSocket-Key must be 16 bytes."
            )
        );
    }

    @Test
    public void shouldSucceedIfWsKeyIs16Bytes() {
        final Map<String, String> headers = new HashMap<>();
        headers.put("SEC-WEBSOCKET-KEY", "dGhlIHNhbXBsZSBub25jZQ==");
        final OpeningResult result = new CheckWsKey(
            headers,
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
