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
 * This class provides <code>CheckConstraint</code> implementation that checks
 * whether a string is valid identifier.
 * 
 * <p>Valid identifier must contain alphanumeric and underscore characters
 * only, first character cannot be digit.</p>
 * 
 * @author Fox Mulder
 * @see Identifier
 * @see ConstraintFactory#identifier()
 */
public final class IdentifierConstraint extends CheckConstraint<String> {
    
    /**
     * <code>IdentifierConstraint</code> single instance.
     */
    public static final IdentifierConstraint DEFAULT = new IdentifierConstraint();
    
    /**
     * Constructs default <code>IdentifierConstraint</code>.
     */
    private IdentifierConstraint() {}
    
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
     * Checks whether the specified string is valid identifier.
     * 
     * @param value String to be checked.
     * @param context Validation context.
     * @return <code>true</code> if the specified string is valid identifier;
     *         <code>false</code> otherwise.
     */
    @Override
    protected <T> boolean check(String value, ValidationContext<T> context) {
        if (value == null)
            return true;
        int length = value.length();
        if (length == 0)
            return false;
        if (!isIdentifierStart(value.charAt(0)))
            return false;
        for (int i = 1; i < length; i++)
            if (!isIdentifierPart(value.charAt(i)))
                return false;
        return true;
    }
    
    /**
     * Determines if the specified character is permissible as the first
     * character in an identifier.
     * 
     * @param ch Character to be tested.
     * @return <code>true</code> if the specified character is permissible as
     *         the first character in an identifier; <code>false</code>
     *         otherwise.
     */
    static boolean isIdentifierStart(char ch) {
        return ch == '_'
            || ch >= 'A' && ch <= 'Z'
            || ch >= 'a' && ch <= 'z';
    }
    
    /**
     * Determines if the specified character may be part of an identifier as
     * other than the first character.
     * 
     * @param ch Character to be tested.
     * @return <code>true</code> if the specified character may be part of an
     *         identifier; <code>false</code> otherwise.
     */
    static boolean isIdentifierPart(char ch) {
        return isIdentifierStart(ch)
            || ch >= '0' && ch <= '9';
    }
    
}
