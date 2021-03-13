/*
 * Copyright (c) 2020 Rin (https://www.levelrin.com)
 *
 * This file has been created under the terms of the MIT License.
 * See the details at https://github.com/levelrin/jws-server/blob/main/LICENSE
 */

package com.levelrin.jwsserver;

import com.levelrin.jwsserver.opening.Opening;
import com.levelrin.jwsserver.opening.OpeningResult;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * It's responsible for reading the client's message, doing the opening handshake, and responding with the handshake result.
 *
 * It will delegate the actual handshake process to other objects.
 * To delegate, other objects will need the request message.
 * It will break the request message into lines for convenience.
 * You can pass a function that takes the lines of the request message to generate an object responsible for handling the opening handshake.
 * That is why we named this class like this.
 *
 * It would close the socket if the opening handshake failed.
 */
public final class WithRequestLines implements WsServer {

    /**
     * It's for reading the client's message and writing a reply.
     */
    private final Socket socket;

    /**
     * A function handle any error from {@link WithRequestLines#socket}.
     */
    private final Consumer<IOException> onError;

    /**
     * A function that takes the lines of the request message
     * to generate an object responsible for handling the opening handshake.
     */
    private final Function<List<String>, Opening> onMessages;

    /**
     * Constructor.
     * @param socket See {@link WithRequestLines#socket}.
     * @param onError See {@link WithRequestLines#onError}.
     * @param onMessages See {@link WithRequestLines#onMessages}.
     */
    public WithRequestLines(final Socket socket, final Consumer<IOException> onError, final Function<List<String>, Opening> onMessages) {
        this.socket = socket;
        this.onError = onError;
        this.onMessages = onMessages;
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
            final PrintWriter writer = new PrintWriter(
                this.socket.getOutputStream(),
                false,
                StandardCharsets.UTF_8
            );
            final List<String> lines = new ArrayList<>();
            String line = reader.readLine();
            while (line != null) {
                lines.add(line);
                line = reader.readLine();
            }
            final OpeningResult openingResult = this.onMessages.apply(lines).handshake();
            writer.print(openingResult.message());
            writer.flush();
            if (!openingResult.success()) {
                this.socket.close();
            }
        } catch (final IOException ex) {
            this.onError.accept(ex);
        }
    }

}
