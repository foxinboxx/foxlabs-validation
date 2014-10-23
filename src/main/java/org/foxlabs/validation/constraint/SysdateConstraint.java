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
 * This class provides <code>CorrectConstraint</code> implementation that
 * replaces a <code>null</code> date with current system date.
 * 
 * @author Fox Mulder
 * @see Sysdate
 * @see ConstraintFactory#sysdate()
 */
public final class SysdateConstraint extends CorrectConstraint<Date> {
    
    /**
     * <code>SysdateConstraint</code> single instance.
     */
    public static final SysdateConstraint DEFAULT = new SysdateConstraint();
    
    /**
     * Constructs default <code>SysdateConstraint</code>.
     */
    private SysdateConstraint() {}
    
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
     * Returns current system date if the specified date is <code>null</code>;
     * returns date as is otherwise.
     * 
     * @param value Date to be tested for <code>null</code>.
     * @param context Validation context.
     * @return Current system date if the specified date is <code>null</code>;
     *         date as is otherwise.
     */
    @Override
    public <T> Date validate(Date value, ValidationContext<T> context) {
        return value == null ? new Date() : value;
    }
    
}
