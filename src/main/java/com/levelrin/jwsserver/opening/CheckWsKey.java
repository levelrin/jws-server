/*
 * Copyright (c) 2020 Rin (https://www.levelrin.com)
 *
 * This file has been created under the terms of the MIT License.
 * See the details at https://github.com/levelrin/jws-server/blob/main/LICENSE
 */

package com.levelrin.jwsserver.opening;

import java.util.Base64;
import java.util.Map;

/**
 * A decorator to check the WebSocket key.
 * It will decode the key with base64 decoder and check if the length is 16 bytes.
 * The opening handshake will fail if the length is not 16 bytes.
 */
public final class CheckWsKey implements Opening {

    /**
     * HTTP headers of the request from the client.
     */
    private final Map<String, String> headers;

    /**
     * We will decorate this.
     */
    private final Opening origin;

    /**
     * Constructor.
     * @param headers See {@link CheckWsKey#headers}.
     * @param origin See {@link CheckWsKey#origin}.
     */
    public CheckWsKey(final Map<String, String> headers, final Opening origin) {
        this.headers = headers;
        this.origin = origin;
    }

    @Override
    public OpeningResult handshake() {
        final byte[] decodedKey = Base64.getDecoder().decode(this.headers.get("SEC-WEBSOCKET-KEY"));
        final OpeningResult result;
        final int validLength = 16;
        if (decodedKey.length == validLength) {
            result = this.origin.handshake();
        } else {
            result = new OpeningResult() {

                @Override
                public boolean success() {
                    return false;
                }

                @Override
                public String message() {
                    return "The length of Sec-WebSocket-Key must be 16 bytes.";
                }

            };
        }
        return result;
    }

}
