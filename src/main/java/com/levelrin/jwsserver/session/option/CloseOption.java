/*
 * Copyright (c) 2020 Rin (https://www.levelrin.com)
 *
 * This file has been created under the terms of the MIT License.
 * See the details at https://github.com/levelrin/jws-server/blob/main/LICENSE
 */

package com.levelrin.jwsserver.session.option;

import com.levelrin.jwsserver.session.advanced.AdvancedClose;
import java.net.Socket;
import java.util.function.Function;

/**
 * It gives you more closing options.
 */
public final class CloseOption implements Function<Socket, AdvancedClose> {

    @Override
    public AdvancedClose apply(final Socket socket) {
        return new AdvancedClose(socket);
    }

}
