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
 * This class provides <code>CorrectConstraint</code> implementation that
 * removes leading and trailing whitespaces from a string.
 * 
 * @author Fox Mulder
 * @see Trim
 * @see ConstraintFactory#trim()
 */
public final class TrimConstraint extends CorrectConstraint<String> {
    
    /**
     * <code>TrimConstraint</code> single instance.
     */
    public static final TrimConstraint DEFAULT = new TrimConstraint();
    
    /**
     * Constructs default <code>TrimConstraint</code>.
     */
    private TrimConstraint() {}
    
    /**
     * Returns <code>java.lang.String</code> type.
     * 
     * @return <code>java.lang.String</code> type.
     */
    @Override
    public Class<?> getType() {
        return String.class;
    }
    
    /**
     * Returns the specified string with no leading and trailing whitespaces.
     * 
     * @param value String to be trimmed.
     * @param context Validation context.
     * @return The specified string with no leading and trailing whitespaces.
     */
    @Override
    public <T> String validate(String value, ValidationContext<T> context) {
        return value == null ? null : value.trim();
    }
    
}
