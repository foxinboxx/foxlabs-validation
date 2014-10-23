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

/**
 * Defines descriptor that holds necessary metadata about entity element.
 * 
 * @author Fox Mulder
 * @param <T> The type of entity
 * @param <V> The type of element
 */
public interface ElementMetaData<T, V> extends MetaData<V> {
    
    /**
     * Returns element name.
     * 
     * @return Element name.
     */
    String getName();
    
    /**
     * Returns default value of this entity element.
     * 
     * @return Default value of this entity element.
     */
    V getDefaultValue();
    
}
