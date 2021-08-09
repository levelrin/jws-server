/*
 * Copyright (c) 2020 Rin (https://www.levelrin.com)
 *
 * This file has been created under the terms of the MIT License.
 * See the details at https://github.com/levelrin/jws-server/blob/main/LICENSE
 */

package com.levelrin.jwsserver.guide;

import com.levelrin.jwsserver.CloseSocketWhenDone;
import com.levelrin.jwsserver.InformOpeningFailure;
import com.levelrin.jwsserver.InformOpeningSuccess;
import com.levelrin.jwsserver.OnConnection;
import com.levelrin.jwsserver.OnHeaders;
import com.levelrin.jwsserver.OnOpeningResult;
import com.levelrin.jwsserver.OnReaction;
import com.levelrin.jwsserver.OnRequestLines;
import com.levelrin.jwsserver.OnWriter;
import com.levelrin.jwsserver.StartCommunication;
import com.levelrin.jwsserver.WithFraming;
import com.levelrin.jwsserver.WithLoop;
import com.levelrin.jwsserver.WithThread;
import com.levelrin.jwsserver.WsServer;
import com.levelrin.jwsserver.binary.BinarySource;
import com.levelrin.jwsserver.binary.CheckLength;
import com.levelrin.jwsserver.binary.SocketBinary;
import com.levelrin.jwsserver.frame.Defragmentation;
import com.levelrin.jwsserver.frame.ControlSection;
import com.levelrin.jwsserver.frame.Fin;
import com.levelrin.jwsserver.frame.Mask;
import com.levelrin.jwsserver.frame.MaskingKey;
import com.levelrin.jwsserver.frame.OnSaveOpcode;
import com.levelrin.jwsserver.frame.OnSavePayload;
import com.levelrin.jwsserver.frame.Opcode;
import com.levelrin.jwsserver.frame.PayloadData;
import com.levelrin.jwsserver.frame.PayloadLength;
import com.levelrin.jwsserver.frame.Rsv1;
import com.levelrin.jwsserver.frame.Rsv2;
import com.levelrin.jwsserver.frame.Rsv3;
import com.levelrin.jwsserver.frame.UnmaskedData;
import com.levelrin.jwsserver.frame.control.BinaryControl;
import com.levelrin.jwsserver.frame.control.CloseControl;
import com.levelrin.jwsserver.frame.control.ContinuationControl;
import com.levelrin.jwsserver.frame.control.PingControl;
import com.levelrin.jwsserver.frame.control.PongControl;
import com.levelrin.jwsserver.frame.control.ReservedControl;
import com.levelrin.jwsserver.frame.control.ReservedNonControl;
import com.levelrin.jwsserver.frame.control.TextControl;
import com.levelrin.jwsserver.opening.CheckConnectionHeader;
import com.levelrin.jwsserver.opening.CheckEndpoint;
import com.levelrin.jwsserver.opening.CheckHeadersExist;
import com.levelrin.jwsserver.opening.CheckHost;
import com.levelrin.jwsserver.opening.CheckHttpVersion;
import com.levelrin.jwsserver.opening.CheckUpgrade;
import com.levelrin.jwsserver.opening.CheckWsKey;
import com.levelrin.jwsserver.opening.CheckWsVersion;
import com.levelrin.jwsserver.opening.FormatFailure;
import com.levelrin.jwsserver.opening.FormatSuccess;
import com.levelrin.jwsserver.opening.OnSecWsAccept;
import com.levelrin.jwsserver.reaction.Reaction;
import com.levelrin.jwsserver.session.Session;
import com.levelrin.jwsserver.session.SyncSession;
import com.levelrin.jwsserver.session.UuidSession;

import java.io.ByteArrayOutputStream;
import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.atomic.AtomicReference;

/**
 * This is the place where we compose all the objects for you after setting dependencies.
 */
@SuppressWarnings(
    {
    "unchecked",
    "ClassDataAbstractionCoupling",
    "ClassFanOutComplexity",
    "MethodLength",
    "LineLength",
    "PMD.ExcessiveImports",
    "PMD.ExcessiveMethodLength",
    "PMD.ShortMethodName",
    "PMD.CloseResource"
    }
)
public final class FinalGuide {

    /**
     * It should contain all the dependencies for composing objects.
     */
    private final Map<String, Object> dependencies;

    /**
     * Constructor.
     * @param dependencies See {@link FinalGuide#dependencies}.
     */
    public FinalGuide(final Map<String, Object> dependencies) {
        this.dependencies = dependencies;
    }

