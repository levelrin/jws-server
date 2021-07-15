/*
 * Copyright (c) 2020 Rin (https://www.levelrin.com)
 *
 * This file has been created under the terms of the MIT License.
 * See the details at https://github.com/levelrin/jws-server/blob/main/LICENSE
 */

package com.levelrin.jwsserver;

import com.levelrin.jwsserver.frame.FrameSection;

/**
 * It's responsible for running a composed {@link FrameSection} object.
 * WebSocket frame consists of various sections such as FIN, RSV, opcode, MASK, and etc.
 * Each section will handle the binary data from the socket
 * and let the next section handle the rest of the data.
 * Each frame section object has next section and so on.
 * Eventually, we will have a recursive composition of frame sections.
 * Ex) FIN -> RSV -> opcode -> MASK -> Payload len -> Payload Data ->
 * our artificial section to do something -> FIN (again) -> ...
 * The above shows how we are going to constantly read and handle the data from the socket.
 */
public final class WithFraming implements WsServer {

    /**
     * It's for reading and handling the data from the socket recursively.
     */
    private final FrameSection section;

    /**
     * Constructor.
     * @param section See {@link WithFraming#section}.
     */
    public WithFraming(final FrameSection section) {
        this.section = section;
    }

    @Override
    public void start() {
        FrameSection nextSection = this.section.next();
        while (nextSection.shouldContinue()) {
            if (nextSection.isEnd()) {
                nextSection = this.section.next();
            } else {
                nextSection = nextSection.next();
            }
        }
    }

}
