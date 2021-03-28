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
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Tests.
 */
final class CheckEndpointTest {

    @Test
    public void shouldUseOriginIfEndpointIsValid() {
        final AtomicBoolean used = new AtomicBoolean(false);
        final List<String> requestLines = new ArrayList<>();
        requestLines.add("GET /chat HTTP/1.1");
        requestLines.add("Host: server.example.com");
        requestLines.add("Upgrade: websocket");
        requestLines.add("Connection: Upgrade");
        requestLines.add("Sec-WebSocket-Key: dGhlIHNhbXBsZSBub25jZQ==");
        requestLines.add("Origin: http://example.com");
        requestLines.add("Sec-WebSocket-Protocol: chat, superchat");
        requestLines.add("Sec-WebSocket-Version: 13");
        final List<String> endpoints = new ArrayList<>();
        endpoints.add("/");
        endpoints.add("/chat");
        new CheckEndpoint(
            requestLines,
            endpoints,
            () -> {
                used.set(true);
                return Mockito.mock(OpeningResult.class);
            }
        ).handshake();
        MatcherAssert.assertThat(
            used.get(),
            CoreMatchers.equalTo(true)
        );
    }

    @Test
    public void shouldFailIfEndpointIsNotValid() {
        final List<String> requestLines = new ArrayList<>();
        requestLines.add("GET /chat HTTP/1.1");
        requestLines.add("Host: server.example.com");
        requestLines.add("Upgrade: websocket");
        requestLines.add("Connection: Upgrade");
        requestLines.add("Sec-WebSocket-Key: dGhlIHNhbXBsZSBub25jZQ==");
        requestLines.add("Origin: http://example.com");
        requestLines.add("Sec-WebSocket-Protocol: chat, superchat");
        requestLines.add("Sec-WebSocket-Version: 13");
        final List<String> endpoints = new ArrayList<>();
        endpoints.add("/");
        endpoints.add("/chat2");
        final OpeningResult result = new CheckEndpoint(
            requestLines,
            endpoints,
            () -> {
                throw new IllegalStateException("Should not call the origin in this test.");
            }
        ).handshake();
        MatcherAssert.assertThat(
            result.success(),
            CoreMatchers.equalTo(false)
        );
        MatcherAssert.assertThat(
            result.message(),
            CoreMatchers.equalTo("The request endpoint '/chat' is unknown.")
        );
    }

}
