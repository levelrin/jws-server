/*
 * Copyright (c) 2020 Rin (https://www.levelrin.com)
 *
 * This file has been created under the terms of the MIT License.
 * See the details at https://github.com/levelrin/jws-server/blob/main/LICENSE
 */

package com.levelrin.jwsserver.opening;

import java.util.Map;
import java.util.stream.Collectors;

/**
 * It's responsible for constructing the HTTP response message for the succeeded opening handshake.
 * It assumes that all the validation has been finished and the opening handshake was successful.
 */
public final class FormatSuccess implements Opening {

    /**
     * We will add this headers into the response message.
     * Note, we will include the Upgrade and Connection headers.
     * In other words, you don't have to add those headers here.
     *
     * For example, let's say this map is empty.
     * The response message will be like this:
     *  HTTP/1.1 101 Switching Protocols
     * Upgrade: websocket
     * Connection: Upgrade
     */
    private final Map<String, String> headers;

    /**
     * Constructor.
     * @param headers See {@link FormatSuccess#headers}.
     */
    public FormatSuccess(final Map<String, String> headers) {
        this.headers = headers;
    }

    @Override
    public OpeningResult handshake() {
        return new Result(this.headers);
    }

    /**
     * Successful opening handshake result.
     */
    private static final class Result implements OpeningResult {

        /**
         * See {@link FormatSuccess#headers}.
         */
        private final Map<String, String> headers;

        /**
         * Constructor.
         * @param headers See {@link Result#headers}.
         */
        Result(final Map<String, String> headers) {
            this.headers = headers;
        }

        @Override
        public boolean success() {
            return true;
        }

        @Override
        public String message() {
            return String.format(
                """
                HTTP/1.1 101 Switching Protocols
                Upgrade: websocket
                Connection: Upgrade
                %s
                """,
                this.headers
                    .keySet()
                    .stream()
                    .map(key -> String.format("%s: %s", key, this.headers.get(key)))
                    .collect(Collectors.joining("\n"))
            );
        }

    }

}
