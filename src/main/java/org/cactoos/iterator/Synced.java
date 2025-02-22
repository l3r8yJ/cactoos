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
package org.cactoos.iterator;

import java.util.Iterator;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * Synchronized {@link Iterator} implementation using a {@link ReadWriteLock}
 * either provided to the constructor or an internally created
 * {@link ReentrantReadWriteLock}.
 *
 * <p>The {@link ReadWriteLock} is used to synchronize read calls to
 * {@link Synced#hasNext()} against write calls to
 * {@link Synced#next()} and write calls to any other read or write
 * calls.</p>
 *
 * <p>Objects of this class are thread-safe.</p>
 *
 * @param <T> The type of the iterator.
 * @since 1.0
 */
public final class Synced<T> implements Iterator<T> {
    /**
     * The original iterator.
     */
    private final Iterator<? extends T> iterator;

    /**
     * The lock to use.
     */
    private final ReadWriteLock lock;

    /**
     * Ctor.
     * @param iterator The iterator to synchronize access to.
     */
    public Synced(final Iterator<? extends T> iterator) {
        this(new ReentrantReadWriteLock(), iterator);
    }

    /**
     * Ctor.
     * @param lock The lock to use for synchronization.
     * @param iterator The iterator to synchronize access to.
     */
    public Synced(final ReadWriteLock lock, final Iterator<? extends T> iterator) {
        this.iterator = iterator;
        this.lock = lock;
    }

    @Override
    public boolean hasNext() {
        this.lock.readLock().lock();
        try {
            return this.iterator.hasNext();
        } finally {
            this.lock.readLock().unlock();
        }
    }

    @Override
    public T next() {
        this.lock.writeLock().lock();
        try {
            return this.iterator.next();
        } finally {
            this.lock.writeLock().unlock();
        }
    }
}
