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

import org.foxlabs.validation.AbstractValidation;
import org.foxlabs.validation.ValidationContext;

/**
 * This class provides <code>Converter</code> stub for any unsupported type.
 * 
 * @author Fox Mulder
 * @param <V> The unsupported type
 */
public final class UnsupportedConverter<V> extends AbstractValidation<V> implements Converter<V> {
    
    /**
     * Unsupported type.
     */
    private final Class<V> type;
    
    /**
     * Constructs a new <code>UnsupportedConverter</code> for the specified
     * type.
     * 
     * @param type Unsupported type.
     */
    UnsupportedConverter(Class<V> type) {
        this.type = type;
    }
    
    /**
     * Returns unsupported type.
     * 
     * @return Unsupported type.
     */
    @Override
    public Class<V> getType() {
        return type;
    }
    
    /**
     * Appends <code>type</code> argument that contains unsupported type.
     * 
     * @param context Validation context.
     * @param arguments Arguments to be substituted into the error message
     *        template.
     * @return <code>true</code>.
     */
    @Override
    public boolean appendMessageArguments(ValidationContext<?> context, Map<String, Object> arguments) {
        super.appendMessageArguments(context, arguments);
        arguments.put("type", type);
        return true;
    }
    
    /**
     * Throws <code>MalformedValueException</code>.
     * 
     * @param value String representation of value. 
     * @param context Validation context.
     * @throws MalformedValueException.
     */
    @Override
    public <T> V decode(String value, ValidationContext<T> context) {
        throw new MalformedValueException(this, context, value);
    }
    
    /**
     * Returns <code>value.toString()</code> if value is not <code>null</code>;
     * returns empty string otherwise.</p>.
     * 
     * @param value Value to be encoded.
     * @param context Validation context.
     * @return String representation of value.
     */
    @Override
    public <T> String encode(V value, ValidationContext<T> context) {
        return value == null ? "" : value.toString();
    }
    
}
