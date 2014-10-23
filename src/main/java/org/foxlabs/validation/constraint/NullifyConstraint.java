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
 * replaces an empty string with <code>null</code>.
 * 
 * @author Fox Mulder
 * @see Nullify
 * @see ConstraintFactory#nullify()
 */
public final class NullifyConstraint extends CorrectConstraint<String> {
    
    /**
     * <code>NullifyConstraint</code> single instance.
     */
    public static final NullifyConstraint DEFAULT = new NullifyConstraint();
    
    /**
     * Constructs default <code>NullifyConstraint</code>.
     */
    private NullifyConstraint() {}
    
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
     * Returns <code>null</code> if the specified string is empty; returns
     * string as is otherwise.
     * 
     * @param value Source string.
     * @param context Validation context.
     * @return <code>null</code> if the specified string is empty; string as is
     *         otherwise.
     */
    @Override
    public <T> String validate(String value, ValidationContext<T> context) {
        return value == null || value.isEmpty() ? null : value;
    }
    
}
