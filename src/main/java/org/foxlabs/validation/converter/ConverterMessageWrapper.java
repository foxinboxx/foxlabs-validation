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

import java.util.MissingResourceException;

import org.foxlabs.validation.ValidationContext;

import org.foxlabs.util.Assert;

/**
 * This class provides ability to override error message of another converter.
 * Any converter annotations can define <code>message</code> property of the
 * <code>java.lang.String</code> type.
 * 
 * @author Fox Mulder
 * @param <V> The type of value to be converted
 * @see ConverterFactory#wrapMessage(Converter, String)
 */
public final class ConverterMessageWrapper<V> extends ConverterWrapper<V> {
    
    /**
     * Wrapped error message template key.
     */
    private final String message;
    
    /**
     * Constructs a new <code>ConverterMessageWrapper</code> with the specified
     * converter and error message template.
     * 
     * @param converter Converter to be wrapped.
     * @param message Wrapped error message template key.
     * @throws IllegalArgumentException if the specified converter is
     *         <code>null</code> or the specified message template key is
     *         <code>null</code> or empty.
     */
    ConverterMessageWrapper(Converter<V> converter, String message) {
        super(converter);
        this.message = Assert.notEmpty(message, "message");
    }
    
    /**
     * Returns wrapped error message template.
     * 
     * @param context Validation context.
     * @return Wrapped error message template.
     */
    @Override
    public String getMessageTemplate(ValidationContext<?> context) {
        try {
            return context.resolveMessage(message);
        } catch (MissingResourceException e) {
            return converter.getMessageTemplate(context);
        }
    }
    
}
