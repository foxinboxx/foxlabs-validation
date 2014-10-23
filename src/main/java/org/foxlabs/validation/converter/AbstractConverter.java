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

import org.foxlabs.validation.AbstractValidation;
import org.foxlabs.validation.ValidationContext;

/**
 * This class provides base implementation of the <code>Converter</code>
 * interface.
 * 
 * @author Fox Mulder
 * @param <V> The type of value to be converted
 */
public abstract class AbstractConverter<V> extends AbstractValidation<V> implements Converter<V> {
    
    /**
     * Converts string representation of value into object.
     * 
     * <p>Invokes {@link #doDecode(String, ValidationContext)} if value is not
     * <code>null</code> or empty; returns <code>null</code> otherwise.</p>
     * 
     * @param value String representation of value.
     * @param context Validation context.
     * @return Decoded value.
     * @throws MalformedValueException if conversion fails.
     * @see #doDecode(String, ValidationContext)
     */
    @Override
    public <T> V decode(String value, ValidationContext<T> context) {
        return value == null || (value = value.trim()).isEmpty() ? null : doDecode(value, context);
    }
    
    /**
     * Converts non-empty string representation of value into object.
     * 
     * <p>This method always throws <code>MalformedValueException</code> and
     * should be overriden in subclasses.</p>
     * 
     * @param value Non-empty string representation of value.
     * @param context Validation context.
     * @throws MalformedValueException.
     */
    protected <T> V doDecode(String value, ValidationContext<T> context) {
        throw new MalformedValueException(this, context, value); 
    }
    
    /**
     * Converts value into string representation.
     * 
     * <p>Invokes {@link #doEncode(Object, ValidationContext)} if value is not
     * <code>null</code>; returns empty string otherwise.</p>
     * 
     * @param value Value to be encoded.
     * @param context Validation context.
     * @return String representation of value.
     * @see #doEncode(Object, ValidationContext)
     */
    @Override
    public <T> String encode(V value, ValidationContext<T> context) {
        return value == null ? "" : doEncode(value, context);
    }
    
    /**
     * Converts null-safe value into string representation.
     * 
     * <p>This method simply returns <code>value.toString()</code> and could
     * be overriden in subclasses.</p>
     * 
     * @param value Null-safe value to be encoded.
     * @param context Validation context.
     * @return String representation of value.
     */
    protected <T> String doEncode(V value, ValidationContext<T> context) {
        return value.toString();
    }
    
}
