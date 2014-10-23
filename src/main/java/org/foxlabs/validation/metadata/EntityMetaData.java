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

import java.util.Set;
import java.util.Collection;

/**
 * Defines descriptor that holds necessary metadata about entity.
 * 
 * @author Fox Mulder
 * @param <T> The type of entity
 */
public interface EntityMetaData<T> extends MetaData<T> {
    
    /**
     * Returns set of property names.
     * 
     * @return Set of property names.
     */
    Set<String> getPropertyNames();
    
    /**
     * Determines if this entity metadata contains the specified property.
     * 
     * @param property Property name.
     * @return <code>true</code> if this entity metadata contains the specified
     *         property; <code>false</code> otherwise.
     */
    boolean hasProperty(String property);
    
    /**
     * Returns metadata for the specified property.
     * 
     * @param property Property name.
     * @return Metadata for the specified property.
     * @throws IllegalArgumentException if the specified property not exists.
     */
    <V> PropertyMetaData<T, V> getPropertyMetaData(String property);
    
    /**
     * Returns metadata of all the properties defined on the entity.
     * 
     * @return Metadata of all the properties defined on the entity.
     */
    Collection<PropertyMetaData<T, Object>> getAllPropertyMetaData();
    
}
