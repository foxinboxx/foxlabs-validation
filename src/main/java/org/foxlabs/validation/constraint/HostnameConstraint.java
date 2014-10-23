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
 * whether a string is valid internet hostname according to
 * <a href="http://tools.ietf.org/html/rfc1123">RFC 1123</a>.
 * 
 * @author Fox Mulder
 * @see Hostname
 * @see ConstraintFactory#hostname()
 */
public final class HostnameConstraint extends CheckConstraint<String> {
    
    /**
     * <code>HostnameConstraint</code> single instance.
     */
    public static final HostnameConstraint DEFAULT = new HostnameConstraint();
    
    /**
     * Constructs default <code>HostnameConstraint</code>.
     */
    private HostnameConstraint() {}
    
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
     * Checks whether the specified string is valid internet hostname.
     * 
     * @param value String to be checked.
     * @param context Validation context.
     * @return <code>true</code> if the specified string is valid internet
     *         hostname; <code>false</code> otherwise.
     */
    @Override
    protected <T> boolean check(String value, ValidationContext<T> context) {
        if (value == null)
            return true;
        int length = value.length();
        if (length == 0)
            return false;
        int index = 0;
        for (int i = 0; i < length; i++) {
            char ch = value.charAt(i);
            if (ch == '.') {
                if (i == index || i + 1 == length)
                    return false;
                if (!isHostnameEnd(value.charAt(i - 1)))
                    return false;
                index = i + 1;
            } else if (i == index) {
                if (!isHostnameStart(ch))
                    return false;
            } else if (i + 1 == length) {
                if (!isHostnameEnd(ch))
                    return false;
            } else if (!isHostnamePart(ch)) {
                return false;
            }
        }
        return true;
    }
    
    /**
     * Determines if the specified character is permissible as the first
     * character in a hostname.
     * 
     * @param ch Character to be tested.
     * @return <code>true</code> if the specified character is permissible as
     *         the first character in a hostname; <code>false</code> otherwise.
     */
    static boolean isHostnameStart(char ch) {
        return ch >= 'a' && ch <= 'z'
            || ch >= 'A' && ch <= 'Z'
            || ch >= '0' && ch <= '9';
    }
    
    /**
     * Determines if the specified character may be part of a hostname as other
     * than the first and last character.
     * 
     * @param ch Character to be tested.
     * @return <code>true</code> if the specified character may be part of a
     *         hostname; <code>false</code> otherwise.
     */
    static boolean isHostnamePart(char ch) {
        return isHostnameStart(ch) || ch == '-';
    }
    
    /**
     * Determines if the specified character is permissible as the last
     * character in a hostname.
     * 
     * @param ch Character to be tested.
     * @return <code>true</code> if the specified character is permissible as
     *         the last character in a hostname; <code>false</code> otherwise.
     */
    static boolean isHostnameEnd(char ch) {
        return isHostnameStart(ch);
    }
    
}
