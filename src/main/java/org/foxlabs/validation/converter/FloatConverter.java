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

import org.foxlabs.util.Assert;

/**
 * This class provides <code>NumberConverter</code> implementation for the
 * <code>java.lang.Float</code> type.
 * 
 * @author Fox Mulder
 * @see NumberPattern
 * @see ConverterFactory#forFloat(String)
 */
public final class FloatConverter extends NumberConverter.DecimalType<Float> {
    
    /**
     * <code>FloatConverter</code> primitive type instance.
     */
    public static final FloatConverter SIMPLE = new FloatConverter(Float.TYPE, null);
    
    /**
     * <code>FloatConverter</code> object type instance.
     */
    public static final FloatConverter OBJECT = new FloatConverter(Float.class, null);
    
    /**
     * Float type (can be either primitive or object type).
     */
    private final Class<Float> type;
    
    /**
     * Constructs a new <code>FloatConverter</code> with the specified type and
     * pattern.
     * 
     * @param type Float type.
     * @param pattern <code>java.text.DecimalFormat</code> pattern or
     *        <code>null</code> if context pattern should be used.
     * @throws IllegalArgumentException if the specified type is
     *         <code>null</code> or the specified pattern is invalid.
     */
    FloatConverter(Class<Float> type, String pattern) {
        super(pattern);
        this.type = Assert.notNull(type, "type");
    }
    
    /**
     * Constructs a new <code>FloatConverter</code> from the specified
     * annotation and type.
     * 
     * @param annotation Converter annotation.
     * @param type Float type.
     * @throws IllegalArgumentException if the specified annotation defines
     *         invalid pattern.
     */
    FloatConverter(NumberPattern annotation, Class<Float> type) {
        this(type, annotation.value());
    }
    
    /**
     * Returns <code>java.lang.Float</code> type.
     * 
     * @return <code>java.lang.Float</code> type.
     */
    @Override
    public Class<Float> getType() {
        return type;
    }
    
    /**
     * Converts default string representation of float value into
     * <code>java.lang.Float</code> object.
     * 
     * @param value Default string representation of float value.
     * @return Decoded <code>java.lang.Float</code> object.
     * @throws NumberFormatException if the specified string could not be
     *         parsed as a decimal or value is out of range.
     */
    @Override
    protected Float doDecodeDefault(String value) {
        return Float.valueOf(value);
    }
    
}
