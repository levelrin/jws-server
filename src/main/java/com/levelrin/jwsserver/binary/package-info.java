/*
 * Copyright (c) 2020 Rin (https://www.levelrin.com)
 *
 * This file has been created under the terms of the MIT License.
 * See the details at https://github.com/levelrin/jws-server/blob/main/LICENSE
 */

/**
 * This package contains classes related to binary numbers (bits).
 *
 * Motivation:
 * The {@link java.net.Socket} provides data in byte, which is not a convenient amount of data
 * for WebSocket framing.
 * Instead, we want to have data in bit.
 * We will create utility objects to work better with binary data.
 */
package com.levelrin.jwsserver.binary;