    /**
     * Compose all the objects and start the server.
     */
    public void go() {
        final ExecutorService serverThread = (ExecutorService) this.dependencies.get("serverThread");
        final ServerSocket serverSocket = (ServerSocket) this.dependencies.get("serverSocket");
        final ExecutorService socketThread = (ExecutorService) this.dependencies.get("socketThread");
        final String host = (String) this.dependencies.get("host");
        final List<Reaction> reactions = (List<Reaction>) this.dependencies.get("reactions");
        final List<String> endpoints = new ArrayList<>();
        for (final Reaction reaction : reactions) {
            endpoints.add(reaction.endpoint());
        }
        final WsServer server = new WithThread(
            serverThread,
            new WithLoop(
                serverSocket,
                new OnConnection(
                    serverSocket,
                    ioException -> {
                        throw new IllegalStateException(
                            "We caught an exception while waiting for the socket connection.",
                            ioException
                        );
                    },
                    socket -> new WithThread(
                        socketThread,
                        new CloseSocketWhenDone(
                            socket,
                            new OnRequestLines(
                                socket,
                                ioException -> {
                                    throw new IllegalStateException(
                                        "We caught an exception while reading the lines from the socket.",
                                        ioException
                                    );
                                },
                                lines -> new OnHeaders(
                                    lines,
                                    headers -> new OnWriter(
                                        socket,
                                        ioException -> {
                                            throw new IllegalStateException(
                                                "Failed to write to the socket.",
                                                ioException
                                            );
                                        },
                                        writer -> new OnOpeningResult(
                                            new FormatFailure(
                                                new CheckHttpVersion(
                                                    lines,
                                                    new CheckEndpoint(
                                                        lines,
                                                        endpoints,
                                                        new CheckHeadersExist(
                                                            headers,
                                                            new CheckHost(
                                                                headers,
                                                                host,
                                                                new CheckUpgrade(
                                                                    headers,
                                                                    new CheckConnectionHeader(
                                                                        headers,
                                                                        new CheckWsKey(
                                                                            headers,
                                                                            new CheckWsVersion(
                                                                                headers,
                                                                                new OnSecWsAccept(
                                                                                    headers,
                                                                                    FormatSuccess::new
                                                                                )
                                                                            )
                                                                        )
                                                                    )
                                                                )
                                                            )
                                                        )
                                                    )
                                                )
                                            ),
                                            openingSuccess -> new InformOpeningSuccess(
                                                writer,
                                                openingSuccess,
                                                new OnReaction(
                                                    lines,
                                                    reactions,
                                                    selectedReaction -> {
                                                        final BinarySource source = new CheckLength(
                                                            new SocketBinary(
                                                                socket,
                                                                ioException -> {
                                                                    throw new IllegalStateException(
                                                                        "Failed to read the data from the socket.",
                                                                        ioException
                                                                    );
                                                                }
                                                            )
                                                        );
                                                        final Session session = new SyncSession(
                                                            new UuidSession(socket)
                                                        );
                                                        final AtomicReference<String> opcodeCache = new AtomicReference<>();
                                                        final ByteArrayOutputStream dataCache = new ByteArrayOutputStream();
                                                        return new StartCommunication(
                                                            selectedReaction,
                                                            session,
                                                            new WithFraming(
                                                                new Fin(
                                                                    source,
                                                                    fin -> new Rsv1(
                                                                        source,
                                                                        rsv1 -> new Rsv2(
                                                                            source,
                                                                            rsv2 -> new Rsv3(
                                                                                source,
                                                                                rsv3 -> new Opcode(
                                                                                    source,
                                                                                    opcode -> new Mask(
                                                                                        source,
                                                                                        new PayloadLength(
                                                                                            source,
                                                                                            length -> new MaskingKey(
                                                                                                source,
                                                                                                key -> new PayloadData(
                                                                                                    source,
                                                                                                    length,
                                                                                                    data -> new UnmaskedData(
                                                                                                        key,
                                                                                                        data,
                                                                                                        unmasked -> new OnSaveOpcode(
                                                                                                            opcode,
                                                                                                            opcodeCache,
                                                                                                            cachedOpcode -> new OnSavePayload(
                                                                                                                unmasked,
                                                                                                                dataCache,
                                                                                                                cachedData -> new Defragmentation(
                                                                                                                    fin,
                                                                                                                    dataCache,
                                                                                                                    new ControlSection(
                                                                                                                        cachedOpcode,
                                                                                                                        Arrays.asList(
                                                                                                                            new ContinuationControl(),
                                                                                                                            new TextControl(
                                                                                                                                cachedData,
                                                                                                                                session,
                                                                                                                                selectedReaction
                                                                                                                            ),
                                                                                                                            new BinaryControl(
                                                                                                                                cachedData,
                                                                                                                                session,
                                                                                                                                selectedReaction
                                                                                                                            ),
                                                                                                                            new ReservedNonControl(),
                                                                                                                            new CloseControl(
                                                                                                                                session,
                                                                                                                                selectedReaction
                                                                                                                            ),
                                                                                                                            new PingControl(socket),
                                                                                                                            new PongControl(),
                                                                                                                            new ReservedControl()
                                                                                                                        )
                                                                                                                    )
                                                                                                                )
                                                                                                            )
                                                                                                        )
                                                                                                    )
                                                                                                )
                                                                                            )
                                                                                        )
                                                                                    )
                                                                                )
                                                                            )
                                                                        )
                                                                    )
                                                                )
                                                            )
                                                        );
                                                    }
                                                )
                                            ),
                                            openingFailure -> new InformOpeningFailure(writer, openingFailure)
                                        )
                                    )
                                )
                            )
                        )
                    )
                )
            )
        );
        server.start();
    }

}
