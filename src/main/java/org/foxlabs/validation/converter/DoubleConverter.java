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
 * <code>java.lang.Double</code> type.
 *
 * @author Fox Mulder
 * @see NumberPattern
 * @see ConverterFactory#forDouble(String)
 */
public final class DoubleConverter extends NumberConverter.DecimalType<Double> {

    /**
     * <code>DoubleConverter</code> primitive type instance.
     */
    public static final DoubleConverter SIMPLE = new DoubleConverter(Double.TYPE, null);

    /**
     * <code>DoubleConverter</code> object type instance.
     */
    public static final DoubleConverter OBJECT = new DoubleConverter(Double.class, null);

    /**
     * Double type (can be either primitive or object type).
     */
    private final Class<Double> type;

    /**
     * Constructs a new <code>DoubleConverter</code> with the specified type
     * and pattern.
     *
     * @param type Double type.
     * @param pattern <code>java.text.DecimalFormat</code> pattern or
     *        <code>null</code> if context pattern should be used.
     * @throws IllegalArgumentException if the specified type is
     *         <code>null</code> or the specified pattern is invalid.
     */
    DoubleConverter(Class<Double> type, String pattern) {
        super(pattern);
        this.type = Predicates.requireNonNull(type, "type");
    }

    /**
     * Constructs a new <code>DoubleConverter</code> from the specified
     * annotation and type.
     *
     * @param annotation Converter annotation.
     * @param type Double type.
     * @throws IllegalArgumentException if the specified annotation defines
     *         invalid pattern.
     */
    DoubleConverter(NumberPattern annotation, Class<Double> type) {
        this(type, annotation.value());
    }

    /**
     * Returns <code>java.lang.Double</code> type.
     *
     * @return <code>java.lang.Double</code> type.
     */
    @Override
    public Class<Double> getType() {
        return type;
    }

    /**
     * Converts default string representation of double value into
     * <code>java.lang.Double</code> object.
     *
     * @param value Default string representation of double value.
     * @return Decoded <code>java.lang.Double</code> object.
     * @throws NumberFormatException if the specified string could not be
     *         parsed as a decimal or value is out of range.
     */
    @Override
    protected Double doDecodeDefault(String value) {
        return Double.valueOf(value);
    }

}
