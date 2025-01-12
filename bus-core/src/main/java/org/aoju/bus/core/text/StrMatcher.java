/*
 * The MIT License
 *
 * Copyright (c) 2017, aoju.org All rights reserved.
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
package org.aoju.bus.core.text;


import org.aoju.bus.core.consts.Symbol;
import org.aoju.bus.core.utils.StringUtils;

import java.util.Arrays;

/**
 * A matcher class that can be queried to determine if a character array
 * portion matches.
 * <p>
 * This class comes complete with various factory methods.
 * If these do not suffice, you can subclass and implement your own matcher.
 *
 * @author Kimi Liu
 * @version 3.0.5
 * @since JDK 1.8
 */
public abstract class StrMatcher {

    /**
     * Matches the comma character.
     */
    private static final StrMatcher COMMA_MATCHER = new CharMatcher(Symbol.C_COMMA);
    /**
     * Matches the tab character.
     */
    private static final StrMatcher TAB_MATCHER = new CharMatcher(Symbol.C_HT);
    /**
     * Matches the space character.
     */
    private static final StrMatcher SPACE_MATCHER = new CharMatcher(Symbol.C_SPACE);
    /**
     * Matches the same characters as StringTokenizer,
     * namely space, tab, newline, formfeed.
     */
    private static final StrMatcher SPLIT_MATCHER = new CharSetMatcher(" \t\n\r\f".toCharArray());
    /**
     * Matches the String trim() whitespace characters.
     */
    private static final StrMatcher TRIM_MATCHER = new TrimMatcher();
    /**
     * Matches the double quote character.
     */
    private static final StrMatcher SINGLE_QUOTE_MATCHER = new CharMatcher(Symbol.C_SINGLE_QUOTE);
    /**
     * Matches the double quote character.
     */
    private static final StrMatcher DOUBLE_QUOTE_MATCHER = new CharMatcher(Symbol.C_DOUBLE_QUOTES);
    /**
     * Matches the single or double quote character.
     */
    private static final StrMatcher QUOTE_MATCHER = new CharSetMatcher("'\"".toCharArray());
    /**
     * Matches no characters.
     */
    private static final StrMatcher NONE_MATCHER = new NoMatcher();

    /**
     * Constructor.
     */
    protected StrMatcher() {
        super();
    }

    /**
     * Returns a matcher which matches the comma character.
     *
     * @return a matcher for a comma
     */
    public static StrMatcher commaMatcher() {
        return COMMA_MATCHER;
    }

    /**
     * Returns a matcher which matches the tab character.
     *
     * @return a matcher for a tab
     */
    public static StrMatcher tabMatcher() {
        return TAB_MATCHER;
    }

    /**
     * Returns a matcher which matches the space character.
     *
     * @return a matcher for a space
     */
    public static StrMatcher spaceMatcher() {
        return SPACE_MATCHER;
    }

    /**
     * Matches the same characters as StringTokenizer,
     * namely space, tab, newline and formfeed.
     *
     * @return the split matcher
     */
    public static StrMatcher splitMatcher() {
        return SPLIT_MATCHER;
    }

    /**
     * Matches the String trim() whitespace characters.
     *
     * @return the trim matcher
     */
    public static StrMatcher trimMatcher() {
        return TRIM_MATCHER;
    }

    /**
     * Returns a matcher which matches the single quote character.
     *
     * @return a matcher for a single quote
     */
    public static StrMatcher singleQuoteMatcher() {
        return SINGLE_QUOTE_MATCHER;
    }

    /**
     * Returns a matcher which matches the double quote character.
     *
     * @return a matcher for a double quote
     */
    public static StrMatcher doubleQuoteMatcher() {
        return DOUBLE_QUOTE_MATCHER;
    }

    /**
     * Returns a matcher which matches the single or double quote character.
     *
     * @return a matcher for a single or double quote
     */
    public static StrMatcher quoteMatcher() {
        return QUOTE_MATCHER;
    }

    /**
     * Matches no characters.
     *
     * @return a matcher that matches nothing
     */
    public static StrMatcher noneMatcher() {
        return NONE_MATCHER;
    }

