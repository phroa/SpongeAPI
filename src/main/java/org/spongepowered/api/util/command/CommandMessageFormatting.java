/*
 * This file is part of SpongeAPI, licensed under the MIT License (MIT).
 *
 * Copyright (c) SpongePowered <https://www.spongepowered.org>
 * Copyright (c) contributors
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package org.spongepowered.api.util.command;

import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.Texts;
import org.spongepowered.api.text.format.TextColors;

public interface CommandMessageFormatting {
    Text PIPE_TEXT = Texts.of("|");
    Text SPACE_TEXT = Texts.of(" ");
    Text PLUS_TEXT = Texts.of("*");
    Text NEWLINE_TEXT = Texts.of('\n');
    Text LT_TEXT = Texts.of("<");
    Text GT_TEXT = Texts.of(">");
    Text ELIPSES_TEXT = Texts.of("…");

    /**
     * Format text to be output as an error directly to a sender. Not necessary when creating an exception to be thrown
     *
     * @param error The error message
     * @return The formatted error message.
     */
    static Text error(Text error) {
        return error.builder().color(TextColors.RED).build();
    }

    /**
     * Format text to be output as a debug message directly to a sender.
     *
     * @param debug The debug message
     * @return The formatted debug message.
     */
    static Text debug(Text debug) {
        return debug.builder().color(TextColors.GRAY).build();
    }

}
