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
import org.foxlabs.validation.ViolationException;

/**
 * Thrown to indicate that constraint validation fails.
 * 
 * @author Fox Mulder
 */
public class ConstraintViolationException extends ViolationException {
    private static final long serialVersionUID = -6788022838214237499L;
    
    /**
     * Constructs a new <code>ConstraintViolationException</code> with the
     * specified constraint, context and invalid value.
     * 
     * @param constraint Constraint that is source of this exception.
     * @param context Validation context.
     * @param value Invalid value.
     */
    public ConstraintViolationException(Constraint<?> constraint, ValidationContext<?> context,
            Object value) {
        super(constraint, context, value);
    }
    
    /**
     * Constructs a new <code>ConstraintViolationException</code> with the
     * specified constraint, context, invalid value and cause.
     * 
     * @param constraint Constraint that is source of this exception.
     * @param context Validation context.
     * @param value Invalid value.
     * @param cause Cause to be wrapped.
     */
    public ConstraintViolationException(Constraint<?> constraint, ValidationContext<?> context,
            Object value, Throwable cause) {
        super(constraint, context, value, cause);
    }
    
    /**
     * Returns constraint that is source of this exception.
     * 
     * @return Constraint that is source of this exception.
     */
    public Constraint<?> getConstraint() {
        return (Constraint<?>) getComponent();
    }
    
}
