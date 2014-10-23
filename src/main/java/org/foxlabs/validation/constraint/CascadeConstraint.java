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

package org.foxlabs.validation.constraint;

import org.foxlabs.validation.AbstractValidation;
import org.foxlabs.validation.ValidationContext;
import org.foxlabs.validation.ValidationException;
import org.foxlabs.validation.metadata.BeanMetaData;
import org.foxlabs.validation.metadata.EntityMetaData;

import org.foxlabs.util.Assert;

/**
 * This class provides <code>Constraint</code> implementation that performs
 * cascade validation on an entity.
 * 
 * @author Fox Mulder
 * @param <V> The type of entity to be validated
 * @see Cascade
 * @see ConstraintFactory#cascade(Class)
 * @see ConstraintFactory#cascade(EntityMetaData)
 */
public final class CascadeConstraint<V> extends AbstractValidation<V> implements Constraint<V> {
    
    /**
     * The type of entity to be validated.
     */
    private final EntityMetaData<V> metadata;
    
    /**
     * Constructs a new <code>CascadeConstaint</code> with the specified bean
     * type.
     * 
     * @param type The type of bean to be validated.
     * @throws IllegalArgumentException if the specified type is not a bean.
     */
    CascadeConstraint(Class<V> type) {
        this(BeanMetaData.<V>getMetaData(type));
    }
    
    /**
     * Constructs a new <code>CascadeConstaint</code> with the specified entity
     * metadata.
     * 
     * @param metadata Entity metadata.
     * @throws IllegalArgumentException if the specified metadata is
     *         <code>null</code>.
     */
    CascadeConstraint(EntityMetaData<V> metadata) {
        this.metadata = Assert.notNull(metadata, "metadata");
    }
    
    /**
     * Returns the type of entity to be validated.
     * 
     * @return The type of entity to be validated.
     */
    @Override
    public Class<?> getType() {
        return metadata.getType();
    }
    
    /**
     * <code>CascadeConstaint</code> has no error message.
     * 
     * @param context Validation context.
     * @return <code>null</code>.
     */
    @Override
    public String getMessageTemplate(ValidationContext<?> context) {
        return null;
    }
    
    /**
     * Performs cascade validation on the specified entity.
     * 
     * @param value Entity to be validated.
     * @param context Validation context.
     * @return Possibly modified entity.
     * @throws ConstraintViolationException if cascade validation fails.
     */
    @Override
    public <T> V validate(V value, ValidationContext<T> context) {
        try {
            return context.getValidator()
                          .getFactory()
                          .newValidator(metadata, context.getValidator())
                          .newContext(context)
                          .validateEntity(value);
        } catch (ValidationException e) {
            throw new ConstraintViolationException(this, context, value, e);
        }
    }
    
}
