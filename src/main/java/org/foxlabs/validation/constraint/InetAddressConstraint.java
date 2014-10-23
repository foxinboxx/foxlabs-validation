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
 * whether a string is valid internet address (can be either hostname or IP
 * address).
 * 
 * @author Fox Mulder
 * @see InetAddress
 * @see ConstraintFactory#inetAddress()
 */
public final class InetAddressConstraint extends CheckConstraint<String> {
    
    /**
     * <code>InetAddressConstraint</code> single instance.
     */
    public static final InetAddressConstraint DEFAULT = new InetAddressConstraint();
    
    /**
     * Constructs default <code>InetAddressConstraint</code>.
     */
    private InetAddressConstraint() {}
    
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
     * Checks whether the specified string is valid internet address.
     * 
     * @param value String to be checked.
     * @param context Validation context.
     * @return <code>true</code> if the specified string is valid internet
     *         address; <code>false</code> otherwise.
     */
    @Override
    protected <T> boolean check(String value, ValidationContext<T> context) {
        return value == null
            || HostnameConstraint.DEFAULT.check(value, context)
            || IpAddressConstraint.DEFAULT.check(value, context);
    }
    
}
