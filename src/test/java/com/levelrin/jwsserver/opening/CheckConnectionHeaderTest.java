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
final class CheckConnectionHeaderTest {

    @Test
    public void shouldFailIfConnectionIsNotUpgrade() {
        final Map<String, String> headers = new HashMap<>();
        headers.put("CONNECTION", "something else");
        final OpeningResult result = new CheckConnectionHeader(
            headers,
            () -> {
                throw new IllegalStateException(
                    "It should not use the encapsulated object in this test."
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
                "The |Connection| header must contain 'upgrade'"
            )
        );
    }

    @Test
    public void shouldSucceedIfConnectionIsUpgrade() {
        final Map<String, String> headers = new HashMap<>();
        headers.put("CONNECTION", "UPGRADE");
        final OpeningResult result = new CheckConnectionHeader(
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
