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

import java.util.Map;

import java.text.NumberFormat;
import java.text.DecimalFormat;
import java.text.ParsePosition;

import org.foxlabs.validation.ValidationContext;

/**
 * This class provides base <code>Converter</code> implementation for all
 * <code>java.lang.Number</code> types.
 * 
 * @author Fox Mulder
 * @param <V> The type of numeric value
 */
public abstract class NumberConverter<V extends Number> extends AbstractConverter<V> {
    
    /**
     * <code>java.text.DecimalFormat</code> number pattern.
     */
    protected final String pattern;
    
    /**
     * Constructs a new <code>NumberConverter</code> with the specified number
     * pattern.
     * 
     * @param pattern <code>java.text.DecimalFormat</code> number pattern or
     *        <code>null</code> if context pattern should be used.
     * @throws IllegalArgumentException if the specified pattern is invalid.
     */
    protected NumberConverter(String pattern) {
        this.pattern = pattern == null ? null : new DecimalFormat(pattern).toPattern();
    }
    
    /**
     * In case of localized conversion appends <code>pattern</code> argument
     * that contains localized number pattern.
     * 
     * @param context Validation context.
     * @param arguments Arguments to be substituted into the error message
     *        template.
     * @return <code>true</code>.
     */
    @Override
    public boolean appendMessageArguments(ValidationContext<?> context, Map<String, Object> arguments) {
        super.appendMessageArguments(context, arguments);
        if (context.isLocalizedConvert()) {
            NumberFormat format = getFormat(context);
            if (format instanceof DecimalFormat)
                arguments.put("pattern", ((DecimalFormat) format).toLocalizedPattern());
        }
        return true;
    }
    
    /**
     * Converts string representation of numeric value into
     * <code>java.lang.Number</code> object.
     * 
     * @param value String representation of numeric value.
     * @param context Validation context.
     * @return Decoded <code>java.lang.Number</code> object.
     * @throws MalformedValueException if the specified string could not be
     *         parsed as a number.
     * @see #doDecodeDefault(String)
     */
    @Override
    protected <T> V doDecode(String value, ValidationContext<T> context) {
        try {
            if (context.isLocalizedConvert()) {
                ParsePosition pos = new ParsePosition(0);
                Number number = getFormat(context).parse(value, pos);
                if (pos.getIndex() == value.length())
                    return doDecodeDefault(number.toString());
            } else {
                return doDecodeDefault(value);
            }
        } catch (NumberFormatException e) {}
        throw new MalformedValueException(this, context, value);
    }
    
    /**
     * Converts default string representation of numeric value into
     * <code>java.lang.Number</code> object.
     * 
     * @param value Default string representation of numeric value.
     * @return Decoded <code>java.lang.Number</code> object.
     * @throws NumberFormatException if the specified string could not be
     *         parsed as a number.
     */
    protected abstract V doDecodeDefault(String value);
    
    /**
     * Converts <code>java.lang.Number</code> object into string representation.
     * 
     * @param value <code>java.lang.Number</code> object to be encoded.
     * @param context Validation context.
     * @return String representation of numeric value.
     * @see #doEncodeDefault(Number)
     */
    @Override
    protected <T> String doEncode(V value, ValidationContext<T> context) {
        return context.isLocalizedConvert() ? getFormat(context).format(value) : doEncodeDefault(value);
    }
    
    /**
     * Converts <code>java.lang.Number</code> object into default string
     * representation.
     * 
     * @param value <code>java.lang.Number</code> object to be encoded.
     * @return Default string representation of numeric value.
     */
    protected String doEncodeDefault(V value) {
        return value.toString();
    }
    
    /**
     * Returns number format for the specified context and this converter
     * configuration.
     * 
     * @param context Validation context.
     * @return Number format for the specified context and this converter
     *         configuration.
     */
    protected abstract NumberFormat getFormat(ValidationContext<?> context);
    
    // IntegerType
    
    /**
     * This class provides base <code>NumberConverter</code> implementation for
     * all integer types.
     * 
     * @author Fox Mulder
     * @param <V> The type of integer value
     */
    public abstract static class IntegerType<V extends Number> extends NumberConverter<V> {
        
        /**
         * Constructs a new <code>NumberConverter.IntegerType</code> with the
         * specified number pattern.
         * 
         * @param pattern Integer number pattern or <code>null</code> if
         *        context pattern should be used.
         * @throws IllegalArgumentException if the specified pattern is invalid.
         */
        protected IntegerType(String pattern) {
            super(pattern);
        }
        
        /**
         * Returns integer number format for the specified context and this
         * converter configuration.
         * 
         * @param context Validation context.
         * @return Integer number format for the specified context and this
         *         converter configuration.
         */
        @Override
        protected NumberFormat getFormat(ValidationContext<?> context) {
            return context.getIntegerFormat(pattern);
        }
        
    }
    
    // DecimalType
    
    /**
     * This class provides base <code>NumberConverter</code> implementation for
     * all decimal types.
     * 
     * @author Fox Mulder
     * @param <V> The type of decimal value
     */
    public abstract static class DecimalType<V extends Number> extends NumberConverter<V> {
        
        /**
         * Constructs a new <code>NumberConverter.DecimalType</code> with the
         * specified number pattern.
         * 
         * @param pattern Decimal number pattern or <code>null</code> if
         *        context pattern should be used.
         * @throws IllegalArgumentException if the specified pattern is invalid.
         */
        protected DecimalType(String pattern) {
            super(pattern);
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
            return context.getDecimalFormat(pattern);
        }
        
    }
    
}
