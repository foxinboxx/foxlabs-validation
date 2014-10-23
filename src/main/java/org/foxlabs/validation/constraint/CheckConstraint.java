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

/**
 * This class provides base implementation of the <code>Constraint</code>
 * interface that only checks value and doesn't modify it.
 * 
 * @author Fox Mulder
 * @param <V> The type of value to be validated
 */
public abstract class CheckConstraint<V> extends AbstractValidation<V> implements Constraint<V> {
    
    /**
     * Performs validation of the specified value and returns unmodified value.
     * 
     * @param value Value to be validated.
     * @param context Validation context.
     * @return Unmodified value.
     * @throws ConstraintViolationException if validation fails.
     */
    @Override
    public final <T> V validate(V value, ValidationContext<T> context) {
        if (check(value, context))
            return value;
        throw new ConstraintViolationException(this, context, value);
    }
    
    /**
     * Checks whether the specified value conforms this constraint rules.
     * 
     * <p>Note that this method should not throw
     * <code>ConstraintViolationException</code>.</p>
     * 
     * @param value Value to be checked.
     * @param context Validation context.
     * @return <code>true</code> if the specified value conforms this
     *         constraint rules; <code>false</code> otherwise.
     */
    protected abstract <T> boolean check(V value, ValidationContext<T> context);
    
}
