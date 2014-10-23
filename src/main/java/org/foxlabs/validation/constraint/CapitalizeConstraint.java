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
 * converts to upper case first letter of first word in a string.
 * 
 * @author Fox Mulder
 * @see Capitalize
 * @see ConstraintFactory#capitalize()
 */
public final class CapitalizeConstraint extends CorrectConstraint<String> {
    
    /**
     * <code>CapitalizeConstraint</code> single instance.
     */
    public static final CapitalizeConstraint DEFAULT = new CapitalizeConstraint();
    
    /**
     * Constructs default <code>CapitalizeConstraint</code>.
     */
    private CapitalizeConstraint() {}
    
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
     * Converts to upper case first letter of first word in the specified
     * string.
     * 
     * @param value String to be capitalized.
     * @param context Validation context.
     * @return Capitalized string.
     */
    @Override
    public <T> String validate(String value, ValidationContext<T> context) {
        if (value == null || value.length() == 0)
            return value;
        char[] chars = value.toCharArray();
        for (int i = 0; i < chars.length; i++)
            if (Character.isLetter(chars[i])) {
                chars[i] = Character.toUpperCase(chars[i]);
                return new String(chars);
            }
        return value;
    }
    
}
