/**
 * The MIT License (MIT)
 *
 * Copyright (c) 2017 Yegor Bugayenko
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
package org.cactoos;

import java.io.IOException;
import java.io.OutputStream;
import org.cactoos.io.FileAsOutput;
import org.cactoos.io.TeeInput;

/**
 * Output.
 *
 * <p>Here is for example how {@link Output} can be used
 * together with {@link Input} in order to modify the content
 * of a text file:</p>
 *
 * <pre> new LengthOfInput(
 *   new TeeInput(
 *     new TextAsInput(new TextOf("Hello, world!")),
 *     new FileAsOutput(new File("/tmp/names.txt"))
 *   )
 * ).asValue();</pre>
 *
 * <p>Here {@link FileAsOutput} implements {@link Output} and behaves like
 * one, providing write-only access to the encapsulated
 * {@link java.io.File}. The {@link TeeInput} copies the content of the
 * input to the output. The {@link org.cactoos.io.LengthOfInput}
 * calculates the size of the copied data.</p>
 *
 * <p>There is no thread-safety guarantee.
 *
 * @author Yegor Bugayenko (yegor256@gmail.com)
 * @version $Id$
 * @see FileAsOutput
 * @see org.cactoos.io.PathAsOutput
 * @since 0.1
 */
public interface Output {

    /**
     * Get write access to it.
     * @return InputStream to read from
     * @throws IOException If something goes wrong
     */
    OutputStream stream() throws IOException;

    /**
     * Output check for no nulls.
     *
     * @author Fabricio Cabral (fabriciofx@gmail.com)
     * @version $Id$
     * @since 0.10
     */
    final class NoNulls implements Output {
        /**
         * The output.
         */
        private final Output origin;
        /**
         * Ctor.
         * @param output The output
         */
        public NoNulls(final Output output) {
            this.origin = output;
        }
        @Override
        public OutputStream stream() throws IOException {
            if (this.origin == null) {
                throw new IOException("NULL instead of a valid output");
            }
            final OutputStream stream = this.origin.stream();
            if (stream == null) {
                throw new IOException("NULL instead of a valid stream");
            }
            return stream;
        }
    }
}
