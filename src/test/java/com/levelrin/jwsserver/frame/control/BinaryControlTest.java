/*
 * Copyright (c) 2020 Rin (https://www.levelrin.com)
 *
 * This file has been created under the terms of the MIT License.
 * See the details at https://github.com/levelrin/jws-server/blob/main/LICENSE
 */

package com.levelrin.jwsserver.frame.control;

import com.levelrin.jwsserver.reaction.Reaction;
import com.levelrin.jwsserver.session.Session;
import org.hamcrest.CoreMatchers;
import org.hamcrest.MatcherAssert;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

/**
 * Tests.
 */
final class BinaryControlTest {

    @Test
    public void shouldMatchOpcodeForBinaryFrame() {
        MatcherAssert.assertThat(
            new BinaryControl(
                new byte[1],
                Mockito.mock(Session.class),
                Mockito.mock(Reaction.class)
            ).match("0010"),
            CoreMatchers.equalTo(true)
        );
    }

    @Test
    @SuppressWarnings("MagicNumber")
    public void shouldCallReactionWithBytes() throws InterruptedException {
        final CountDownLatch latch = new CountDownLatch(1);
        final Reaction reaction = Mockito.mock(Reaction.class);
        final AtomicReference<byte[]> actual = new AtomicReference<>();
        Mockito.doAnswer(invocation -> {
            final byte[] data = invocation.getArgument(1);
            actual.set(data);
            latch.countDown();
            return data;
        }).when(reaction).onMessage(
            Mockito.any(Session.class),
            Mockito.any(byte[].class)
        );
        new BinaryControl(
            new byte[] {
                1, 2, 3,
            },
            Mockito.mock(Session.class),
            reaction
        ).section();
        final int timeout = 300;
        if (latch.await(timeout, TimeUnit.MILLISECONDS)) {
            MatcherAssert.assertThat(
                actual.get(),
                CoreMatchers.equalTo(
                    new byte[] {
                        1, 2, 3,
                    }
                )
            );
        } else {
            Assertions.fail("The reaction was not called.");
        }
    }

}
