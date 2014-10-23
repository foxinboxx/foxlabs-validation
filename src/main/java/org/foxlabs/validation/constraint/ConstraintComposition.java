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
 * This class provides <code>ConstraintAggregation</code> implementation that
 * is composition of the encapsulated constraints.
 * 
 * <p>This class differs from the <code>ConstraintConjunction</code> in that it
 * returns error message composed from messages of the encapsulated constraints
 * (each message in a new line).</p>
 * 
 * @author Fox Mulder
 * @param <V> The type of value to be validated
 * @see Composition
 * @see ConstraintFactory#join(Constraint...)
 * @see ConstraintFactory#join(Class, Constraint...)
 * @see ConstraintFactory#join(java.util.Collection)
 * @see ConstraintFactory#join(Class, java.util.Collection)
 */
public final class ConstraintComposition<V> extends ConstraintAggregation<V> {
    
    /**
     * Constructs a new <code>ConstraintComposition</code> with the specified
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
    ConstraintComposition(Class<?> type, Constraint<? super V>[] constraints) {
        super(type, constraints);
    }
    
    /**
     * Returns localized error message or <code>null</code> if all of the
     * encapsulated constraints have no error message.
     * 
     * @param context Validation context.
     * @return Localized error message or <code>null</code> if all of the
     *         encapsulated constraints have no error message.
     */
    @Override
    public String getMessageTemplate(ValidationContext<?> context) {
        StringBuilder template = new StringBuilder();
        for (Constraint<?> constraint : constraints) {
            String message = context.buildMessage(constraint);
            if (message != null) {
                if (template.length() > 0)
                    template.append('\n');
                template.append(message);
            }
        }
        return template.length() == 0 ? null : template.toString();
    }
    
    /**
     * Returns <code>false</code>.
     * 
     * @param context Validation context.
     * @param arguments Arguments to be substituted into error message
     *        template.
     * @return <code>false</code>.
     */
    @Override
    public boolean appendMessageArguments(ValidationContext<?> context, Map<String, Object> arguments) {
        return false;
    }
    
}
