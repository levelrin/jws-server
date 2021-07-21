/*
 * Copyright (c) 2020 Rin (https://www.levelrin.com)
 *
 * This file has been created under the terms of the MIT License.
 * See the details at https://github.com/levelrin/jws-server/blob/main/LICENSE
 */

package com.levelrin.jwsserver.guide;

import com.levelrin.jwsserver.reaction.Reaction;
import com.levelrin.jwsserver.session.Session;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
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
                "Message from the client: %s%n",
                message
            );
        }

        @Override
        public void onMessage(final Session session, final byte[] message) {
            throw new UnsupportedOperationException("Not implemented yet.");
        }

        @Override
        public void onClose(final Session session) {
            throw new UnsupportedOperationException("Not implemented yet.");
        }

    }

}
