/*
 * Copyright (c) 2020 Rin (https://www.levelrin.com)
 *
 * This file has been created under the terms of the MIT License.
 * See the details at https://github.com/levelrin/jws-server/blob/main/LICENSE
 */

package com.levelrin.jwsserver.session.advanced;

import com.levelrin.jwsserver.io.CheckableOutputStream;
import org.hamcrest.CoreMatchers;
import org.hamcrest.MatcherAssert;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

/**
 * Tests.
 */
final class AdvancedCloseTest {

    @Test
    @SuppressWarnings({"MagicNumber", "PMD.CloseResource"})
    public void shouldSendCloseFrameWithCodeAndReason() throws IOException {
        final Socket socket = Mockito.mock(Socket.class);
        final CheckableOutputStream stream = new CheckableOutputStream();
        Mockito.doReturn(stream).when(socket).getOutputStream();
        final short code = 1000;
        final String reason = "A normal close.";
        new AdvancedClose(socket).close(code, reason);
        final ByteArrayOutputStream expected = new ByteArrayOutputStream();
        // -120 is equivalent to 10001000 in binary.
        // That means it's a FIN close frame.
        expected.write(-120);
        // 17 is equivalent to 00010001.
        // That means the unmasked payload length is 17,
        // in which 2 bytes are from the reason and 15 bytes are from the reason.
        expected.write(17);
        // 3 is equivalent to 00000011.
        // -24 is equivalent to 11101000.
        // The combination is 0000001111101000, which is 1000 in decimal.
        // In other words, 3 and -24 represent the code.
        expected.write(3);
        expected.write(-24);
        expected.write(reason.getBytes(StandardCharsets.UTF_8));
        MatcherAssert.assertThat(
            stream.sentData(),
            CoreMatchers.equalTo(
                expected.toByteArray()
            )
        );
    }

}
