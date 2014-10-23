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
 * replaces a value with <code>null</code>.
 * 
 * @author Fox Mulder
 * @see SetNull
 * @see ConstraintFactory#setNull()
 */
public final class SetNullConstraint extends CorrectConstraint<Object> {
    
    /**
     * <code>SetNullConstraint</code> single instance.
     */
    public static final SetNullConstraint DEFAULT = new SetNullConstraint();
    
    /**
     * Constructs default <code>SetNullConstraint</code>.
     */
    private SetNullConstraint() {}
    
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
     * Returns <code>null</code>.
     * 
     * @param value Any value.
     * @param context Validation context.
     * @return <code>null</code>.
     */
    @Override
    public <T> Object validate(Object value, ValidationContext<T> context) {
        return null;
    }
    
}
