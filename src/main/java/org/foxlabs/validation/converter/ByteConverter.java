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
 * <code>java.lang.Byte</code> type.
 *
 * @author Fox Mulder
 * @see NumberPattern
 * @see ConverterFactory#forByte(String)
 */
public final class ByteConverter extends NumberConverter.IntegerType<Byte> {

    /**
     * <code>ByteConverter</code> primitive type instance.
     */
    public static final ByteConverter SIMPLE = new ByteConverter(Byte.TYPE, null);

    /**
     * <code>ByteConverter</code> object type instance.
     */
    public static final ByteConverter OBJECT = new ByteConverter(Byte.class, null);

    /**
     * Byte type (can be either primitive or object type).
     */
    private final Class<Byte> type;

    /**
     * Constructs a new <code>ByteConverter</code> with the specified type and
     * pattern.
     *
     * @param type Byte type.
     * @param pattern <code>java.text.DecimalFormat</code> pattern or
     *        <code>null</code> if context pattern should be used.
     * @throws IllegalArgumentException if the specified type is
     *         <code>null</code> or the specified pattern is invalid.
     */
    ByteConverter(Class<Byte> type, String pattern) {
        super(pattern);
        this.type = Predicates.requireNonNull(type, "type");
    }

    /**
     * Constructs a new <code>ByteConverter</code> from the specified
     * annotation and type.
     *
     * @param annotation Converter annotation.
     * @param type Byte type.
     * @throws IllegalArgumentException if the specified annotation defines
     *         invalid pattern.
     */
    ByteConverter(NumberPattern annotation, Class<Byte> type) {
        this(type, annotation.value());
    }

    /**
     * Returns <code>java.lang.Byte</code> type.
     *
     * @return <code>java.lang.Byte</code> type.
     */
    @Override
    public Class<Byte> getType() {
        return type;
    }

    /**
     * Converts default string representation of byte value into
     * <code>java.lang.Byte</code> object.
     *
     * @param value Default string representation of byte value.
     * @return Decoded <code>java.lang.Byte</code> object.
     * @throws NumberFormatException if the specified string could not be
     *         parsed as an integer or value is out of range.
     */
    @Override
    protected Byte doDecodeDefault(String value) {
        return Byte.valueOf(value);
    }

}