    /**
     * Constructor that creates a matcher from a character.
     *
     * @param ch the character to match, must not be null
     * @return a new Matcher for the given char
     */
    public static StrMatcher charMatcher(final char ch) {
        return new CharMatcher(ch);
    }

    /**
     * Constructor that creates a matcher from a set of characters.
     *
     * @param chars the characters to match, null or empty matches nothing
     * @return a new matcher for the given char[]
     */
    public static StrMatcher charSetMatcher(final char... chars) {
        if (chars == null || chars.length == 0) {
            return NONE_MATCHER;
        }
        if (chars.length == 1) {
            return new CharMatcher(chars[0]);
        }
        return new CharSetMatcher(chars);
    }

    /**
     * Constructor that creates a matcher from a string representing a set of characters.
     *
     * @param chars the characters to match, null or empty matches nothing
     * @return a new Matcher for the given characters
     */
    public static StrMatcher charSetMatcher(final String chars) {
        if (StringUtils.isEmpty(chars)) {
            return NONE_MATCHER;
        }
        if (chars.length() == 1) {
            return new CharMatcher(chars.charAt(0));
        }
        return new CharSetMatcher(chars.toCharArray());
    }

    /**
     * Constructor that creates a matcher from a string.
     *
     * @param str the string to match, null or empty matches nothing
     * @return a new Matcher for the given String
     */
    public static StrMatcher stringMatcher(final String str) {
        if (StringUtils.isEmpty(str)) {
            return NONE_MATCHER;
        }
        return new StringMatcher(str);
    }

    /**
     * Returns the number of matching characters, zero for no match.
     * <p>
     * This method is called to check for a match.
     * The parameter <code>pos</code> represents the current position to be
     * checked in the string <code>buffer</code> (a character array which must
     * not be changed).
     * The API guarantees that <code>pos</code> is a valid index for <code>buffer</code>.
     * <p>
     * The character array may be larger than the active area to be matched.
     * Only values in the buffer between the specified indices may be accessed.
     * <p>
     * The matching code may check one character or many.
     * It may check characters preceding <code>pos</code> as well as those
     * after, so long as no checks exceed the bounds specified.
     * <p>
     * It must return zero for no match, or a positive number if a match was found.
     * The number indicates the number of characters that matched.
     *
     * @param buffer      the text content to match against, do not change
     * @param pos         the starting position for the match, valid for buffer
     * @param bufferStart the first active index in the buffer, valid for buffer
     * @param bufferEnd   the end index (exclusive) of the active buffer, valid for buffer
     * @return the number of matching characters, zero for no match
     */
    public abstract int isMatch(char[] buffer, int pos, int bufferStart, int bufferEnd);

    /**
     * Returns the number of matching characters, zero for no match.
     * <p>
     * This method is called to check for a match.
     * The parameter <code>pos</code> represents the current position to be
     * checked in the string <code>buffer</code> (a character array which must
     * not be changed).
     * The API guarantees that <code>pos</code> is a valid index for <code>buffer</code>.
     * <p>
     * The matching code may check one character or many.
     * It may check characters preceding <code>pos</code> as well as those after.
     * <p>
     * It must return zero for no match, or a positive number if a match was found.
     * The number indicates the number of characters that matched.
     *
     * @param buffer the text content to match against, do not change
     * @param pos    the starting position for the match, valid for buffer
     * @return the number of matching characters, zero for no match
     * @since 2.4
     */
    public int isMatch(final char[] buffer, final int pos) {
        return isMatch(buffer, pos, 0, buffer.length);
    }

    /**
     * Class used to define a set of characters for matching purposes.
     */
    static final class CharSetMatcher extends StrMatcher {
        /**
         * The set of characters to match.
         */
        private final char[] chars;

        /**
         * Constructor that creates a matcher from a character array.
         *
         * @param chars the characters to match, must not be null
         */
        CharSetMatcher(final char[] chars) {
            super();
            this.chars = chars.clone();
            Arrays.sort(this.chars);
        }

