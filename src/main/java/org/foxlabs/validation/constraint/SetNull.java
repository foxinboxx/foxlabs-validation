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

import java.lang.annotation.Target;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import org.foxlabs.validation.ValidationTarget;

/**
 * Replaces value of the annotated element with <code>null</code>.
 * 
 * @author Fox Mulder
 * @see SetNullConstraint
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.METHOD, ElementType.FIELD})
@ConstrainedBy(SetNullConstraint.class)
public @interface SetNull {
    
    /**
     * @return Array of groups the constraint is applied on. Empty array means
     *         default group.
     */
    String[] groups() default {};
    
    /**
     * @return An object part to which constraint should be applied.
     *         {@link ValidationTarget#VALUE} will be used by default.
     */
    ValidationTarget[] targets() default {};
    
    /**
     * Defines several <code>@SetNull</code> annotations on the same element.
     * 
     * @author Fox Mulder
     */
    @Retention(RetentionPolicy.RUNTIME)
    @Target({ElementType.TYPE, ElementType.METHOD, ElementType.FIELD})
    public static @interface List {
        
        /**
         * @return <code>@SetNull</code> annotations.
         */
        SetNull[] value();
        
    }
    
}
