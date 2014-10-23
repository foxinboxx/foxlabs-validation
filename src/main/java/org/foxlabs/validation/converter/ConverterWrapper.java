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

import org.foxlabs.util.Assert;

/**
 * This class provides convenient implementation of the <code>Converter</code>
 * that can be subclassed to wrap another converter.
 * 
 * @author Fox Mulder
 * @param <V> The type of value to be converted
 */
public abstract class ConverterWrapper<V> implements Converter<V> {
    
    /**
     * Wrapped converter.
     */
    protected final Converter<V> converter;
    
    /**
     * Constructs a new <code>ConverterWrapper</code> with the specified
     * converter.
     * 
     * @param converter Converter to be wrapped.
     * @throws IllegalArgumentException if the specified converter is
     *         <code>null</code>.
     */
    public ConverterWrapper(Converter<V> converter) {
        this.converter = Assert.notNull(converter, "converter");
    }
    
    /**
     * Returns wrapped converter.
     * 
     * @return Wrapped converter.
     */
    public final Converter<V> getConverter() {
        return converter;
    }
    
    /**
     * The default behavior of this method is to return
     * {@link Converter#getType()} on the wrapped converter.
     * 
     * @see Converter#getType()
     */
    @Override
    public Class<V> getType() {
        return converter.getType();
    }
    
    /**
     * The default behavior of this method is to return
     * {@link Converter#getMessageTemplate(ValidationContext)} on the wrapped
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
     * wrapped converter.
     * 
     * @see Converter#appendMessageArguments(ValidationContext, Map)
     */
    @Override
    public boolean appendMessageArguments(ValidationContext<?> context, Map<String, Object> arguments) {
        return converter.appendMessageArguments(context, arguments);
    }
    
    /**
     * The default behavior of this method is to return
     * {@link Converter#decode(String, ValidationContext)} on the wrapped
     * converter.
     * 
     * @see Converter#decode(String, ValidationContext)
     */
    @Override
    public <T> V decode(String value, ValidationContext<T> context) {
        try {
            return converter.decode(value, context);
        } catch (MalformedValueException e) {
            throw new MalformedValueException(this, context, value, e);
        }
    }
    
    /**
     * The default behavior of this method is to return
     * {@link Converter#encode(Object, ValidationContext)} on the wrapped
     * converter.
     * 
     * @see Converter#encode(Object, ValidationContext)
     */
    @Override
    public <T> String encode(V value, ValidationContext<T> context) {
        return converter.encode(value, context);
    }
    
}
