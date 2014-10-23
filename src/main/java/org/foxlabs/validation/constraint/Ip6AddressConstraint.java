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
 * whether a string is valid IPv6 address.
 * 
 * @author Fox Mulder
 * @see Ip6Address
 * @see ConstraintFactory#ip6Address()
 */
public final class Ip6AddressConstraint extends CheckConstraint<String> {
    
    /**
     * <code>Ip6AddressConstraint</code> single instance.
     */
    public static final Ip6AddressConstraint DEFAULT = new Ip6AddressConstraint();
    
    /**
     * Constructs default <code>Ip6AddressConstraint</code>.
     */
    private Ip6AddressConstraint() {}
    
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
     * Checks whether the specified string is valid IPv6 address.
     * 
     * @param value String to be checked.
     * @param context Validation context.
     * @return <code>true</code> if the specified string is valid IPv6 address;
     *         <code>false</code> otherwise.
     */
    @Override
    protected <T> boolean check(String value, ValidationContext<T> context) {
        if (value == null)
            return true;
        int length = value.length();
        if (length < 2 || length > 39)
            return false;
        int maxcolons = 7;
        boolean compress = false;
        int index = value.indexOf('.');
        if (index > 0) {
            length = value.lastIndexOf(':', index);
            if (length < 1)
                return false;
            String ipv4 = value.substring(length + 1);
            if (!Ip4AddressConstraint.DEFAULT.check(ipv4, context))
                return false;
            if (value.charAt(length - 1) == ':') {
                if (--length == 0)
                    return true;
                compress = true;
                maxcolons--;
            }
            maxcolons -= 2;
        }
        boolean colon = false;
        int digits = 0, colons = 0;
        for (int i = 0; i < length; i++) {
            char ch = value.charAt(i);
            if (ch == ':') {
                if (colon) {
                    if (compress)
                        return false;
                    if (i == 1 || i + 1 == length)
                        maxcolons++;
                    compress = true;
                } else if (i + 1 == length) {
                    return false;
                }
                if (++colons > maxcolons)
                    return false;
                colon = true; digits = 0;
            } else if (ch >= '0' && ch <= '9' || ch >= 'a' && ch <= 'f' || ch >= 'A' && ch <= 'F') {
                if (++digits > 4)
                    return false;
                colon = false;
            } else {
                return false;
            }
        }
        return compress ? colons <= maxcolons : colons == maxcolons;
    }
    
}
