/*
 * Copyright (c) 2020 Rin (https://www.levelrin.com)
 *
 * This file has been created under the terms of the MIT License.
 * See the details at https://github.com/levelrin/jws-server/blob/main/LICENSE
 */

package com.levelrin.jwsserver.opening;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

/**
 * It's responsible for generating the value of Sec-WebSocket-Accept header.
 * It will create a map, which represents the HTTP response headers for {@link FormatSuccess}.
 */
public final class OnSecWsAccept implements Opening {

    /**
     * The request headers from the client.
     * We will use the SEC-WEBSOCKET-KEY to generate the Sec-WebSocket-Accept value.
     */
    private final Map<String, String> requestHeaders;

    /**
     * We will use this to construct the HTTP response message.
     */
    private final Function<Map<String, String>, Opening> onHeader;

    /**
     * Constructor.
     * @param requestHeaders See {@link OnSecWsAccept#requestHeaders}.
     * @param onHeader See {@link OnSecWsAccept#onHeader}.
     */
    public OnSecWsAccept(final Map<String, String> requestHeaders, final Function<Map<String, String>, Opening> onHeader) {
        this.requestHeaders = requestHeaders;
        this.onHeader = onHeader;
    }

    @Override
    public OpeningResult handshake() {
        final Map<String, String> header = new HashMap<>();
        try {
            header.put(
                "Sec-WebSocket-Accept",
                Base64.getEncoder().encodeToString(
                    MessageDigest.getInstance("SHA-1").digest(
                        String.format(
                            "%s258EAFA5-E914-47DA-95CA-C5AB0DC85B11",
                            this.requestHeaders.get("SEC-WEBSOCKET-KEY")
                        ).getBytes(StandardCharsets.UTF_8)
                    )
                )
            );
        } catch (final NoSuchAlgorithmException ex) {
            throw new IllegalStateException("Failed to generate the Sec-WebSocket-Accept.", ex);
        }
        return this.onHeader.apply(header).handshake();
    }

}
