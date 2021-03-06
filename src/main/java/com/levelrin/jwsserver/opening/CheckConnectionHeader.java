/*
 * Copyright (c) 2020 Rin (https://www.levelrin.com)
 *
 * This file has been created under the terms of the MIT License.
 * See the details at https://github.com/levelrin/jws-server/blob/main/LICENSE
 */

package com.levelrin.jwsserver.opening;

import java.util.Locale;
import java.util.Map;

/**
 * A decorator to check the connection header of the HTTP request from the client.
 * The value of connection header must be upgrade.
 * Otherwise, the opening handshake will fail.
 */
public final class CheckConnectionHeader implements Opening {

    /**
     * HTTP request headers from the client.
     */
    private final Map<String, String> headers;

    /**
     * We will use this if it passes the validation.
     */
    private final Opening origin;

    /**
     * Constructor.
     * @param headers See {@link CheckConnectionHeader#headers}.
     * @param origin See {@link CheckConnectionHeader#origin}.
     */
    public CheckConnectionHeader(final Map<String, String> headers, final Opening origin) {
        this.headers = headers;
        this.origin = origin;
    }

    @Override
    public OpeningResult handshake() {
        final OpeningResult result;
        if (this.headers.get("CONNECTION").toUpperCase(Locale.ROOT).contains("UPGRADE")) {
            result = this.origin.handshake();
        } else {
            result = new OpeningResult() {

                @Override
                public boolean success() {
                    return false;
                }

                @Override
                public String message() {
                    return "The |Connection| header must contain 'upgrade'";
                }

            };
        }
        return result;
    }

}
