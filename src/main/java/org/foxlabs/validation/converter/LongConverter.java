/*
 * Copyright (C) 2012 FoxLabs
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.foxlabs.validation.converter;

import org.foxlabs.common.Checks;

/**
 * This class provides <code>NumberConverter</code> implementation for the
 * <code>java.lang.Long</code> type.
 *
 * @author Fox Mulder
 * @see NumberPattern
 * @see ConverterFactory#forLong(String)
 */
public final class LongConverter extends NumberConverter.IntegerType<Long> {

    /**
     * <code>LongConverter</code> primitive type instance.
     */
    public static final LongConverter SIMPLE = new LongConverter(Long.TYPE, null);

    /**
     * <code>LongConverter</code> object type instance.
     */
    public static final LongConverter OBJECT = new LongConverter(Long.class, null);

    /**
     * Long type (can be either primitive or object type).
     */
    private final Class<Long> type;

    /**
     * Constructs a new <code>LongConverter</code> with the specified type and
     * pattern.
     *
     * @param type Long type.
     * @param pattern <code>java.text.DecimalFormat</code> pattern or
     *        <code>null</code> if context pattern should be used.
     * @throws IllegalArgumentException if the specified type is
     *         <code>null</code> or the specified pattern is invalid.
     */
    LongConverter(Class<Long> type, String pattern) {
        super(pattern);
        this.type = Checks.checkNotNull(type, "type");
    }

    /**
     * Constructs a new <code>LongConverter</code> from the specified
     * annotation and type.
     *
     * @param annotation Converter annotation.
     * @param type Long type.
     * @throws IllegalArgumentException if the specified annotation defines
     *         invalid pattern.
     */
    LongConverter(NumberPattern annotation, Class<Long> type) {
        this(type, annotation.value());
    }

    /**
     * Returns <code>java.lang.Long</code> type.
     *
     * @return <code>java.lang.Long</code> type.
     */
    @Override
    public Class<Long> getType() {
        return type;
    }

    /**
     * Converts default string representation of long value into
     * <code>java.lang.Long</code> object.
     *
     * @param value Default string representation of long value.
     * @return Decoded <code>java.lang.Long</code> object.
     * @throws NumberFormatException if the specified string could not be
     *         parsed as an integer or value is out of range.
     */
    @Override
    protected Long doDecodeDefault(String value) {
        return Long.valueOf(value);
    }

}
