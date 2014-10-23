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

import org.foxlabs.util.Assert;

/**
 * This class provides <code>CorrectConstraint</code> implementation that
 * allows to cut length of a string.
 * 
 * @author Fox Mulder
 * @see MaxLength
 * @see ConstraintFactory#maxLength(int)
 */
public final class MaxLengthConstraint extends CorrectConstraint<String> {
    
    /**
     * Maximum string length.
     */
    private final int limit;
    
    /**
     * Constructs a new <code>MaxLengthConstraint</code> with the specified
     * maximum string length.
     * 
     * @param limit Maximum string length.
     * @throws IllegalArgumentException if the specified limit is negative or
     *         zero.
     */
    MaxLengthConstraint(int limit) {
        this.limit = Assert.notNegativeOrZero(limit, "limit");
    }
    
    /**
     * Constructs a new <code>MaxLengthConstraint</code> from the specified
     * annotation.
     * 
     * @param annotation Constraint annotation.
     * @throws IllegalArgumentException if the specified annotation defines
     *         maximum string length that is negative or zero.
     */
    MaxLengthConstraint(MaxLength annotation) {
        this(annotation.value());
    }
    
    /**
     * Returns maximum string length.
     * 
     * @return Maximum string length.
     */
    public int getLimit() {
        return limit;
    }
    
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
     * Returns substring of maximum length for the specified string or string
     * as is if its length is not greater than maximum length.
     * 
     * @param value Source string.
     * @param context Validation context.
     * @return Substring of maximum length for the specified string or string
     *         as is if its length is not greater than maximum length.
     */
    @Override
    public <T> String validate(String value, ValidationContext<T> context) {
        return value == null ? null : value.length() > limit ? value.substring(0, limit) : value;
    }
    
}
