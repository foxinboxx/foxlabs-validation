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

import org.foxlabs.validation.constraint.Constraint;
import org.foxlabs.validation.converter.Converter;

import org.foxlabs.util.Assert;
import org.foxlabs.util.reflect.Types;

/**
 * This class provides base implementation of the <code>PropertyMetaData</code>
 * interface.
 * 
 * @author Fox Mulder
 * @param <T> The type of entity
 * @param <V> The type of property
 */
public abstract class AbstractPropertyMetaData<T, V> implements PropertyMetaData<T, V> {
    
    /**
     * Property name.
     */
    protected final String name;
    
    /**
     * Converter to be used for property value conversion into and from string.
     */
    protected final Converter<V> converter;
    
    /**
     * Constraint to be used for property value validation.
     */
    protected final Constraint<? super V> constraint;
    
    /**
     * Property default value.
     */
    protected final V defaultValue;
    
    /**
     * Constructs a new <code>AbstractPropertyMetaData</code> with the specified
     * name, converter, constraint and default value.
     * 
     * @param name Property name.
     * @param converter Converter to be used for property value conversion
     *        into and from string.
     * @param constraint Constraint to be used for property value validation.
     * @param defaultValue Property default value.
     * @throws IllegalArgumentException if the specified name or converter is
     *         <code>null</code> or name is empty.
     */
    protected AbstractPropertyMetaData(String name, Converter<V> converter,
            Constraint<? super V> constraint, V defaultValue) {
        this.name = Assert.notEmpty(name, "name");
        this.converter = Assert.notNull(converter, "converter");
        this.constraint = constraint;
        this.defaultValue = defaultValue == null
            ? Types.defaultValueOf(converter.getType())
            : defaultValue;
    }
    
    /**
     * Returns type of this property.
     * 
     * @return Type of this property.
     */
    @Override
    public final Class<V> getType() {
        return converter.getType();
    }
    
    /**
     * Returns name of this property.
     * 
     * @return Name of this property.
     */
    @Override
    public final String getName() {
        return name;
    }
    
    /**
     * Returns default value of this property.
     * 
     * @return Default value of this property.
     */
    @Override
    public final V getDefaultValue() {
        return defaultValue;
    }
    
    /**
     * Returns converter to be used for property value conversion into and from
     * string.
     * 
     * @return Converter to be used for property value conversion into and from
     *         string.
     */
    @Override
    public final Converter<V> getConverter() {
        return converter;
    }
    
    /**
     * Returns constraint to be used for property value validation. If this
     * property has no constraint then this method returns <code>null</code>.
     * 
     * @return Constraint to be used for property value validation.
     */
    @Override
    public final Constraint<? super V> getConstraint() {
        return constraint;
    }
    
    /**
     * Determines if this property requires value.
     * 
     * @return <code>true</code> if this property requires value;
     *         <code>false</code> otherwise.
     */
    @Override
    public boolean isRequired() {
        return getType().isPrimitive();
    }
    
    /**
     * Determines if this property is readable.
     * 
     * <p>This method always returns <code>true</code> and should be overriden
     * in subclasses.</p>
     * 
     * @return <code>true</code>.
     */
    @Override
    public boolean isReadable() {
        return true;
    }
    
    /**
     * Determines if this property is writeable.
     * 
     * <p>This method always returns <code>true</code> and should be overriden
     * in subclasses.</p>
     * 
     * @return <code>true</code>.
     */
    @Override
    public boolean isWriteable() {
        return true;
    }
    
    /**
     * Casts the specified value to this property type.
     * 
     * @param value Value to be cast.
     * @return Value after casting.
     * @throws ClassCastException if the specified value is not assignable to
     *         this property type.
     */
    @Override
    public V cast(Object value) {
        return getType().cast(value);
    }
    
    /**
     * Returns value of this property for the specified entity.
     * 
     * <p>This method always throws <code>UnsupportedOperationException</code>
     * and should be overriden in subclasses.</p>
     * 
     * @param entity Entity whose property value should be returned.
     * @throws UnsupportedOperationException.
     */
    @Override
    public V getValue(T entity) {
        throw new UnsupportedOperationException();
    }
    
    /**
     * Assigns value of this property for the specified entity.
     * 
     * <p>This method always throws <code>UnsupportedOperationException</code>
     * and should be overriden in subclasses.</p>
     * 
     * @param entity Entity whose property value should be assigned.
     * @param value New property value.
     * @throws UnsupportedOperationException.
     */
    @Override
    public void setValue(T entity, Object value) {
        throw new UnsupportedOperationException();
    }
    
    /**
     * Returns a hash code value for this property metadata.
     * 
     * @return A hash code value for this property metadata.
     */
    public int hashCode() {
        return getType().hashCode() ^ getName().hashCode();
    }
    
    /**
     * Determines if this property metadata equals to the specified one.
     * By default two properties metadata considered to be equal if they have
     * equal names and types.
     * 
     * @param obj Another property metadata.
     * @return <code>true</code> if this property metadata equals to the
     *         specified one; <code>false</code> otherwise.
     */
    public boolean equals(Object obj) {
        if (obj instanceof PropertyMetaData) {
            PropertyMetaData<?, ?> other = (PropertyMetaData<?, ?>) obj;
            return getType().equals(other.getType()) && getName().equals(other.getName());
        }
        return false;
    }
    
    /**
     * Returns string representing this property metadata.
     * 
     * @return String representing this property metadata.
     */
    public String toString() {
        return name + ": " + getType().getName();
    }
    
}