        /**
         * Returns whether or not the given character matches.
         *
         * @param buffer      the text content to match against, do not change
         * @param pos         the starting position for the match, valid for buffer
         * @param bufferStart the first active index in the buffer, valid for buffer
         * @param bufferEnd   the end index of the active buffer, valid for buffer
         * @return the number of matching characters, zero for no match
         */
        @Override
        public int isMatch(final char[] buffer, final int pos, final int bufferStart, final int bufferEnd) {
            return Arrays.binarySearch(chars, buffer[pos]) >= 0 ? 1 : 0;
        }
    }

    /**
     * Class used to define a character for matching purposes.
     */
    static final class CharMatcher extends StrMatcher {
        /**
         * The character to match.
         */
        private final char ch;

        /**
         * Constructor that creates a matcher that matches a single character.
         *
         * @param ch the character to match
         */
        CharMatcher(final char ch) {
            super();
            this.ch = ch;
        }

        /**
         * Returns whether or not the given character matches.
         *
         * @param buffer      the text content to match against, do not change
         * @param pos         the starting position for the match, valid for buffer
         * @param bufferStart the first active index in the buffer, valid for buffer
         * @param bufferEnd   the end index of the active buffer, valid for buffer
         * @return the number of matching characters, zero for no match
         */
        @Override
        public int isMatch(final char[] buffer, final int pos, final int bufferStart, final int bufferEnd) {
            return ch == buffer[pos] ? 1 : 0;
        }
    }

    /**
     * Class used to define a set of characters for matching purposes.
     */
    static final class StringMatcher extends StrMatcher {
        /**
         * The string to match, as a character array.
         */
        private final char[] chars;

        /**
         * Constructor that creates a matcher from a String.
         *
         * @param str the string to match, must not be null
         */
        StringMatcher(final String str) {
            super();
            chars = str.toCharArray();
        }

        /**
         * Returns whether or not the given text matches the stored string.
         *
         * @param buffer      the text content to match against, do not change
         * @param pos         the starting position for the match, valid for buffer
         * @param bufferStart the first active index in the buffer, valid for buffer
         * @param bufferEnd   the end index of the active buffer, valid for buffer
         * @return the number of matching characters, zero for no match
         */
        @Override
        public int isMatch(final char[] buffer, int pos, final int bufferStart, final int bufferEnd) {
            final int len = chars.length;
            if (pos + len > bufferEnd) {
                return 0;
            }
            for (int i = 0; i < chars.length; i++, pos++) {
                if (chars[i] != buffer[pos]) {
                    return 0;
                }
            }
            return len;
        }

        @Override
        public String toString() {
            return super.toString() + ' ' + Arrays.toString(chars);
        }

    }

    /**
     * Class used to match no characters.
     */
    static final class NoMatcher extends StrMatcher {

        /**
         * Constructs a new instance of <code>NoMatcher</code>.
         */
        NoMatcher() {
            super();
        }

        /**
         * Always returns <code>false</code>.
         *
         * @param buffer      the text content to match against, do not change
         * @param pos         the starting position for the match, valid for buffer
         * @param bufferStart the first active index in the buffer, valid for buffer
         * @param bufferEnd   the end index of the active buffer, valid for buffer
         * @return the number of matching characters, zero for no match
         */
        @Override
        public int isMatch(final char[] buffer, final int pos, final int bufferStart, final int bufferEnd) {
            return 0;
        }
    }

    /**
     * Class used to match whitespace as per trim().
     */
    static final class TrimMatcher extends StrMatcher {

        /**
         * Constructs a new instance of <code>TrimMatcher</code>.
         */
        TrimMatcher() {
            super();
        }

        /**
         * Returns whether or not the given character matches.
         *
         * @param buffer      the text content to match against, do not change
         * @param pos         the starting position for the match, valid for buffer
         * @param bufferStart the first active index in the buffer, valid for buffer
         * @param bufferEnd   the end index of the active buffer, valid for buffer
         * @return the number of matching characters, zero for no match
         */
        @Override
        public int isMatch(final char[] buffer, final int pos, final int bufferStart, final int bufferEnd) {
            return buffer[pos] <= 32 ? 1 : 0;
        }
    }

}
