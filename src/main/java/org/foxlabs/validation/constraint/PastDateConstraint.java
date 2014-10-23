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

import java.util.Date;

import org.foxlabs.validation.ValidationContext;

/**
 * This class provides <code>CheckConstraint</code> implementation that checks
 * whether a date is date in the past.
 * 
 * @author Fox Mulder
 * @see PastDate
 * @see ConstraintFactory#pastDate()
 */
public final class PastDateConstraint extends CheckConstraint<Date> {
    
    /**
     * <code>PastDateConstraint</code> single instance.
     */
    public static final PastDateConstraint DEFAULT = new PastDateConstraint();
    
    /**
     * Constructs default <code>PastDateConstraint</code>.
     */
    private PastDateConstraint() {}
    
    /**
     * Returns <code>java.util.Date</code> type.
     * 
     * @return <code>java.util.Date</code> type.
     */
    @Override
    public Class<?> getType() {
        return Date.class;
    }
    
    /**
     * Checks whether the specified date is a date in the past.
     * 
     * @param value Date to be checked.
     * @param context Validation context.
     * @return <code>true</code> if the specified date is a date in the past;
     *         <code>false</code> otherwise.
     */
    @Override
    protected <T> boolean check(Date value, ValidationContext<T> context) {
        return value == null || value.before(new Date());
    }
    
}
