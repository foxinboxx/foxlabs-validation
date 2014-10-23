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
 * converts a string to upper case.
 * 
 * @author Fox Mulder
 * @see UpperCase
 * @see ConstraintFactory#upperCase()
 */
public final class UpperCaseConstraint extends CorrectConstraint<String> {
    
    /**
     * <code>UpperCaseConstraint</code> single instance.
     */
    public static final UpperCaseConstraint DEFAULT = new UpperCaseConstraint();
    
    /**
     * Constructs default <code>UpperCaseConstraint</code>.
     */
    private UpperCaseConstraint() {}
    
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
     * Converts the specified string to upper case.
     * 
     * @param value String to be uppercased.
     * @param context Validation context.
     * @return The specified string in upper case.
     */
    @Override
    public <T> String validate(String value, ValidationContext<T> context) {
        return value == null || value.isEmpty() ? value : value.toUpperCase(context.getMessageLocale());
    }
    
}
