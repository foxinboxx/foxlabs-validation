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

package org.foxlabs.validation.metadata;

import java.util.Map;

import org.foxlabs.validation.constraint.Constraint;
import org.foxlabs.validation.converter.Converter;

import org.foxlabs.util.reflect.Types;

/**
 * This class provides <code>EntityMetaData</code> implementation for map.
 * 
 * @author Fox Mulder
 */
public class MapMetaData extends AbstractEntityMetaData<Map<String, Object>> {
    
    /**
     * Constructs a new <code>MapMetaData</code> with the specified map
     * constraint and properties metadata.
     * 
     * @param constraint Constraint to be used for map validation.
     * @param properties Metadata of all the properties defined on the map.
     */
    protected MapMetaData(Constraint<? super Map<String, Object>> constraint,
            Map<String, Property<Object>> properties) {
        super(constraint, properties);
    }
    
    /**
     * Returns <code>java.util.Map</code> type.
     * 
     * @return <code>java.util.Map</code> type.
     */
    @Override
    public final Class<Map<String, Object>> getType() {
        return Types.cast(Map.class);
    }
    
    // Property
    
    /**
     * This class provides <code>PropertyMetaData</code> implementation for map.
     * 
     * @author Fox Mulder
     * @param <V> The type of property
     */
    protected static class Property<V> extends AbstractPropertyMetaData<Map<String, Object>, V> {
        
        /**
         * Constructs a new <code>MapMetaData.Property</code> with the
         * specified name, converter, constraint and default value.
         * 
         * @param name Property name.
         * @param converter Converter to be used for property value conversion
         *        into and from string representation.
         * @param constraint Constraint to be used for property value validation.
         * @param defaultValue Property default value.
         * @throws IllegalArgumentException if the specified name or converter
         *         is <code>null</code> or name is empty.
         */
        protected Property(String name, Converter<V> converter,
                Constraint<? super V> constraint, V defaultValue) {
            super(name, converter, constraint, defaultValue);
        }
        
        /**
         * Returns value of this property for the specified map.
         * 
         * @param map Map whose property value should be returned.
         * @return Value of this property for the specified map.
         * @throws IllegalArgumentException if the specified map is
         *         <code>null</code>.
         */
        @Override
        public V getValue(Map<String, Object> map) {
            if (map == null)
                throw new IllegalArgumentException();
            return cast(map.get(name));
        }
        
        /**
         * Assigns value of this property for the specified map.
         * 
         * @param map Map whose property value should be assigned.
         * @param value New property value.
         * @throws IllegalArgumentException if the specified map is
         *         <code>null</code>.
         */
        @Override
        public void setValue(Map<String, Object> map, Object value) {
            if (map == null)
                throw new IllegalArgumentException();
            map.put(name, value == null ? defaultValue : cast(value));
        }
        
    }
    
    // Builder
    
    /**
     * This class allows to build <code>MapMetaData</code> instances.
     * 
     * @author Fox Mulder
     */
    public static class Builder extends MetaDataBuilder<Map<String, Object>> {
        
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
        @Override
        public <V> Builder property(String name, Converter<V> converter,
                Constraint<? super V> constraint, V defaultValue) {
            this.properties.put(name, new Property<V>(name, converter,
                    constraint, defaultValue));
            return this;
        }
        
        /**
         * Builds a new <code>MapMetaData</code> instance.
         * 
         * @return A new <code>MapMetaData</code> instance.
         */
        @Override
        @SuppressWarnings("unchecked")
        public MapMetaData build() {
            return new MapMetaData(constraint, (Map<String, Property<Object>>) properties.clone());
        }
        
    }
    
}
