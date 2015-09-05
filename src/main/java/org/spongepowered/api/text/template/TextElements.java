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
package org.spongepowered.api.text.template;

import com.google.common.base.Function;
import com.google.common.base.Optional;
import com.google.common.base.Preconditions;
import org.spongepowered.api.data.key.Key;
import org.spongepowered.api.data.key.Keys;
import org.spongepowered.api.data.value.BaseValue;
import org.spongepowered.api.data.value.ValueContainer;
import org.spongepowered.api.data.value.mutable.CompositeValueStore;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.TextBuilder;
import org.spongepowered.api.text.Texts;
import org.spongepowered.api.text.translation.Translatable;

import java.util.Collection;
import java.util.Iterator;

public final class TextElements {

    public static Text DEFAULT_SEPARATOR = Texts.of(", ");

    public static TextElement<Text> identity() {
        return new TextElement<Text>() {
            @Override
            public Text create(Text value) {
                return value;
            }
        };
    }

    public static <T> TextElement<T> always(final Text text) {
        return new TextElement<T>() {
            @Override
            public Text create(T value) {
                return text;
            }
        };
    }

    public static <T> TextElement<T> function(final Function<T, Text> function) {
        return new TextElement<T>() {
            @Override
            public Text create(T value) {
                return Preconditions.checkNotNull(function.apply(value));
            }
        };
    }

    public static <T, U> TextElement<U> compose(final TextElement<T> element, final Function<U, T> function) {
        return new TextElement<U>() {
            @Override
            public Text create(U value) {
                return element.create(Preconditions.checkNotNull(function.apply(value)));
            }
        };
    }

    public static <T> TextElement<T> map(final TextElement<T> element, final Function<Text, Text> function) {
        return new TextElement<T>() {
            @Override
            public Text create(T value) {
                return Preconditions.checkNotNull(function.apply(element.create(value)));
            }
        };
    }

    public static <E> TextElement<Optional<E>> optional(final TextElement<E> singleArg) {
        return new TextElement<Optional<E>>() {
            @Override
            public Text create(Optional<E> value) {
                if (value.isPresent()) {
                    return singleArg.create(value.get());
                } else {
                    return Texts.of();
                }
            }
        };
    }

    public static <E> TextElement<Iterator<E>> iterator(final TextElement<E> singleArg, final Text separator) {
        return new TextElement<Iterator<E>>() {
            @Override
            public Text create(Iterator<E> iterator) {
                TextBuilder builder = Texts.builder();
                boolean first = true;
                while (iterator.hasNext()) {
                    Text next = singleArg.create(iterator.next());
                    if (!next.isEmpty()) {
                        if (!first) {
                            builder.append(separator);
                        }
                        first = false;
                        builder.append(next);
                    }
                }
                return builder.build();
            }
        };
    }

    public static <E> TextElement<Iterator<E>> iterator(final TextElement<E> singleArg) {
        return iterator(singleArg, DEFAULT_SEPARATOR);
    }

    public static <E> TextElement<Iterable<E>> iterable(final TextElement<E> singleArg, final Text separator) {
        return new TextElement<Iterable<E>>() {
            @Override
            public Text create(Iterable<E> value) {
                return iterator(singleArg, separator).create(value.iterator());
            }
        };
    }

    public static <E> TextElement<Iterable<E>> iterable(final TextElement<E> singleArg) {
        return iterable(singleArg, DEFAULT_SEPARATOR);
    }

    public static <T> TextElement<T> fallback(final TextElement<T> thatArg, final TextElement<T> fallbackArg) {
        return new TextElement<T>() {
            @Override
            public Text create(T value) {
                Text result = thatArg.create(value);
                if (result.isEmpty()) {
                    return fallbackArg.create(value);
                } else {
                    return result;
                }
            }
        };
    }

    public static <T> TextElement<T> fallback(final TextElement<T> thatArg, final Text fallback) {
        return new TextElement<T>() {
            @Override
            public Text create(T value) {
                Text result = thatArg.create(value);
                if (result.isEmpty()) {
                    return fallback;
                } else {
                    return result;
                }
            }
        };
    }

    public static <T extends Translatable> TextElement<T> translatable() {
        return new TextElement<T>() {
            @Override
            public Text create(T value) {
                return Texts.of(value);
            }
        };
    }

    public static TextElement<Player> playerDisplayName() {
        return key(Keys.DISPLAY_NAME);
    }

    public static <C extends CompositeValueStore<?, ?>> TextElement<C> key(
        final Key<? extends BaseValue<Text>> key) {
        return new TextElement<C>() {
            @Override
            public Text create(C value) {
                return value.get(key).or(Texts.of());
            }
        };
    }

    // collectionKey(Keys.SIGN_LINES, iterable(identity()));
    public static <C extends CompositeValueStore<?, ?>, L extends Collection<Text>> TextElement<C> collectionKey(
        final Key<? extends BaseValue<L>> key, final TextElement<? super L> join) {
        return new TextElement<C>() {
            @Override
            public Text create(C value) {
                Optional<L> possibleColl = value.get(key);
                if (possibleColl.isPresent()) {
                    return join.create(possibleColl.get());
                } else {
                    return Texts.of();
                }
            }
        };
    }

    // optionalKey(Keys.LAST_COMMAND_OUTPUT);
    public static <C extends CompositeValueStore<?, ?>> TextElement<C> optionalKey(
        final Key<? extends BaseValue<Optional<Text>>> key) {
        return new TextElement<C>() {
            @Override
            public Text create(C value) {
                Optional<Text> possibleColl = value.getOrElse(key, Optional.<Text>absent());
                if (possibleColl.isPresent()) {
                    return possibleColl.get();
                } else {
                    return Texts.of();
                }
            }
        };
    }

}
