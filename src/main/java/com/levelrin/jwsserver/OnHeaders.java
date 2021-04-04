/*
 * Copyright (c) 2020 Rin (https://www.levelrin.com)
 *
 * This file has been created under the terms of the MIT License.
 * See the details at https://github.com/levelrin/jws-server/blob/main/LICENSE
 */

package com.levelrin.jwsserver;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.function.Function;

/**
 * It's responsible for parsing the HTTP headers from the request lines.
 * The keys of the headers will be all capitalized because they are case-insensitive.
 */
public final class OnHeaders implements WsServer {

    /**
     * HTTP request lines from the client.
     */
    private final List<String> lines;

    /**
     * You can do something with the HTTP headers using this.
     * The map represents the HTTP headers.
     * The keys of the headers will be all capitalized because they are case-insensitive.
     */
    private final Function<Map<String, String>, WsServer> withHeaders;

    /**
     * Constructor.
     * @param lines See {@link OnHeaders#lines}.
     * @param withHeaders See {@link OnHeaders#withHeaders}.
     */
    public OnHeaders(final List<String> lines, final Function<Map<String, String>, WsServer> withHeaders) {
        this.lines = lines;
        this.withHeaders = withHeaders;
    }

    @Override
    public void start() {
        final Map<String, String> headers = new HashMap<>();
        for (final String line : this.lines.subList(1, this.lines.size())) {
            if (line.isEmpty()) {
                break;
            }
            final String[] pair = line.split(":", 2);
            headers.put(
                pair[0].trim().toUpperCase(Locale.ROOT),
                pair[1].trim()
            );
        }
        this.withHeaders.apply(headers).start();
    }

}
