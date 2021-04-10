/*
 * Copyright (c) 2020 Rin (https://www.levelrin.com)
 *
 * This file has been created under the terms of the MIT License.
 * See the details at https://github.com/levelrin/jws-server/blob/main/LICENSE
 */

package com.levelrin.jwsserver;

import com.levelrin.jwsserver.reaction.Reaction;
import com.levelrin.jwsserver.session.Session;
import org.hamcrest.CoreMatchers;
import org.hamcrest.MatcherAssert;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Tests.
 */
final class StartCommunicationTest {

    @Test
    public void shouldReactToStart() {
        final Reaction reaction = Mockito.mock(Reaction.class);
        final Session session = Mockito.mock(Session.class);
        final AtomicBoolean started = new AtomicBoolean(false);
        Mockito.doAnswer(invocation -> {
            started.set(true);
            return started;
        }).when(reaction).onStart(session);
        new StartCommunication(
            reaction,
            session,
            Mockito.mock(WsServer.class)
        ).start();
        MatcherAssert.assertThat(
            started.get(),
            CoreMatchers.equalTo(true)
        );
    }

}
