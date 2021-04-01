/*
 * Copyright (c) 2020 Rin (https://www.levelrin.com)
 *
 * This file has been created under the terms of the MIT License.
 * See the details at https://github.com/levelrin/jws-server/blob/main/LICENSE
 */

package com.levelrin.jwsserver;

import com.levelrin.jwsserver.opening.Opening;
import com.levelrin.jwsserver.opening.OpeningResult;
import org.hamcrest.CoreMatchers;
import org.hamcrest.MatcherAssert;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import java.util.concurrent.atomic.AtomicReference;

/**
 * Tests.
 */
final class OnOpeningResultTest {

    @Test
    public void shouldRunSuccessFunctionOnSuccess() {
        final Opening fakeOpening = Mockito.mock(Opening.class);
        final OpeningResult fakeResult = Mockito.mock(OpeningResult.class);
        Mockito.when(fakeResult.success()).thenReturn(true);
        Mockito.when(fakeResult.message()).thenReturn("The opening handshake was successful!");
        Mockito.when(fakeOpening.handshake()).thenReturn(fakeResult);
        final AtomicReference<OpeningResult> resultCache = new AtomicReference<>();
        new OnOpeningResult(
            fakeOpening,
            success -> {
                resultCache.set(success);
                return Mockito.mock(WsServer.class);
            },
            failure -> {
                throw new IllegalStateException("Failure function was executed.");
            }
        ).start();
        MatcherAssert.assertThat(
            resultCache.get().success(),
            CoreMatchers.equalTo(true)
        );
        MatcherAssert.assertThat(
            resultCache.get().message(),
            CoreMatchers.equalTo("The opening handshake was successful!")
        );
    }

    @Test
    public void shouldRunFailureFunctionOnFailure() {
        final Opening fakeOpening = Mockito.mock(Opening.class);
        final OpeningResult fakeResult = Mockito.mock(OpeningResult.class);
        Mockito.when(fakeResult.success()).thenReturn(false);
        Mockito.when(fakeResult.message()).thenReturn("Task failed successfully.");
        Mockito.when(fakeOpening.handshake()).thenReturn(fakeResult);
        final AtomicReference<OpeningResult> resultCache = new AtomicReference<>();
        new OnOpeningResult(
            fakeOpening,
            success -> {
                throw new IllegalStateException("Success function was executed.");
            },
            failure -> {
                resultCache.set(failure);
                return Mockito.mock(WsServer.class);
            }
        ).start();
        MatcherAssert.assertThat(
            resultCache.get().success(),
            CoreMatchers.equalTo(false)
        );
        MatcherAssert.assertThat(
            resultCache.get().message(),
            CoreMatchers.equalTo("Task failed successfully.")
        );
    }

}
