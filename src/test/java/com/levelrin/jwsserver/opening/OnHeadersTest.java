/*
 * Copyright (c) 2020 Rin (https://www.levelrin.com)
 *
 * This file has been created under the terms of the MIT License.
 * See the details at https://github.com/levelrin/jws-server/blob/main/LICENSE
 */

package com.levelrin.jwsserver.opening;

import org.hamcrest.CoreMatchers;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Tests.
 */
final class OnHeadersTest {

    @Test
    public void shouldRunFunctionWithCorrectHeaders() {
        final List<String> lines = new ArrayList<>();
        lines.add("GET /path/to/websocket/endpoint HTTP/1.1");
        lines.add("Host: localhost");
        lines.add("Upgrade: websocket");
        lines.add("Connection: Upgrade");
        lines.add("Sec-WebSocket-Key: xqBt3ImNzJbYqRINxEFlkg==");
        lines.add("Origin: http://localhost");
        lines.add("Sec-WebSocket-Version: 13");
        final Map<String, String> actual = new HashMap<>();
        new OnHeaders(
            lines,
            headers -> {
                actual.putAll(headers);
                return Mockito.mock(Opening.class);
            }
        ).handshake();
        MatcherAssert.assertThat(
            actual,
            CoreMatchers.allOf(
                Matchers.hasEntry("HOST", "LOCALHOST"),
                Matchers.hasEntry("UPGRADE", "WEBSOCKET"),
                Matchers.hasEntry("CONNECTION", "UPGRADE"),
                Matchers.hasEntry("SEC-WEBSOCKET-KEY", "XQBT3IMNZJBYQRINXEFLKG=="),
                Matchers.hasEntry("ORIGIN", "HTTP://LOCALHOST"),
                Matchers.hasEntry("SEC-WEBSOCKET-VERSION", "13")
            )
        );
    }

}
