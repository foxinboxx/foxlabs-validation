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

import org.foxlabs.validation.Validation;
import org.foxlabs.validation.ValidationContext;

/**
 * The root interface in the converter hierarchy. A converter provides ability
 * to convert values of certain data type into and from string representation.
 * <code>Converter</code> instances should be registered and obtained from
 * <code>ConverterFactory</code>.
 * 
 * @author Fox Mulder
 * @param <V> The type of value to be converted
 * @see ConverterFactory
 */
public interface Converter<V> extends Validation<V> {
    
    /**
     * Returns the type of value to be converted.
     * 
     * @return The type of value to be converted.
     */
    Class<V> getType();
    
    /**
     * Returns localized error message template.
     * 
     * <p>This method may return <code>null</code> if converter never generates
     * <code>MalformedValueException</code>.
     * 
     * @param context Validation context.
     * @return Localized error message template.
     */
    String getMessageTemplate(ValidationContext<?> context);
    
    /**
     * Appends arguments to be substituted into the error message template.
     * 
     * @param context Validation context.
     * @param arguments Arguments to be substituted into the error message
     *        template.
     */
    boolean appendMessageArguments(ValidationContext<?> context, Map<String, Object> arguments);
    
    /**
     * Converts string representation of value into object.
     * 
     * <p>The method {@link ValidationContext#isLocalizedConvert()} determines
     * if converter should take locale into account. Locale can be obtained
     * through the method {@link ValidationContext#getMessageLocale()}.</p>
     * 
     * @param <T> The type of validated entity.
     * @param value String representation of value.
     * @param context Validation context.
     * @return Decoded value.
     * @throws MalformedValueException if conversion fails.
     */
    <T> V decode(String value, ValidationContext<T> context);
    
    /**
     * Converts value into string representation.
     * 
     * <p>The method {@link ValidationContext#isLocalizedConvert()} determines
     * if converter should take locale into account. Locale can be obtained
     * through the method {@link ValidationContext#getMessageLocale()}.</p>
     * 
     * @param <T> The type of validated entity.
     * @param value Value to be encoded.
     * @param context Validation context.
     * @return String representation of value.
     */
    <T> String encode(V value, ValidationContext<T> context);
    
}
