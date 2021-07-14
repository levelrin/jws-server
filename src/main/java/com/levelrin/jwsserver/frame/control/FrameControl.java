/*
 * Copyright (c) 2020 Rin (https://www.levelrin.com)
 *
 * This file has been created under the terms of the MIT License.
 * See the details at https://github.com/levelrin/jws-server/blob/main/LICENSE
 */

package com.levelrin.jwsserver.frame.control;

import com.levelrin.jwsserver.frame.FrameSection;

/**
 * We need to check the opcode and interpret the payload data in a correct way.
 * In other words, we need conditional branches to execute the appropriate method.
 * It represents those conditional branches, which means it's responsible for checking the opcode.
 * If the opcode belongs to this, we will use its frame section to handle the data.
 *
 * The following table shows the definition of opcode:
 * | A single hexadecimal digit | binary    | decimal | semantics                               |
 * | %x0                        | 0000      | 0       | continuation frame                      |
 * | %x1                        | 0001      | 1       | text frame                              |
 * | %x2                        | 0010      | 2       | binary frame                            |
 * | %x3-7                      | 0011-0111 | 3-7     | reserved for further non-control frames |
 * | %x8                        | 1000      | 8       | connection close                        |
 * | %x9                        | 1001      | 9       | ping                                    |
 * | %xA                        | 1010      | 10      | pong                                    |
 * | %xB-F                      | 1011-1111 | 11-15   | reserved for further control frames     |
 */
public interface FrameControl {

    /**
     * Check if the opcode belongs to this control.
     * @param opcode It defines how we should interpret the payload data.
     * @return True if opcode belongs to this control. Otherwise, false.
     */
    boolean match(String opcode);

    /**
     * We will use this method if {@link FrameControl#match(String)} is true.
     * @return The correct frame section for the opcode.
     */
    FrameSection section();

}
