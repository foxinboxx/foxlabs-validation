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

import java.util.Comparator;

import org.foxlabs.validation.ValidationTarget;

/**
 * Checks whether value of the annotated element is not greater than allowed
 * maximum value.
 * 
 * @author Fox Mulder
 * @see RangeConstraint
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.METHOD, ElementType.FIELD})
@ConstrainedBy(RangeConstraint.class)
public @interface Max {
    
    /**
     * @return Maximum value.
     */
    String value();
    
    /**
     * @return Type of the comparator to be used for range checking.
     */
    Class<? extends Comparator<?>> comparator() default DefaultComparator.class;
    
    /**
     * @return Overriding error message template. Empty string means that default
     *         message template should be used.
     */
    String message() default "";
    
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
     * Defines several <code>@Max</code> annotations on the same element.
     * 
     * @author Fox Mulder
     */
    @Retention(RetentionPolicy.RUNTIME)
    @Target({ElementType.TYPE, ElementType.METHOD, ElementType.FIELD})
    public static @interface List {
        
        /**
         * @return <code>@Max</code> annotations.
         */
        Max[] value();
        
    }
    
}
