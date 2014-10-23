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
import java.util.Date;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.text.ParsePosition;

import org.foxlabs.validation.ValidationContext;

/**
 * This class provides <code>Converter</code> implementation for the
 * <code>java.util.Date</code> type.
 * 
 * @author Fox Mulder
 * @see DatePattern
 * @see DateStyle
 * @see ConverterFactory#forDate(String)
 * @see ConverterFactory#forDate(int, int)
 */
public class DateConverter extends AbstractConverter<Date> {
    
    /**
     * <code>DateConverter</code> default instance.
     */
    public static final DateConverter DEFAULT = new DateConverter();
    
    /**
     * <code>java.text.SimpleDateFormat</code> date/time pattern.
     */
    protected final String pattern;
    
    /**
     * <code>java.text.DateFormat</code> date style.
     */
    protected final int dateStyle;
    
    /**
     * <code>java.text.DateFormat</code> time style.
     */
    protected final int timeStyle;
    
    /**
     * Constructs default <code>DateConverter</code>.
     */
    protected DateConverter() {
        this.pattern = null;
        this.dateStyle = this.timeStyle = -1;
    }
    
    /**
     * Constructs a new <code>DateConverter</code> with the specified date/time
     * pattern.
     * 
     * @param pattern <code>java.text.SimpleDateFormat</code> date/time
     *        pattern.
     * @throws IllegalArgumentException if the specified pattern is
     *         <code>null</code> or invalid.
     */
    protected DateConverter(String pattern) {
        this.pattern = new SimpleDateFormat(pattern).toPattern();
        this.dateStyle = this.timeStyle = -1;
    }
    
    /**
     * Constructs a new <code>DateConverter</code> from the specified
     * annotation.
     * 
     * @param annotation Converter annotation.
     * @throws IllegalArgumentException if the specified annotation defines
     *         invalid pattern.
     */
    protected DateConverter(DatePattern annotation) {
        this(annotation.value());
    }
    
    /**
     * Constructs a new <code>DateConverter</code> with the specified date and
     * time styles.
     * 
     * @param dateStyle <code>java.text.DateFormat</code> date style.
     * @param timeStyle <code>java.text.DateFormat</code> time style.
     * @throws IllegalArgumentException if the specified date or time style
     *         is invalid.
     */
    protected DateConverter(int dateStyle, int timeStyle) {
        if (timeStyle < 0) {
            DateFormat.getDateInstance(dateStyle);
        } else if (dateStyle < 0) {
            DateFormat.getTimeInstance(timeStyle);
        } else {
            DateFormat.getDateTimeInstance(dateStyle, timeStyle);
        }
        this.pattern = null;
        this.dateStyle = dateStyle;
        this.timeStyle = timeStyle;
    }
    
    /**
     * Constructs a new <code>DateConverter</code> from the specified
     * annotation.
     * 
     * @param annotation Converter annotation.
     * @throws IllegalArgumentException if the specified annotation defines
     *         invalid date/time style.
     */
    protected DateConverter(DateStyle annotation) {
        this(annotation.date(), annotation.time());
    }
    
    /**
     * Returns <code>java.util.Date</code> type.
     * 
     * @return <code>java.util.Date</code> type.
     */
    @Override
    public final Class<Date> getType() {
        return Date.class;
    }
    
    /**
     * In case of localized conversion appends <code>pattern</code> argument
     * that contains localized date/time pattern.
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
            DateFormat format = getFormat(context);
            if (format instanceof SimpleDateFormat)
                arguments.put("pattern", ((SimpleDateFormat) format).toLocalizedPattern());
        }
        return true;
    }
    
    /**
     * Converts string representation of date/time into
     * <code>java.util.Date</code> object.
     * 
     * @param value String representation of date/time.
     * @param context Validation context.
     * @return Decoded <code>java.util.Date</code> object.
     * @throws MalformedValueException if the specified string could not be
     *         parsed as a date/time.
     */
    @Override
    protected <T> Date doDecode(String value, ValidationContext<T> context) {
        if (context.isLocalizedConvert()) {
            ParsePosition pos = new ParsePosition(0);
            Date date = getFormat(context).parse(value, pos);
            if (pos.getIndex() == value.length())
                return date;
        } else {
            try {
                return doDecodeDefault(value);
            } catch (IllegalArgumentException e) {}
        }
        throw new MalformedValueException(this, context, value);
    }
    
    /**
     * Converts default string representation of date/time into
     * <code>java.util.Date</code> object.
     * 
     * @param value Default string representation of date/time.
     * @return Decoded <code>java.util.Date</code> object.
     * @throws IllegalArgumentException if the specified string could not be
     *         parsed as a date/time.
     */
    @SuppressWarnings("deprecation")
    protected Date doDecodeDefault(String value) {
        return new Date(value);
    }
    
    /**
     * Converts <code>java.util.Date</code> object into string representation.
     * 
     * @param value <code>java.util.Date</code> object to be encoded.
     * @param context Validation context.
     * @return String representation of date/time.
     */
    @Override
    protected <T> String doEncode(Date value, ValidationContext<T> context) {
        return context.isLocalizedConvert() ? getFormat(context).format(value) : doEncodeDefault(value);
    }
    
    /**
     * Converts <code>java.util.Date</code> object into default string
     * representation.
     * 
     * @param value <code>java.util.Date</code> object to be encoded.
     * @return Default string representation of date/time.
     */
    protected String doEncodeDefault(Date value) {
        return value.toString();
    }
    
    /**
     * Returns date/time format for the specified context and this converter
     * configuration.
     * 
     * @param context Validation context.
     * @return Date/time format for the specified context and this converter
     *         configuration.
     */
    protected DateFormat getFormat(ValidationContext<?> context) {
        return pattern == null
            ? context.getDateFormat(dateStyle, timeStyle)
            : context.getDateFormat(pattern);
    }
    
}
