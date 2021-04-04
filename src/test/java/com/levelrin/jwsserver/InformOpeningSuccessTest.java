/*
 * Copyright (c) 2020 Rin (https://www.levelrin.com)
 *
 * This file has been created under the terms of the MIT License.
 * See the details at https://github.com/levelrin/jws-server/blob/main/LICENSE
 */

package com.levelrin.jwsserver;

import com.levelrin.jwsserver.opening.OpeningResult;
import org.hamcrest.CoreMatchers;
import org.hamcrest.MatcherAssert;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import java.io.PrintWriter;
import java.util.concurrent.atomic.AtomicReference;

/**
 * Tests.
 */
final class InformOpeningSuccessTest {

    @Test
    @SuppressWarnings("PMD.CloseResource")
    public void shouldWriteSuccessMessage() {
        final PrintWriter writer = Mockito.mock(PrintWriter.class);
        final AtomicReference<String> reply = new AtomicReference<>();
        Mockito.doAnswer(invocation -> {
            reply.set(invocation.getArgument(0));
            return reply;
        }).when(writer).println(Mockito.anyString());
        final OpeningResult result = Mockito.mock(OpeningResult.class);
        Mockito.doReturn("Yoi Yoi").when(result).message();
        new InformOpeningSuccess(
            writer,
            result
        ).start();
        MatcherAssert.assertThat(
            reply.get(),
            CoreMatchers.equalTo("Yoi Yoi")
        );
        Mockito.verify(writer).flush();
    }

}
