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


import java.util.Map;

/**
 * Lookup a String key to a String value.
 * <p>
 * This class represents the simplest form of a string to string map.
 * It has a benefit over a map in that it can create the result on
 * demand based on the key.
 * <p>
 * This class comes complete with various factory methods.
 * If these do not suffice, you can subclass and implement your own matcher.
 * <p>
 * For example, it would be possible to implement a lookup that used the
 * key as a primary key, and looked up the value on demand from the database
 *
 * @author Kimi Liu
 * @version 3.0.5
 * @since JDK 1.8
 */
public abstract class StrLookup<V> {

    /**
     * Lookup that always returns null.
     */
    private static final StrLookup<String> NONE_LOOKUP = new MapStrLookup<String>(null);

    /**
     * Lookup based on system properties.
     */
    private static final StrLookup<String> SYSTEM_PROPERTIES_LOOKUP = new SystemPropertiesStrLookup();

    /**
     * Constructor.
     */
    protected StrLookup() {
        super();
    }

    /**
     * Returns a lookup which always returns null.
     *
     * @return a lookup that always returns null, not null
     */
    public static StrLookup<?> noneLookup() {
        return NONE_LOOKUP;
    }

    /**
     * Returns a new lookup which uses a copy of the current
     * {@link System#getProperties() System properties}.
     * <p>
     * If a security manager blocked access to system properties, then null will
     * be returned from every lookup.
     * <p>
     * If a null key is used, this lookup will throw a NullPointerException.
     *
     * @return a lookup using system properties, not null
     */
    public static StrLookup<String> systemPropertiesLookup() {
        return SYSTEM_PROPERTIES_LOOKUP;
    }

    /**
     * Returns a lookup which looks up values using a map.
     * <p>
     * If the map is null, then null will be returned from every lookup.
     * The map result object is converted to a string using toString().
     *
     * @param <V> the type of the values supported by the lookup
     * @param map the map of keys to values, may be null
     * @return a lookup using the map, not null
     */
    public static <V> StrLookup<V> mapLookup(final Map<String, V> map) {
        return new MapStrLookup<V>(map);
    }

    /**
     * Looks up a String key to a String value.
     * <p>
     * The internal implementation may use any mechanism to return the value.
     * The simplest implementation is to use a Map. However, virtually any
     * implementation is possible.
     * <p>
     * For example, it would be possible to implement a lookup that used the
     * key as a primary key, and looked up the value on demand from the database
     * Or, a numeric based implementation could be created that treats the key
     * as an integer, increments the value and return the result as a string -
     * converting 1 to 2, 15 to 16 etc.
     * <p>
     * The {@link #lookup(String)} method always returns a String, regardless of
     * the underlying data, by converting it as necessary. For example:
     * <pre>
     * Map&lt;String, Object&gt; map = new HashMap&lt;String, Object&gt;();
     * map.put("number", Integer.valueOf(2));
     * assertEquals("2", StrLookup.mapLookup(map).lookup("number"));
     * </pre>
     *
     * @param key the key to be looked up, may be null
     * @return the matching value, null if no match
     */
    public abstract String lookup(String key);

    /**
     * Lookup implementation that uses a Map.
     */
    static class MapStrLookup<V> extends StrLookup<V> {

        /**
         * Map keys are variable names and value.
         */
        private final Map<String, V> map;

        /**
         * Creates a new instance backed by a Map.
         *
         * @param map the map of keys to values, may be null
         */
        MapStrLookup(final Map<String, V> map) {
            this.map = map;
        }

        /**
         * Looks up a String key to a String value using the map.
         * <p>
         * If the map is null, then null is returned.
         * The map result object is converted to a string using toString().
         *
         * @param key the key to be looked up, may be null
         * @return the matching value, null if no match
         */
        @Override
        public String lookup(final String key) {
            if (map == null) {
                return null;
            }
            final Object obj = map.get(key);
            if (obj == null) {
                return null;
            }
            return obj.toString();
        }
    }

    /**
     * Lookup implementation based on system properties.
     */
    private static class SystemPropertiesStrLookup extends StrLookup<String> {
        /**
         * {@inheritDoc} This implementation directly accesses system properties.
         */
        @Override
        public String lookup(String key) {
            if (key.length() > 0) {
                try {
                    return System.getProperty(key);
                } catch (SecurityException scex) {
                    // Squelched. All lookup(String) will return null.
                }
            }
            return null;
        }
    }
}
