/* 
 * Copyright (C) 2013 FoxLabs
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

package org.foxlabs.validation.metadata;

import java.util.LinkedHashMap;

import org.foxlabs.validation.constraint.Constraint;
import org.foxlabs.validation.converter.Converter;
import org.foxlabs.validation.converter.ConverterFactory;

/**
 * This class provides base methods to build <code>EntityMetaData</code>
 * instances.
 * 
 * @author Fox Mulder
 * @param <T> The type of entity
 * @see MapMetaData.Builder
 */
public abstract class MetaDataBuilder<T> {
    
    /**
     * Constraint to be used for entity validation.
     */
    protected Constraint<? super T> constraint;
    
    /**
     * Metadata of all the properties defined on the entity.
     */
    protected final LinkedHashMap<String, PropertyMetaData<T, ?>> properties;
    
    /**
     * Constructs a new <code>MetaDataBuilder</code>.
     */
    protected MetaDataBuilder() {
        this.properties = new LinkedHashMap<String, PropertyMetaData<T, ?>>();
    }
    
    /**
     * Defines a property with the specified name and type.
     * 
     * @param name Property name.
     * @param type Property type.
     * @return Reference to this builder instance.
     */
    public <V> MetaDataBuilder<T> property(String name, Class<V> type) {
        return property(name, ConverterFactory.createConverter(type, name),
                null, null);
    }
    
    /**
     * Defines a property with the specified name, type and default value.
     * 
     * @param name Property name.
     * @param type Property type.
     * @param defaultValue Property default value.
     * @return Reference to this builder instance.
     */
    public <V> MetaDataBuilder<T> property(String name, Class<V> type, V defaultValue) {
        return property(name, ConverterFactory.createConverter(type, name),
                null, defaultValue);
    }
    
    /**
     * Defines a property with the specified name, type and constraint.
     * 
     * @param name Property name.
     * @param type Property type.
     * @param constraint Constraint to be used for property value validation.
     * @return Reference to this builder instance.
     */
    public <V> MetaDataBuilder<T> property(String name, Class<V> type,
            Constraint<? super V> constraint) {
        return property(name, ConverterFactory.createConverter(type, name),
                constraint, null);
    }
    
    /**
     * Defines a property with the specified name, type, constraint and
     * default value.
     * 
     * @param name Property name.
     * @param type Property type.
     * @param constraint Constraint to be used for property value validation.
     * @param defaultValue Property default value.
     * @return Reference to this builder instance.
     */
    public <V> MetaDataBuilder<T> property(String name, Class<V> type,
            Constraint<? super V> constraint, V defaultValue) {
        return property(name, ConverterFactory.createConverter(type, name),
                constraint, defaultValue);
    }
    
    /**
     * Defines a property with the specified name and converter.
     * 
     * @param name Property name.
     * @param converter Converter to be used for property value conversion
     *        into and from string.
     * @return Reference to this builder instance.
     */
    public <V> MetaDataBuilder<T> property(String name, Converter<V> converter) {
        return property(name, converter, null, null);
    }
    
    /**
     * Defines a property with the specified name, converter and default
     * value.
     * 
     * @param name Property name.
     * @param converter Converter to be used for property value conversion
     *        into and from string.
     * @param defaultValue Property default value.
     * @return Reference to this builder instance.
     */
    public <V> MetaDataBuilder<T> property(String name, Converter<V> converter,
            V defaultValue) {
        return property(name, converter, null, defaultValue);
    }
    
    /**
     * Defines a property with the specified name, converter and constraint.
     * 
     * @param name Property name.
     * @param converter Converter to be used for property value conversion
     *        into and from string.
     * @param constraint Constraint to be used for property value validation.
     * @return Reference to this builder instance.
     */
    public <V> MetaDataBuilder<T> property(String name, Converter<V> converter,
            Constraint<? super V> constraint) {
        return property(name, converter, constraint, null);
    }
    
    /**
     * Defines a property with the specified name, converter, constraint
     * and default value.
     * 
     * @param name Property name.
     * @param converter Converter to be used for property value conversion
     *        into and from string.
     * @param constraint Constraint to be used for property value validation.
     * @param defaultValue Property default value.
     * @return Reference to this builder instance.
     */
    public abstract <V> MetaDataBuilder<T> property(String name,
            Converter<V> converter, Constraint<? super V> constraint, V defaultValue);
    
    /**
     * Defines constraint to be used for entity validation.
     * 
     * @param constraint Constraint to be used for entity validation.
     * @return Reference to this builder instance.
     */
    public MetaDataBuilder<T> constraint(Constraint<? super T> constraint) {
        this.constraint = constraint;
        return this;
    }
    
    /**
     * Removes all the properties and clears entity constraint.
     */
    public void clear() {
        constraint = null;
        properties.clear();
    }
    
    /**
     * Builds a new <code>EntityMetaData</code> instance.
     * 
     * @return A new <code>EntityMetaData</code> instance.
     */
    public abstract <M extends EntityMetaData<T>> M build();
    
}
