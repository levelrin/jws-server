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
 * A decorator to check the host header of HTTP request from the client.
 * The opening handshake will fail if the host does not match.
 */
public final class CheckHost implements Opening {

    /**
     * HTTP request headers from the client.
     */
    private final Map<String, String> headers;

    /**
     * The correct host we are looking for.
     * The host header must contain this.
     */
    private final String host;

    /**
     * We will use this if it passes the validation.
     */
    private final Opening origin;

    /**
     * Constructor.
     * @param headers See {@link CheckHost#headers}.
     * @param host See {@link CheckHost#host}.
     * @param origin See {@link CheckHost#origin}.
     */
    public CheckHost(final Map<String, String> headers, final String host, final Opening origin) {
        this.headers = headers;
        this.host = host;
        this.origin = origin;
    }

    @Override
    public OpeningResult handshake() {
        final OpeningResult result;
        final String headerHost = this.headers.get("HOST").toUpperCase(Locale.ROOT);
        if (headerHost.contains(this.host.toUpperCase(Locale.ROOT))) {
            result = this.origin.handshake();
        } else {
            result = new OpeningResult() {

                @Override
                public boolean success() {
                    return false;
                }

                @Override
                public String message() {
                    return "The |Host| header must contain the server's authority.";
                }

            };
        }
        return result;
    }

}
