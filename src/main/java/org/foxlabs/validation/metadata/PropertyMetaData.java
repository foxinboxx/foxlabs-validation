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

import org.foxlabs.validation.converter.Converter;

/**
 * Defines descriptor that holds necessary metadata about property.
 * 
 * @author Fox Mulder
 * @param <T> The type of entity
 * @param <V> The type of property
 */
public interface PropertyMetaData<T, V> extends ElementMetaData<T, V> {
    
    /**
     * Returns converter to be used for property value conversion into and from
     * string representation.
     * 
     * @return Converter to be used for property value conversion into and from
     *         string representation.
     */
    Converter<V> getConverter();
    
    /**
     * Determines if this property requires value.
     * 
     * @return <code>true</code> if this property requires value;
     *         <code>false</code> otherwise.
     */
    boolean isRequired();
    
    /**
     * Determines if this property is readable.
     * 
     * @return <code>true</code> if this property is readable;
     *         <code>false</code> otherwise.
     */
    boolean isReadable();
    
    /**
     * Determines if this property is writeable.
     * 
     * @return <code>true</code> if this property is writeable;
     *         <code>false</code> otherwise.
     */
    boolean isWriteable();
    
    /**
     * Returns value of this property for the specified entity.
     * 
     * @param entity Entity whose property value should be returned.
     * @return Value of this property for the specified entity.
     * @throws IllegalArgumentException if the specified entity is <code>null</code>.
     * @throws UnsupportedOperationException if this property is not readable.
     */
    V getValue(T entity);
    
    /**
     * Assigns value of this property for the specified entity.
     * 
     * @param entity Entity whose property value should be assigned.
     * @param value New property value.
     * @throws IllegalArgumentException if the specified entity is <code>null</code>.
     * @throws UnsupportedOperationException if this property is not writeable.
     */
    void setValue(T entity, Object value);
    
}
