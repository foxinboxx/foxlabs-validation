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

package org.foxlabs.validation.metadata;

import org.foxlabs.validation.constraint.Constraint;

/**
 * Defines descriptor that holds metadata.
 * 
 * @author Fox Mulder
 * @param <T> The type of data
 */
public interface MetaData<T> {
    
    /**
     * Returns the type of data.
     * 
     * @return The type of data.
     */
    Class<T> getType();
    
    /**
     * Returns constraint to be used for data validation. If there is no
     * constraint defined then this method returns <code>null</code>.
     * 
     * @return Constraint to be used for data validation.
     */
    Constraint<? super T> getConstraint();
    
    /**
     * Casts the specified value to data type.
     * 
     * @param value Value to be cast.
     * @return Value after casting.
     * @throws ClassCastException if the specified value is not assignable to
     *         data type.
     */
    T cast(Object value);
    
}
