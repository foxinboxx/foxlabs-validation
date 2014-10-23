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
 * converts a string to lower case.
 * 
 * @author Fox Mulder
 * @see LowerCase
 * @see ConstraintFactory#lowerCase()
 */
public final class LowerCaseConstraint extends CorrectConstraint<String> {
    
    /**
     * <code>LowerCaseConstraint</code> single instance.
     */
    public static final LowerCaseConstraint DEFAULT = new LowerCaseConstraint();
    
    /**
     * Constructs default <code>LowerCaseConstraint</code>.
     */
    private LowerCaseConstraint() {}
    
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
     * Converts the specified string to lower case.
     * 
     * @param value String to be lowercased.
     * @param context Validation context.
     * @return The specified string in lower case.
     */
    @Override
    public <T> String validate(String value, ValidationContext<T> context) {
        return value == null || value.isEmpty() ? value : value.toLowerCase(context.getMessageLocale());
    }
    
}
