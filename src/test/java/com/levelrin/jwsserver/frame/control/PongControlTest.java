/*
 * Copyright (c) 2020 Rin (https://www.levelrin.com)
 *
 * This file has been created under the terms of the MIT License.
 * See the details at https://github.com/levelrin/jws-server/blob/main/LICENSE
 */

package com.levelrin.jwsserver.frame.control;

import com.levelrin.jwsserver.frame.End;
import com.levelrin.jwsserver.session.Session;
import org.hamcrest.CoreMatchers;
import org.hamcrest.MatcherAssert;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import java.util.concurrent.atomic.AtomicReference;

/**
 * Tests.
 */
final class PongControlTest {

    @Test
    public void shouldMatchOpcodeForPongFrame() {
        MatcherAssert.assertThat(
            new PongControl(
                Mockito.mock(Session.class),
                session -> { }
            ).match("1010"),
            CoreMatchers.equalTo(true)
        );
    }

    @Test
    public void shouldReturnEnd() {
        MatcherAssert.assertThat(
            new PongControl(
                Mockito.mock(Session.class),
                session -> { }
            ).section(),
            CoreMatchers.instanceOf(End.class)
        );
    }

    @Test
    public void shouldRunFunctionOnPongWithSession() {
        final Session session = Mockito.mock(Session.class);
        Mockito.doReturn("Yoi").when(session).id();
        final AtomicReference<String> actual = new AtomicReference<>();
        new PongControl(
            session,
            selectedSession -> actual.set(selectedSession.id())
        ).section();
        MatcherAssert.assertThat(
            actual.get(),
            CoreMatchers.equalTo("Yoi")
        );
    }

}
