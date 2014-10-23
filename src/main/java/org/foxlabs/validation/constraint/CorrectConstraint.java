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
 * interface that modifies (corrects) value and never throws
 * <code>ConstraintViolationException</code>.
 * 
 * @author Fox Mulder
 * @param <V> The type of value to be validated
 */
public abstract class CorrectConstraint<V> extends AbstractValidation<V> implements Constraint<V> {
    
    /**
     * <code>CorrectConstraint</code>s have no error message.
     * 
     * @param context Validation context.
     * @return <code>null</code>.
     */
    @Override
    public final String getMessageTemplate(ValidationContext<?> context) {
        return null;
    }
    
}
