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

import java.util.Map;

import org.foxlabs.validation.ValidationContext;

/**
 * This class provides <code>Constraint</code> implementation for custom
 * constraints.
 * 
 * @author Fox Mulder
 */
public final class CustomConstraint<V> implements Constraint<V> {
    
    /**
     * Composite constraint.
     */
    private final Constraint<V> constraint;
    
    /**
     * Constructs a new <code>CustomConstraint</code> with the specified
     * composite constraint.
     * 
     * @param constraint Composite constraint.
     */
    CustomConstraint(Constraint<V> constraint) {
        this.constraint = constraint;
    }
    
    /**
     * The default behavior of this method is to return
     * {@link Constraint#getType()} on the composite constraint.
     * 
     * @see Constraint#getType()
     */
    @Override
    public Class<?> getType() {
        return constraint.getType();
    }
    
    /**
     * The default behavior of this method is to return
     * {@link Constraint#getMessageTemplate(ValidationContext)} on the
     * composite constraint.
     * 
     * @see Constraint#getMessageTemplate(ValidationContext)
     */
    @Override
    public String getMessageTemplate(ValidationContext<?> context) {
        return constraint.getMessageTemplate(context);
    }
    
    /**
     * The default behavior of this method is to return
     * {@link Constraint#appendMessageArguments(ValidationContext, Map)} on the
     * composite constraint.
     * 
     * @see Constraint#appendMessageArguments(ValidationContext, Map)
     */
    @Override
    public boolean appendMessageArguments(ValidationContext<?> context, Map<String, Object> arguments) {
        return constraint.appendMessageArguments(context, arguments);
    }
    
    /**
     * The default behavior of this method is to return
     * {@link Constraint#validate(Object, ValidationContext)} on the composite
     * constraint.
     * 
     * @see Constraint#validate(Object, ValidationContext)
     */
    @Override
    public <T> V validate(V value, ValidationContext<T> context) {
        try {
            return constraint.validate(value, context);
        } catch (ConstraintViolationException e) {
            throw new ConstraintViolationException(this, context, value);
        }
    }
    
}
