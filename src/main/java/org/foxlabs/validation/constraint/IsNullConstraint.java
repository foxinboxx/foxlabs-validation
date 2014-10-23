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
 * whether a value is <code>null</code>.
 * 
 * @author Fox Mulder
 * @see IsNull
 * @see ConstraintFactory#isNull()
 */
public final class IsNullConstraint extends CheckConstraint<Object> {
    
    /**
     * <code>IsNullConstraint</code> single instance.
     */
    public static final IsNullConstraint DEFAULT = new IsNullConstraint();
    
    /**
     * Constructs default <code>IsNullConstraint</code>.
     */
    private IsNullConstraint() {}
    
    /**
     * Returns <code>java.lang.Object</code> type.
     * 
     * @return <code>java.lang.Object</code> type.
     */
    @Override
    public Class<?> getType() {
        return Object.class;
    }
    
    /**
     * Checks whether the specified value is <code>null</code>.
     * 
     * @param value Value to be checked for <code>null</code>.
     * @param context Validation context.
     * @return <code>true</code> if the specified value is <code>null</code>;
     *         <code>false</code> otherwise.
     */
    @Override
    protected <T> boolean check(Object value, ValidationContext<T> context) {
        return value == null;
    }
    
}
