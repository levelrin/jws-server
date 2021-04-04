/*
 * Copyright (c) 2020 Rin (https://www.levelrin.com)
 *
 * This file has been created under the terms of the MIT License.
 * See the details at https://github.com/levelrin/jws-server/blob/main/LICENSE
 */

package com.levelrin.jwsserver;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * It's responsible for reading the client's message.
 * You can pass a function to something with the message.
 */
public final class OnRequestLines implements WsServer {

    /**
     * It's for reading the client's message.
     */
    private final Socket socket;

    /**
     * A function that handles {@link IOException} from {@link OnRequestLines#socket}.
     */
    private final Consumer<IOException> onError;

    /**
     * You can do something with the lines of client's message using this function.
     */
    private final Function<List<String>, WsServer> withLines;

    /**
     * Constructor.
     * @param socket See {@link OnRequestLines#socket}.
     * @param onError See {@link OnRequestLines#onError}.
     * @param withLines See {@link OnRequestLines#withLines}.
     */
    public OnRequestLines(final Socket socket, final Consumer<IOException> onError, final Function<List<String>, WsServer> withLines) {
        this.socket = socket;
        this.onError = onError;
        this.withLines = withLines;
    }

    @Override
    @SuppressWarnings("PMD.CloseResource")
    public void start() {
        try {
            final BufferedReader reader = new BufferedReader(
                new InputStreamReader(
                    this.socket.getInputStream(),
                    StandardCharsets.UTF_8
                )
            );
            final List<String> lines = new ArrayList<>();
            String line = reader.readLine();
            while (line != null && !line.isEmpty()) {
                lines.add(line);
                line = reader.readLine();
            }
            this.withLines.apply(lines).start();
        } catch (final IOException ex) {
            this.onError.accept(ex);
        }
    }

}
