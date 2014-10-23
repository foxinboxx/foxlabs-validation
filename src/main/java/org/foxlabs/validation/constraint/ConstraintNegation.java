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

import org.foxlabs.validation.ValidationContext;

/**
 * This class provides <code>ConstraintAggregation</code> implementation that
 * is negation of the encapsulated constraints. In other words this class
 * represents logical NOT operator.
 * 
 * @author Fox Mulder
 * @param <V> The type of value to be validated
 * @see Negation
 * @see ConstraintFactory#not(Constraint...)
 * @see ConstraintFactory#not(Class, Constraint...)
 * @see ConstraintFactory#not(java.util.Collection)
 * @see ConstraintFactory#not(Class, java.util.Collection)
 */
public final class ConstraintNegation<V> extends ConstraintAggregation<V> {
    
    /**
     * Constructs a new <code>ConstraintNegation</code> with the specified
     * value type and array of constraints.
     * 
     * @param type The type of value to be validated.
     * @param constraints Array of constraints to be used for validation of a
     *        value.
     * @throws IllegalArgumentException if the specified type is
     *         <code>null</code> or the specified array of constraints is
     *         <code>null</code> or empty or contains <code>null</code>
     *         elements.
     */
    ConstraintNegation(Class<?> type, Constraint<? super V>[] constraints) {
        super(type, constraints);
    }
    
    /**
     * Checks whether the specified value not conforms to all of the
     * encapsulated constraints.
     * 
     * @param value Value to be validated.
     * @param context Validation context.
     * @return Unmodified value.
     * @throws ConstraintViolationException if the specified value conforms
     *         at least one of the encapsulated constraint.
     */
    @Override
    public <T> V validate(V value, ValidationContext<T> context) {
        for (Constraint<? super V> constraint : constraints) {
            try {
                constraint.validate(value, context);
            } catch (ConstraintViolationException e) {
                continue;
            }
            throw new ConstraintViolationException(this, context, value);
        }
        return value;
    }
    
}
