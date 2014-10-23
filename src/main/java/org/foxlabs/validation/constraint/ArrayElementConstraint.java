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

import java.lang.reflect.Array;

import java.util.List;

import org.foxlabs.validation.ValidationContext;
import org.foxlabs.validation.ValidationTarget;

import org.foxlabs.util.reflect.Types;

/**
 * This class provides <code>SequenceElementConstraint</code> implementation
 * that performs validation of array elements.
 * 
 * @author Fox Mulder
 * @param <V> The type of array elements
 * @see ConstraintFactory#arrayElement(Constraint)
 */
public final class ArrayElementConstraint<V> extends SequenceElementConstraint<Object, V> {
    
    /**
     * Array type.
     */
    private final Class<Object> type;
    
    /**
     * Constructs a new <code>ArrayElementConstraint</code> with the specified
     * constraint of array elements.
     * 
     * @param constraint Constraint to be used for validation of array elements.
     * @throws IllegalArgumentException if the specified constraint is
     *         <code>null</code>.
     */
    ArrayElementConstraint(Constraint<V> constraint) {
        super(constraint);
        this.type = Types.arrayTypeOf(constraint.getType());
    }
    
    /**
     * Returns array type.
     * 
     * @return Array type.
     */
    @Override
    public Class<?> getType() {
        return type;
    }
    
    /**
     * Checks whether all elements of the specified array conforms to the
     * constraint of array elements.
     * 
     * @param array Array whose elements to be checked.
     * @param context Validation context.
     * @param violations List of violations.
     * @return Array with possibly modified elements if constraint of array
     *         elements can modify values; unmodified array otherwise.
     */
    @Override
    @SuppressWarnings("unchecked")
    protected <T> Object doValidate(Object array, ValidationContext<T> context,
            List<ConstraintViolationException> violations) {
        int length = Array.getLength(array);
        context.setCurrentTarget(ValidationTarget.ELEMENTS);
        for (int i = 0; i < length; i++) {
            try {
                context.setCurrentIndex(i);
                V oldValue = (V) Array.get(array, i);
                V newValue = constraint.validate(oldValue, context);
                if (oldValue != newValue)
                    Array.set(array, i, newValue);
            } catch (ConstraintViolationException e) {
                violations.add(e);
            }
        }
        return array;
    }
    
}
