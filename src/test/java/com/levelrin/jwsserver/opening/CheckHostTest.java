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
final class CheckHostTest {

    @Test
    public void shouldFailIfHostDoesNotMatch() {
        final Map<String, String> headers = new HashMap<>();
        headers.put("HOST", "levelrin.com");
        final OpeningResult result = new CheckHost(
            headers,
            "localhost",
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
                "The |Host| header must contain the server's authority."
            )
        );
    }

    @Test
    public void shouldSuccessIfHostMatches() {
        final Map<String, String> headers = new HashMap<>();
        headers.put("HOST", "LOCALHOST");
        final OpeningResult result = new CheckHost(
            headers,
            "localhost",
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
