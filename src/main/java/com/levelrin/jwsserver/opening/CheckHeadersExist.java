/*
 * Copyright (c) 2020 Rin (https://www.levelrin.com)
 *
 * This file has been created under the terms of the MIT License.
 * See the details at https://github.com/levelrin/jws-server/blob/main/LICENSE
 */

package com.levelrin.jwsserver.opening;

import java.util.Arrays;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

/**
 * A decorator to check if the request headers from the client have required headers.
 * The opening handshake will fail if the request is missing any header.
 */
public final class CheckHeadersExist implements Opening {

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
     * @param headers See {@link CheckHeadersExist#headers}.
     * @param origin See {@link CheckHeadersExist#origin}.
     */
    public CheckHeadersExist(final Map<String, String> headers, final Opening origin) {
        this.headers = headers;
        this.origin = origin;
    }

    @Override
    public OpeningResult handshake() {
        final String[] requiredHeaders = {
            "HOST", "UPGRADE", "CONNECTION", "SEC-WEBSOCKET-KEY", "SEC-WEBSOCKET-VERSION",
        };
        boolean notFound = false;
        final AtomicReference<String> missingHeader = new AtomicReference<>(
            "Programming mistakes. Please report this bug."
        );
        for (final String header : requiredHeaders) {
            if (!this.headers.containsKey(header)) {
                notFound = true;
                missingHeader.set(header);
                break;
            }
        }
        final OpeningResult result;
        if (notFound) {
            result = new Result(missingHeader, requiredHeaders);
        } else {
            result = this.origin.handshake();
        }
        return result;
    }

    /**
     * The result of failed opening handshake.
     */
    private static final class Result implements OpeningResult {

        /**
         * We will include this into the message.
         */
        private final AtomicReference<String> missingHeader;

        /**
         * Required headers.
         */
        private final String[] requiredHeaders;

        /**
         * Constructor.
         * @param missingHeader See {@link Result#missingHeader}.
         * @param requiredHeaders See {@link Result#requiredHeaders}.
         */
        Result(final AtomicReference<String> missingHeader, final String... requiredHeaders) {
            this.missingHeader = missingHeader;
            this.requiredHeaders = requiredHeaders.clone();
        }

        @Override
        public boolean success() {
            return false;
        }

        @Override
        public String message() {
            return String.format(
                """
                Missing header: %s
                The HTTP request for the WebSocket opening handshake must have the following keys:
                %s
                """,
                this.missingHeader.get(),
                Arrays.toString(this.requiredHeaders)
            );
        }

    }

}
