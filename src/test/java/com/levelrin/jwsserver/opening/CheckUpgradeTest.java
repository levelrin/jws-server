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
final class CheckUpgradeTest {

    @Test
    public void shouldFailIfUpgradeIsNotWebSocket() {
        final Map<String, String> headers = new HashMap<>();
        headers.put("UPGRADE", "Yoi");
        final OpeningResult result = new CheckUpgrade(
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
            CoreMatchers.equalTo("The |Upgrade| header must be 'websocket'")
        );
    }

    @Test
    public void shouldSucceedIfUpgradeIsWebSocket() {
        final Map<String, String> headers = new HashMap<>();
        headers.put("UPGRADE", "WEBSOCKET");
        final OpeningResult result = new CheckUpgrade(
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
            CoreMatchers.equalTo("Success!")
        );
    }

}
