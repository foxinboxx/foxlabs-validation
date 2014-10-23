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
 * whether a string is valid IPv4 address.
 * 
 * @author Fox Mulder
 * @see Ip4Address
 * @see ConstraintFactory#ip4Address()
 */
public final class Ip4AddressConstraint extends CheckConstraint<String> {
    
    /**
     * <code>Ip4AddressConstraint</code> single instance.
     */
    public static final Ip4AddressConstraint DEFAULT = new Ip4AddressConstraint();
    
    /**
     * Constructs default <code>Ip4AddressConstraint</code>.
     */
    private Ip4AddressConstraint() {}
    
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
     * Checks whether the specified string is valid IPv4 address.
     * 
     * @param value String to be checked.
     * @param context Validation context.
     * @return <code>true</code> if the specified string is valid IPv4 address;
     *         <code>false</code> otherwise.
     */
    @Override
    protected <T> boolean check(String value, ValidationContext<T> context) {
        if (value == null)
            return true;
        int length = value.length();
        if (length < 7 || length > 15)
            return false;
        int digits = 0, dots = 0;
        for (int i = 0; i < length; i++) {
            char ch = value.charAt(i);
            if (ch == '.') {
                if (++dots > 3 || digits == 0 || i + 1 == length)
                    return false;
                digits = 0;
            } else if (ch >= '0' && ch <= '9') {
                if (++digits > 3)
                    return false;
                if (digits == 3) {
                    ch = value.charAt(i - 2);
                    if (ch > '2')
                        return false;
                    if (ch == '2') {
                        ch = value.charAt(i - 1);
                        if (ch > '5')
                            return false;
                        if (ch == '5') {
                            ch = value.charAt(i);
                            if (ch > '5')
                                return false;
                        }
                    }
                }
            } else {
                return false;
            }
        }
        return dots == 3;
    }
    
}
