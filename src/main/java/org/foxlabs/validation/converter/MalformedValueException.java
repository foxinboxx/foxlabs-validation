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

package org.foxlabs.validation.converter;

import org.foxlabs.validation.ValidationContext;
import org.foxlabs.validation.ViolationException;

/**
 * Thrown to indicate that converting string representation of a value fails.
 * 
 * @author Fox Mulder
 */
public class MalformedValueException extends ViolationException {
    private static final long serialVersionUID = 2769942635269487005L;
    
    /**
     * Constructs a new <code>MalformedValueException</code> with the specified
     * converter, context and invalid value.
     * 
     * @param converter Converter that is source of this exception.
     * @param context Validation context.
     * @param value String representation of invalid value.
     */
    public MalformedValueException(Converter<?> converter, ValidationContext<?> context,
            String value) {
        super(converter, context, value);
    }
    
    /**
     * Constructs a new <code>MalformedValueException</code> with the specified
     * converter, context, invalid value and cause.
     * 
     * @param converter Converter that is source of this exception.
     * @param context Validation context.
     * @param value String representation of invalid value.
     * @param cause Cause to be wrapped.
     */
    public MalformedValueException(Converter<?> converter, ValidationContext<?> context,
            String value, Throwable cause) {
        super(converter, context, value, cause);
    }
    
    /**
     * Returns converter that is source of this exception.
     * 
     * @return Converter that is source of this exception.
     */
    public Converter<?> getConverter() {
        return (Converter<?>) getComponent();
    }
    
    /**
     * Returns string representation of invalid value.
     * 
     * @return String representation of invalid value.
     */
    public String getInvalidValue() {
        return (String) super.getInvalidValue();
    }
    
}
