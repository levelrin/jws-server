/*
 * Copyright (c) 2020 Rin (https://www.levelrin.com)
 *
 * This file has been created under the terms of the MIT License.
 * See the details at https://github.com/levelrin/jws-server/blob/main/LICENSE
 */

package com.levelrin.jwsserver.guide;

import com.levelrin.jwsserver.reaction.Reaction;
import com.levelrin.jwsserver.session.Session;
import com.levelrin.jwsserver.session.option.CloseOption;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.CountDownLatch;

/**
 * Tests.
 */
@SuppressWarnings("PMD.SystemPrintln")
final class FinalGuideTest {

    @Test
    @Disabled
    public void manualTest() throws InterruptedException {
        System.out.println("START");
        final int port = 4567;
        new JwsGuide()
            .defaultServerThread()
            .port(port)
            .defaultSocketThread()
            .skipHostValidation()
            .reaction(new AdHocReaction())
            .ready()
            .go();
        // We don't have to use a latch in real environment.
        // It's only necessary in unit testing environment.
        final CountDownLatch latch = new CountDownLatch(1);
        latch.await();
        System.out.println("END");
    }

    /**
     * Reaction for manual testing.
     */
    private static final class AdHocReaction implements Reaction {

        @Override
        public String endpoint() {
            return "/";
        }

        @Override
        public void onStart(final Session session) {
            System.out.println("Connected to a client.");
        }

        @Override
        public void onMessage(final Session session, final String message) {
            System.out.printf(
                "A text message from the client: %s%n",
                message
            );
            session.sendMessage("Hello back from the server!");
            session.sendMessage("Haha".getBytes(StandardCharsets.UTF_8));
        }

        @Override
        public void onMessage(final Session session, final byte[] message) {
            System.out.printf(
                "A binary message from the client: %s%n",
                new String(message, StandardCharsets.UTF_8)
            );
            //session.close();
            final short code = 1000;
            session.advanced(new CloseOption()).close(code, "Normal Close!");
        }

        @Override
        public void onClose(final Session session, final int code, final String reason) {
            System.out.printf(
                "Connection closed. session: %s%ncode: %s%nreason: %s%n",
                session.id(),
                code,
                reason
            );
        }

    }

}
