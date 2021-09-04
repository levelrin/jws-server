/*
 * Copyright (c) 2020 Rin (https://www.levelrin.com)
 *
 * This file has been created under the terms of the MIT License.
 * See the details at https://github.com/levelrin/jws-server/blob/main/LICENSE
 */

package com.levelrin.jwsserver.session.option;

import com.levelrin.jwsserver.session.advanced.Ping;
import java.net.Socket;
import java.util.function.Function;

/**
 * It gives you an option to ping the client.
 */
public final class PingOption implements Function<Socket, Ping> {

    @Override
    public Ping apply(final Socket socket) {
        return new Ping(socket);
    }

}
