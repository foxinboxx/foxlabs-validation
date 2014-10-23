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

import java.math.BigInteger;

/**
 * This class provides <code>NumberConverter</code> implementation for the
 * <code>java.math.BigInteger</code> type.
 * 
 * @author Fox Mulder
 * @see NumberPattern
 * @see ConverterFactory#forBigInteger(String)
 */
public final class BigIntegerConverter extends NumberConverter.IntegerType<BigInteger> {
    
    /**
     * <code>BigIntegerConverter</code> default instance.
     */
    public static final BigIntegerConverter DEFAULT = new BigIntegerConverter((String) null);
    
    /**
     * Constructs a new <code>BigIntegerConverter</code> with the specified
     * pattern.
     * 
     * @param pattern <code>java.text.DecimalFormat</code> pattern or
     *        <code>null</code> if context pattern should be used.
     * @throws IllegalArgumentException if the specified pattern is invalid.
     */
    BigIntegerConverter(String pattern) {
        super(pattern);
    }
    
    /**
     * Constructs a new <code>BigIntegerConverter</code> from the specified
     * annotation.
     * 
     * @param annotation Converter annotation.
     * @throws IllegalArgumentException if the specified annotation defines
     *         invalid pattern.
     */
    BigIntegerConverter(NumberPattern annotation) {
        this(annotation.value());
    }
    
    /**
     * Returns <code>java.math.BigInteger</code> type.
     * 
     * @return <code>java.math.BigInteger</code> type.
     */
    @Override
    public Class<BigInteger> getType() {
        return BigInteger.class;
    }
    
    /**
     * Converts default string representation of integer value into
     * <code>java.math.BigInteger</code> object.
     * 
     * @param value Default string representation of integer value.
     * @return Decoded <code>java.math.BigInteger</code> object.
     * @throws NumberFormatException if the specified string could not be
     *         parsed as an integer.
     */
    @Override
    protected BigInteger doDecodeDefault(String value) {
        return new BigInteger(value);
    }
    
}
