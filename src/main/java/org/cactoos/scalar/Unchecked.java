/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2017-2022 Yegor Bugayenko
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included
 * in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NON-INFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package org.cactoos.scalar;

import java.io.IOException;
import java.io.UncheckedIOException;
import org.cactoos.Scalar;

/**
 * Scalar that doesn't throw checked {@link Exception}.
 *
 * <p>There is no thread-safety guarantee.
 *
 * @param <T> Type of result
 * @since 0.3
 */
public final class Unchecked<T> implements Scalar<T> {

    /**
     * Original origin.
     */
    private final Scalar<? extends T> origin;

    /**
     * Ctor.
     * @param scalar Encapsulated origin
     */
    public Unchecked(final Scalar<? extends T> scalar) {
        this.origin = scalar;
    }

    @Override
    public T value() {
        try {
            return new IoChecked<>(this.origin).value();
        } catch (final IOException ex) {
            throw new UncheckedIOException(ex);
        }
    }

}
