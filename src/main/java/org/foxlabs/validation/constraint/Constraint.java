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

import org.foxlabs.validation.Validation;
import org.foxlabs.validation.ValidationContext;

/**
 * The root interface in the constraint hierarchy. A constraint provides ability
 * to validate values of certain data type. <code>Constraint</code> instances
 * should be obtained from <code>ConstraintFactory</code>.
 * 
 * @author Fox Mulder
 * @param <V> The type of value to be validated
 * @see ConstraintFactory
 */
public interface Constraint<V> extends Validation<V> {
    
    /**
     * Returns the type of value to be validated.
     * 
     * @return The type of value to be validated.
     */
    Class<?> getType();
    
    /**
     * Returns localized error message template.
     * 
     * <p>This method may return <code>null</code> if constraint never
     * generates <code>ConstraintViolationException</code>.</p>
     * 
     * @param context Validation context.
     * @return Localized error message template.
     */
    String getMessageTemplate(ValidationContext<?> context);
    
    /**
     * Appends arguments to be substituted into the error message template.
     * 
     * @param context Validation context.
     * @param arguments Arguments to be substituted into the error message
     *        template.
     */
    boolean appendMessageArguments(ValidationContext<?> context, Map<String, Object> arguments);
    
    /**
     * Validates the specified value using context if needed and returns
     * possibly modified value.
     * 
     * @param <T> The type of validated entity.
     * @param value Value to be validated.
     * @param context Validation context.
     * @return Possibly modified value.
     * @throws ConstraintViolationException if validation fails.
     */
    <T> V validate(V value, ValidationContext<T> context);
    
}
