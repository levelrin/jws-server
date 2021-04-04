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
final class CheckWsVersionTest {

    @Test
    public void shouldFailIfWsVersionIsNot13() {
        final Map<String, String> headers = new HashMap<>();
        headers.put("SEC-WEBSOCKET-VERSION", "8");
        final OpeningResult result = new CheckWsVersion(
            headers,
            () -> new OpeningResult() {

                @Override
                public boolean success() {
                    throw new IllegalStateException(
                        "It should not call the encapsulated object in this case."
                    );
                }

                @Override
                public String message() {
                    throw new IllegalStateException(
                        "It should not call the encapsulated object in this case."
                    );
                }

            }
        ).handshake();
        MatcherAssert.assertThat(
            result.success(),
            CoreMatchers.equalTo(false)
        );
        MatcherAssert.assertThat(
            result.message(),
            CoreMatchers.equalTo("The Sec-WebSocket-Version must be 13.")
        );
    }

    @Test
    public void shouldUseEncapsulatedObjectIfWsVersionIs13() {
        final Map<String, String> headers = new HashMap<>();
        headers.put("SEC-WEBSOCKET-VERSION", "13");
        final OpeningResult result = new CheckWsVersion(
            headers,
            () -> new OpeningResult() {

                @Override
                public boolean success() {
                    return true;
                }

                @Override
                public String message() {
                    return "Yoi Yoi";
                }

            }
        ).handshake();
        MatcherAssert.assertThat(
            result.success(),
            CoreMatchers.equalTo(true)
        );
        MatcherAssert.assertThat(
            result.message(),
            CoreMatchers.equalTo("Yoi Yoi")
        );
    }

}
