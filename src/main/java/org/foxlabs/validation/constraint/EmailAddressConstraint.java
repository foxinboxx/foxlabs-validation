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
 * whether a string is valid email address.
 * 
 * @author Fox Mulder
 * @see EmailAddress
 * @see ConstraintFactory#emailAddress()
 */
public final class EmailAddressConstraint extends CheckConstraint<String> {
    
    /**
     * <code>EmailAddressConstraint</code> single instance.
     */
    public static final EmailAddressConstraint DEFAULT = new EmailAddressConstraint();
    
    /**
     * Constructs default <code>EmailAddressConstraint</code>.
     */
    private EmailAddressConstraint() {}
    
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
     * Checks whether the specified string is valid email address.
     * 
     * @param value String to be checked.
     * @param context Validation context.
     * @return <code>true</code> if the specified string is valid email address;
     *         <code>false</code> otherwise.
     */
    @Override
    protected <T> boolean check(String value, ValidationContext<T> context) {
        if (value == null)
            return true;
        int length = value.length();
        int index = value.lastIndexOf('@');
        if (index < 1 || index + 1 == length)
            return false;
        // domain
        if (value.charAt(index + 1) == '[') {
            if (value.charAt(length - 1) != ']')
                return false;
            String ip = value.substring(index + 2, length - 1);
            if (!IpAddressConstraint.DEFAULT.check(ip, context))
                return false;
        } else {
            String hostname = value.substring(index + 1);
            if (!HostnameConstraint.DEFAULT.check(hostname, context))
                return false;
        }
        // local-part (special characters are not checked)
        for (int i = 0; i < index; i++) {
            char ch = value.charAt(i);
            if (!isLocalPart(ch))
                return false;
            if (ch == '.') {
                if (i == 0 || i + 1 == index || value.charAt(i - 1) == '.')
                    return false;
            }
        }
        return true;
    }
    
    /**
     * Determines if the specified character may be local part of an email.
     * 
     * @param ch Character to be tested.
     * @return <code>true</code> if the specified character may be local part
     *         of an email; <code>false</code> otherwise.
     */
    static boolean isLocalPart(char ch) {
        return ch >= 'a' && ch <= 'z'
            || ch >= 'A' && ch <= 'Z'
            || ch >= '0' && ch <= '9'
            || ".!#$%&'*+-/=?^_`{|}~".indexOf(ch) >= 0;
    }
    
}
