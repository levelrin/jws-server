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
import org.mockito.Mockito;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

/**
 * Tests.
 */
final class OnSecWsAcceptTest {

    @Test
    public void shouldGenerateSecWebSocketAcceptUsingKey() {
        final Map<String, String> requestHeaders = new HashMap<>();
        requestHeaders.put("SEC-WEBSOCKET-KEY", "dGhlIHNhbXBsZSBub25jZQ==");
        final AtomicReference<String> value = new AtomicReference<>();
        new OnSecWsAccept(
            requestHeaders,
            header -> {
                value.set(header.get("Sec-WebSocket-Accept"));
                return Mockito.mock(Opening.class);
            }
        ).handshake();
        MatcherAssert.assertThat(
            value.get(),
            CoreMatchers.equalTo("s3pPLMBiTxaQ9kYGzzhZRbK+xOo=")
        );
    }

}
