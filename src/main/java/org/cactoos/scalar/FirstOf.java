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

import org.cactoos.Func;
import org.cactoos.Scalar;
import org.cactoos.func.FuncOf;
import org.cactoos.iterable.Filtered;

/**
 * Find first element in a list that satisfies specified condition.
 *
 * <p>There is no thread-safety guarantee.
 *
 * @param <T> Type of result
 * @since 0.32
 */
public final class FirstOf<T> implements Scalar<T> {

    /**
     * Condition for getting the element.
     */
    private final Func<? super T, Boolean> condition;

    /**
     * Source iterable.
     */
    private final Iterable<? extends T> source;

    /**
     * Fallback used if no value matches.
     */
    private final Scalar<? extends T> fallback;

    /**
     * Constructor with default condition (always `true`) and plain fallback.
     * @param src Source iterable
     * @param fbck Fallback used if no value matches
     */
    public FirstOf(final Iterable<? extends T> src, final T fbck) {
        this(
            new FuncOf<>(new True()),
            src,
            () -> fbck
        );
    }

    /**
     * Constructor with default condition (always `true`).
     * @param src Source iterable
     * @param fbck Fallback used if no value matches
     */
    public FirstOf(final Iterable<? extends T> src, final Scalar<? extends T> fbck) {
        this(
            new FuncOf<>(new True()),
            src,
            fbck
        );
    }

    /**
     * Constructor.
     * @param cond Condition for getting the element
     * @param src Source iterable
     * @param fbck Fallback used if no value matches
     */
    public FirstOf(
        final Func<? super T, Boolean> cond,
        final Iterable<? extends T> src,
        final Scalar<? extends T> fbck
    ) {
        this.condition = cond;
        this.source = src;
        this.fallback = fbck;
    }

    @Override
    public T value() throws Exception {
        return new ItemAt<>(
            0,
            new FuncOf<>(this.fallback),
            new Filtered<>(this.condition, this.source)
        ).value();
    }
}
