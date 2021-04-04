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
final class FormatSuccessTest {

    @Test
    public void headersShouldBeAdded() {
        final Map<String, String> headers = new HashMap<>();
        headers.put("Sec-WebSocket-Accept", "s3pPLMBiTxaQ9kYGzzhZRbK+xOo=");
        headers.put("Sec-WebSocket-Protocol", "chat");
        final OpeningResult result = new FormatSuccess(headers).handshake();
        MatcherAssert.assertThat(
            result.success(),
            CoreMatchers.equalTo(true)
        );
        MatcherAssert.assertThat(
            result.message(),
            CoreMatchers.equalTo(
                """
                HTTP/1.1 101 Switching Protocols
                Upgrade: websocket
                Connection: Upgrade
                Sec-WebSocket-Accept: s3pPLMBiTxaQ9kYGzzhZRbK+xOo=
                Sec-WebSocket-Protocol: chat
                """
            )
        );
    }

}
