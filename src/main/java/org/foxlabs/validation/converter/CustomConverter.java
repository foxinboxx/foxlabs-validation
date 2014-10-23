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

import org.foxlabs.validation.ValidationContext;

/**
 * This class provides <code>Converter</code> implementation for custom
 * converters.
 * 
 * @author Fox Mulder
 */
public final class CustomConverter<V> implements Converter<V> {
    
    /**
     * Composite converter.
     */
    private final Converter<V> converter;
    
    /**
     * Constructs a new <code>CustomConverter</code> with the specified
     * composite converter.
     * 
     * @param converter Composite converter.
     */
    CustomConverter(Converter<V> converter) {
        this.converter = converter;
    }
    
    /**
     * The default behavior of this method is to return
     * {@link Converter#getType()} on the composite converter.
     * 
     * @see Converter#getType()
     */
    @Override
    public Class<V> getType() {
        return converter.getType();
    }
    
    /**
     * The default behavior of this method is to return
     * {@link Converter#getMessageTemplate(ValidationContext)} on the composite
     * converter.
     * 
     * @see Converter#getMessageTemplate(ValidationContext)
     */
    @Override
    public String getMessageTemplate(ValidationContext<?> context) {
        return converter.getMessageTemplate(context);
    }
    
    /**
     * The default behavior of this method is to return
     * {@link Converter#appendMessageArguments(ValidationContext, Map)} on the
     * composite converter.
     * 
     * @see Converter#appendMessageArguments(ValidationContext, Map)
     */
    @Override
    public boolean appendMessageArguments(ValidationContext<?> context, Map<String, Object> arguments) {
        return converter.appendMessageArguments(context, arguments);
    }
    
    /**
     * The default behavior of this method is to return
     * {@link Converter#decode(String, ValidationContext)} on the composite
     * converter.
     * 
     * @see Converter#decode(String, ValidationContext)
     */
    @Override
    public <T> V decode(String value, ValidationContext<T> context) {
        try {
            return converter.decode(value, context);
        } catch (MalformedValueException e) {
            throw new MalformedValueException(this, context, value);
        }
    }
    
    /**
     * The default behavior of this method is to return
     * {@link Converter#encode(Object, ValidationContext)} on the composite
     * converter.
     * 
     * @see Converter#encode(Object, ValidationContext)
     */
    @Override
    public <T> String encode(V value, ValidationContext<T> context) {
        return converter.encode(value, context);
    }
    
}
