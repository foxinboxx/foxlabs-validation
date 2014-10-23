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

import java.math.BigDecimal;

import java.text.NumberFormat;
import java.text.DecimalFormat;

import org.foxlabs.validation.ValidationContext;

/**
 * This class provides <code>NumberConverter</code> implementation for the
 * <code>java.math.BigDecimal</code> type.
 * 
 * @author Fox Mulder
 * @see NumberPattern
 * @see ConverterFactory#forBigDecimal(String)
 */
public final class BigDecimalConverter extends NumberConverter.DecimalType<BigDecimal> {
    
    /**
     * <code>BigDecimalConverter</code> default instance.
     */
    public static final BigDecimalConverter DEFAULT = new BigDecimalConverter((String) null);
    
    /**
     * Constructs a new <code>BigDecimalConverter</code> with the specified
     * pattern.
     * 
     * @param pattern <code>java.text.DecimalFormat</code> pattern or
     *        <code>null</code> if context pattern should be used.
     * @throws IllegalArgumentException if the specified pattern is invalid.
     */
    BigDecimalConverter(String pattern) {
        super(pattern);
    }
    
    /**
     * Constructs a new <code>BigDecimalConverter</code> from the specified
     * annotation.
     * 
     * @param annotation Converter annotation.
     * @throws IllegalArgumentException if the specified annotation defines
     *         invalid pattern.
     */
    BigDecimalConverter(NumberPattern annotation) {
        this(annotation.value());
    }
    
    /**
     * Returns <code>java.math.BigDecimal</code> type.
     * 
     * @return <code>java.math.BigDecimal</code> type.
     */
    @Override
    public Class<BigDecimal> getType() {
        return BigDecimal.class;
    }
    
    /**
     * Converts default string representation of decimal value into
     * <code>java.math.BigDecimal</code> object.
     * 
     * @param value Default string representation of decimal value.
     * @return Decoded <code>java.math.BigDecimal</code> object.
     * @throws NumberFormatException if the specified string could not be
     *         parsed as a decimal.
     */
    @Override
    protected BigDecimal doDecodeDefault(String value) {
        return new BigDecimal(value);
    }
    
    /**
     * Returns decimal number format for the specified context and this
     * converter configuration.
     * 
     * @param context Validation context.
     * @return Decimal number format for the specified context and this
     *         converter configuration.
     */
    @Override
    protected NumberFormat getFormat(ValidationContext<?> context) {
        NumberFormat format = super.getFormat(context);
        if (format instanceof DecimalFormat) {
            if (pattern == null)
                format = (DecimalFormat) format.clone();
            ((DecimalFormat) format).setParseBigDecimal(true);
        }
        return format;
    }
    
}
