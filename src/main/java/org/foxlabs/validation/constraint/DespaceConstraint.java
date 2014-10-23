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
 * removes all whitespaces from a string.
 * 
 * @author Fox Mulder
 * @see Despace
 * @see ConstraintFactory#despace()
 */
public final class DespaceConstraint extends CorrectConstraint<String> {
    
    /**
     * <code>DespaceConstraint</code> single instance.
     */
    public static final DespaceConstraint DEFAULT = new DespaceConstraint();
    
    /**
     * Constructs default <code>DespaceConstraint</code>.
     */
    private DespaceConstraint() {}
    
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
     * Removes all whitespaces from the specified string.
     * 
     * @param value Source string.
     * @param context Validation context.
     * @return The specified string with no whitespaces.
     */
    @Override
    public <T> String validate(String value, ValidationContext<T> context) {
        int length = value == null ? 0 : value.length();
        if (length == 0)
            return value;
        StringBuilder buf = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            char ch = value.charAt(i);
            if (!Character.isWhitespace(ch))
                buf.append(ch);
        }
        return buf.toString();
    }
    
}
