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

import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.Collections;

import org.foxlabs.validation.constraint.Constraint;

import org.foxlabs.util.reflect.Types;

/**
 * This class provides base implementation of the <code>EntityMetaData</code>
 * interface.
 * 
 * @author Fox Mulder
 * @param <T> The type of entity
 */
public abstract class AbstractEntityMetaData<T> implements EntityMetaData<T> {
    
    /**
     * Constraint to be used for entity validation.
     */
    protected final Constraint<? super T> constraint;
    
    /**
     * Metadata of all the properties defined on the entity.
     */
    protected final Map<String, PropertyMetaData<T, Object>> properties;
    
    /**
     * Constructs a new <code>AbstractEntityMetaData</code> with the specified
     * entity constraint and properties metadata.
     * 
     * @param constraint Constraint to be used for entity validation.
     * @param properties Metadata of all the properties defined on the entity.
     */
    protected AbstractEntityMetaData(Constraint<? super T> constraint,
            Map<String, ? extends PropertyMetaData<T, Object>> properties) {
        this.constraint = constraint;
        this.properties = Collections.unmodifiableMap(properties);
    }
    
    /**
     * Returns constraint to be used for entity validation. If this entity has
     * no constraint then this method returns <code>null</code>.
     * 
     * @return Constraint to be used for entity validation.
     */
    @Override
    public final Constraint<? super T> getConstraint() {
        return constraint;
    }
    
    /**
     * Returns set of property names.
     * 
     * @return Set of property names.
     */
    @Override
    public final Set<String> getPropertyNames() {
        return properties.keySet();
    }
    
    /**
     * Determines if this entity metadata contains the specified property.
     * 
     * @param property Property name.
     * @return <code>true</code> if this entity metadata contains the specified
     *         property; <code>false</code> otherwise.
     */
    @Override
    public boolean hasProperty(String property) {
        return properties.containsKey(property);
    }
    
    /**
     * Returns metadata for the specified property.
     * 
     * @param property Property name.
     * @return Metadata for the specified property.
     * @throws IllegalArgumentException if the specified property not exists.
     */
    @Override
    public final <V> PropertyMetaData<T, V> getPropertyMetaData(String property) {
        PropertyMetaData<T, ?> meta = properties.get(property);
        if (meta == null)
            throw new IllegalArgumentException(property);
        return Types.cast(meta);
    }
    
    /**
     * Returns metadata of all the properties defined on the entity.
     * 
     * @return Metadata of all the properties defined on the entity.
     */
    @Override
    public final Collection<PropertyMetaData<T, Object>> getAllPropertyMetaData() {
        return properties.values();
    }
    
    /**
     * Casts the specified entity to this entity type.
     * 
     * @param entity Entity to be cast.
     * @return Entity after casting.
     * @throws ClassCastException if the specified entity is not assignable to
     *         this entity type.
     */
    @Override
    public T cast(Object entity) {
        return getType().cast(entity);
    }
    
    /**
     * Returns a hash code value for this entity metadata.
     * 
     * @return A hash code value for this entity metadata.
     */
    public int hashCode() {
        return getType().hashCode();
    }
    
    /**
     * Determines if this entity metadata equals to the specified one.
     * By default two entities metadata consider to be equal if they have
     * equal types.
     * 
     * @param obj Another entity metadata.
     * @return <code>true</code> if this entity metadata equals to the
     *         specified one; <code>false</code> otherwise.
     */
    public boolean equals(Object obj) {
        if (obj instanceof EntityMetaData) {
            EntityMetaData<?> other = (EntityMetaData<?>) obj;
            return getType().equals(other.getType());
        }
        return false;
    }
    
    /**
     * Returns string representing this entity metadata.
     * 
     * @return String representing this entity metadata.
     */
    public String toString() {
        StringBuilder buf = new StringBuilder();
        buf.append(getType().getName());
        for (PropertyMetaData<T, ?> meta : getAllPropertyMetaData())
            buf.append("\n\t").append(meta);
        return buf.toString();
    }
    
}
