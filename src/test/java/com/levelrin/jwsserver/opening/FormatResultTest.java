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

/**
 * Tests.
 */
final class FormatResultTest {

    @Test
    public void shouldWrapFailedMessageAndFormHttpResponse() {
        final OpeningResult result = new FormatResult(
            () -> new OpeningResult() {

                @Override
                public boolean success() {
                    return false;
                }

                @Override
                public String message() {
                    return "Task failed successfully.";
                }

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
                HTTP/1.1 400 Bad Request
                Content-Type: application/json

                {
                   "about":"openingHandshake",
                   "success":false,
                   "message":"Task failed successfully."
                }
                """
            )
        );
    }

}
