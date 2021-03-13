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
final class CheckHeadersExistTest {

    @Test
    public void shouldFailIfRequestMissesAnyRequiredHeader() {
        final Map<String, String> headers = new HashMap<>();
        final OpeningResult result = new CheckHeadersExist(
            headers,
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
                """
                Missing header: HOST
                The HTTP request for the WebSocket opening handshake must have the following keys:
                [HOST, UPGRADE, CONNECTION, SEC-WEBSOCKET-KEY, SEC-WEBSOCKET-VERSION]
                """
            )
        );
    }

    @Test
    public void shouldSuccessIfRequestHasAllRequiredHeaders() {
        final Map<String, String> headers = new HashMap<>();
        headers.put("HOST", "one");
        headers.put("UPGRADE", "two");
        headers.put("CONNECTION", "three");
        headers.put("SEC-WEBSOCKET-KEY", "four");
        headers.put("SEC-WEBSOCKET-VERSION", "five");
        final OpeningResult result = new CheckHeadersExist(
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
