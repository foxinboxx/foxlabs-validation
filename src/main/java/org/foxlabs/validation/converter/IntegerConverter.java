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
 * <code>java.lang.Integer</code> type.
 *
 * @author Fox Mulder
 * @see NumberPattern
 * @see ConverterFactory#forInteger(String)
 */
public final class IntegerConverter extends NumberConverter.IntegerType<Integer> {

    /**
     * <code>IntegerConverter</code> primitive type instance.
     */
    public static final IntegerConverter SIMPLE = new IntegerConverter(Integer.TYPE, null);

    /**
     * <code>IntegerConverter</code> object type instance.
     */
    public static final IntegerConverter OBJECT = new IntegerConverter(Integer.class, null);

    /**
     * Integer type (can be either primitive or object type).
     */
    private final Class<Integer> type;

    /**
     * Constructs a new <code>IntegerConverter</code> with the specified type
     * and pattern.
     *
     * @param type Integer type.
     * @param pattern <code>java.text.DecimalFormat</code> pattern or
     *        <code>null</code> if context pattern should be used.
     * @throws IllegalArgumentException if the specified type is
     *         <code>null</code> or the specified pattern is invalid.
     */
    IntegerConverter(Class<Integer> type, String pattern) {
        super(pattern);
        this.type = Checks.checkNotNull(type, "type");
    }

    /**
     * Constructs a new <code>IntegerConverter</code> from the specified
     * annotation and type.
     *
     * @param annotation Converter annotation.
     * @param type Integer type.
     * @throws IllegalArgumentException if the specified annotation defines
     *         invalid pattern.
     */
    IntegerConverter(NumberPattern annotation, Class<Integer> type) {
        this(type, annotation.value());
    }

    /**
     * Returns <code>java.lang.Integer</code> type.
     *
     * @return <code>java.lang.Integer</code> type.
     */
    @Override
    public Class<Integer> getType() {
        return type;
    }

    /**
     * Converts default string representation of integer value into
     * <code>java.lang.Integer</code> object.
     *
     * @param value Default string representation of integer value.
     * @return Decoded <code>java.lang.Integer</code> object.
     * @throws NumberFormatException if the specified string could not be
     *         parsed as an integer or value is out of range.
     */
    @Override
    protected Integer doDecodeDefault(String value) {
        return Integer.valueOf(value);
    }

}
