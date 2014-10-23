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
 * whether a boolean value is <code>false</code>.
 * 
 * @author Fox Mulder
 * @see IsFalse
 * @see ConstraintFactory#isFalse()
 */
public final class IsFalseConstraint extends CheckConstraint<Boolean> {
    
    /**
     * <code>IsFalseConstraint</code> single instance.
     */
    public static final IsFalseConstraint DEFAULT = new IsFalseConstraint();
    
    /**
     * Constructs default <code>IsFalseConstraint</code>.
     */
    private IsFalseConstraint() {}
    
    /**
     * Returns <code>java.lang.Boolean</code> type.
     * 
     * @return <code>java.lang.Boolean</code> type.
     */
    @Override
    public Class<?> getType() {
        return Boolean.class;
    }
    
    /**
     * Checks whether the specified boolean value is <code>false</code>.
     * 
     * @param value Boolean value to be checked for <code>false</code>.
     * @param context Validation context.
     * @return <code>true</code> if the specified boolean value is
     *         <code>false</code>; <code>false</code> otherwise.
     */
    @Override
    protected <T> boolean check(Boolean value, ValidationContext<T> context) {
        return value == null || !value;
    }
    
}
