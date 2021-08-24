/*
 * Copyright (c) 2020 Rin (https://www.levelrin.com)
 *
 * This file has been created under the terms of the MIT License.
 * See the details at https://github.com/levelrin/jws-server/blob/main/LICENSE
 */

package com.levelrin.jwsserver.frame.control;

import com.levelrin.jwsserver.frame.StopFraming;
import com.levelrin.jwsserver.reaction.Reaction;
import com.levelrin.jwsserver.session.Session;
import org.hamcrest.CoreMatchers;
import org.hamcrest.MatcherAssert;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * Tests.
 */
final class CloseControlTest {

    @Test
    public void shouldMatchOpcodeForCloseFrame() {
        MatcherAssert.assertThat(
            new CloseControl(
                new byte[] {0, 0},
                Mockito.mock(Session.class),
                Mockito.mock(Reaction.class)
            ).match("1000"),
            CoreMatchers.equalTo(true)
        );
    }

    @Test
    public void nextSectionShouldBeStopFraming() {
        MatcherAssert.assertThat(
            new CloseControl(
                new byte[] {0, 0},
                Mockito.mock(Session.class),
                Mockito.mock(Reaction.class)
            ).section(),
            CoreMatchers.instanceOf(StopFraming.class)
        );
    }

    @Test
    public void shouldCallOnClose() {
        final Reaction reaction = Mockito.mock(Reaction.class);
        final Session session = Mockito.mock(Session.class);
        new CloseControl(
            new byte[] {0, 0},
            session,
            reaction
        ).section();
        Mockito.verify(reaction).onClose(session, 0, "");
    }

    @Test
    @SuppressWarnings("MagicNumber")
    public void shouldReadCodeAndReason() throws IOException {
        final Reaction reaction = Mockito.mock(Reaction.class);
        final Session session = Mockito.mock(Session.class);
        final ByteArrayOutputStream payload = new ByteArrayOutputStream();
        // The first two bytes indicates the status code of 1000.
        payload.write(3);
        payload.write(-24);
        payload.write("Yoi Yoi".getBytes(StandardCharsets.UTF_8));
        new CloseControl(
            payload.toByteArray(),
            session,
            reaction
        ).section();
        Mockito.verify(reaction).onClose(session, 1000, "Yoi Yoi");
    }

}
