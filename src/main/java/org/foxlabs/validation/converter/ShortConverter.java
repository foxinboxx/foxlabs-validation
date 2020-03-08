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

import org.foxlabs.common.Predicates;

/**
 * This class provides <code>NumberConverter</code> implementation for the
 * <code>java.lang.Short</code> type.
 *
 * @author Fox Mulder
 * @see NumberPattern
 * @see ConverterFactory#forShort(String)
 */
public final class ShortConverter extends NumberConverter.IntegerType<Short> {

    /**
     * <code>ShortConverter</code> primitive type instance.
     */
    public static final ShortConverter SIMPLE = new ShortConverter(Short.TYPE, null);

    /**
     * <code>ShortConverter</code> object type instance.
     */
    public static final ShortConverter OBJECT = new ShortConverter(Short.class, null);

    /**
     * Short type (can be either primitive or object type).
     */
    private final Class<Short> type;

    /**
     * Constructs a new <code>ShortConverter</code> with the specified type and
     * pattern.
     *
     * @param type Short type.
     * @param pattern <code>java.text.DecimalFormat</code> pattern or
     *        <code>null</code> if context pattern should be used.
     * @throws IllegalArgumentException if the specified type is
     *         <code>null</code> or the specified pattern is invalid.
     */
    ShortConverter(Class<Short> type, String pattern) {
        super(pattern);
        this.type = Predicates.requireNonNull(type, "type");
    }

    /**
     * Constructs a new <code>ShortConverter</code> from the specified
     * annotation and type.
     *
     * @param annotation Converter annotation.
     * @param type Short type.
     * @throws IllegalArgumentException if the specified annotation defines
     *         invalid pattern.
     */
    ShortConverter(NumberPattern annotation, Class<Short> type) {
        this(type, annotation.value());
    }

    /**
     * Returns <code>java.lang.Short</code> type.
     *
     * @return <code>java.lang.Short</code> type.
     */
    @Override
    public Class<Short> getType() {
        return type;
    }

    /**
     * Converts default string representation of short value into
     * <code>java.lang.Short</code> object.
     *
     * @param value Default string representation of short value.
     * @return Decoded <code>java.lang.Short</code> object.
     * @throws NumberFormatException if the specified string could not be
     *         parsed as an integer or value is out of range.
     */
    @Override
    protected Short doDecodeDefault(String value) {
        return Short.valueOf(value);
    }

}
