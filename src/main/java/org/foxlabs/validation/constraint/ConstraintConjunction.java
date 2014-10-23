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

/**
 * This class provides <code>ConstraintAggregation</code> implementation that
 * is conjunction of the encapsulated constraints. In other words this class
 * represents logical AND operator.
 * 
 * @author Fox Mulder
 * @param <V> The type of value to be validated
 * @see Conjunction
 * @see ConstraintFactory#and(Constraint...)
 * @see ConstraintFactory#and(Class, Constraint...)
 * @see ConstraintFactory#and(java.util.Collection)
 * @see ConstraintFactory#and(Class, java.util.Collection)
 */
public final class ConstraintConjunction<V> extends ConstraintAggregation<V> {
    
    /**
     * Constructs a new <code>ConstraintConjunction</code> with the specified
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
    ConstraintConjunction(Class<?> type, Constraint<? super V>[] constraints) {
        super(type, constraints);
    }
    
}
