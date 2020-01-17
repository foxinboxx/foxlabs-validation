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

import java.util.List;
import java.util.LinkedList;
import java.util.Map;

import org.foxlabs.validation.AbstractValidation;
import org.foxlabs.validation.ValidationContext;
import org.foxlabs.validation.ValidationException;

import org.foxlabs.util.Assert;

/**
 * This class provides base implementation of the <code>Constraint</code> that
 * encapsulates other constraints to validate a value.
 * 
 * @author Fox Mulder
 * @param <V> The type of value to be validated
 */
public abstract class ConstraintAggregation<V> extends AbstractValidation<V> implements Constraint<V> {
    
    /**
     * The type of value to be validated.
     */
    protected final Class<?> type;
    
    /**
     * Array of constraints to be used for validation of a value.
     */
    protected final Constraint<? super V>[] constraints;
    
    /**
     * Constructs a new <code>ConstraintAggregation</code> with the specified
     * value type and array of other constraints.
     * 
     * @param type The type of value to be validated.
     * @param constraints Array of constraints to be used for validation of a
     *        value.
     * @throws IllegalArgumentException if the specified type is
     *         <code>null</code> or the specified array of constraints is
     *         <code>null</code> or empty or contains <code>null</code>
     *         elements.
     */
    @SafeVarargs
    protected ConstraintAggregation(Class<?> type, Constraint<? super V>... constraints) {
        this.type = Assert.notNull(type, "type");
        this.constraints = Assert.noNullElements(constraints, "constraints");
    }
    
    /**
     * Returns the type of value to be validated.
     * 
     * @return The type of value to be validated.
     */
    @Override
    public final Class<?> getType() {
        return type;
    }
    
    /**
     * Returns array of constraints to be used for validation of a value.
     * 
     * @return Array of constraints to be used for validation of a value.
     */
    public final Constraint<? super V>[] getConstraints() {
        return constraints.clone();
    }
    
    /**
     * Appends <code>constraints</code> argument that contains encapsulated
     * constraints.
     * 
     * @param context Validation context.
     * @param arguments Arguments to be substituted into the error message
     *        template.
     * @return <code>true</code>.
     */
    @Override
    public boolean appendMessageArguments(ValidationContext<?> context, Map<String, Object> arguments) {
        super.appendMessageArguments(context, arguments);
        arguments.put("constraints", constraints);
        return true;
    }
    
    /**
     * Checks whether the specified value conforms to all of the encapsulated
     * constraints.
     * 
     * @param value Value to be validated.
     * @param context Validation context.
     * @return Possibly modified value if at least one of the encapsulated
     *         constraints can modify value; unmodified value otherwise.
     * @throws ConstraintViolationException if the specified value not
     *         conforms at least one of the encapsulated constraint.
     */
    @Override
    @SuppressWarnings("unchecked")
    public <T> V validate(V value, ValidationContext<T> context) {
        List<ConstraintViolationException> violations = new LinkedList<ConstraintViolationException>();
        for (Constraint<? super V> constraint : constraints) {
            try {
                value = (V) constraint.validate(value, context);
            } catch (ConstraintViolationException e) {
                violations.add(e);
                if (context.isFailFast())
                    break;
            }
        }
        if (violations.isEmpty())
            return value;
        throw new ConstraintViolationException(this, context, value,
                new ValidationException(violations));
    }
    
}
