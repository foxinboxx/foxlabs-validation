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

package org.foxlabs.validation.converter;

import java.lang.annotation.Target;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import org.foxlabs.validation.ValidationTarget;

/**
 * The annotated number pattern (see <code>java.text.DecimalFormat</code> for
 * more information).
 * 
 * @author Fox Mulder
 * @see NumberConverter
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.ANNOTATION_TYPE, ElementType.METHOD, ElementType.FIELD})
@ConvertedBy({
    ByteConverter.class,
    ShortConverter.class,
    IntegerConverter.class,
    LongConverter.class,
    FloatConverter.class,
    DoubleConverter.class,
    BigIntegerConverter.class,
    BigDecimalConverter.class
})
public @interface NumberPattern {
    
    /**
     * @return <code>java.text.DecimalFormat</code> number pattern.
     */
    String value();
    
    /**
     * @return Overriding error message template. Empty string means that default
     *         message template should be used.
     */
    String message() default "";
    
    /**
     * @return An object part to which converter should be applied.
     *         {@link ValidationTarget#VALUE} will be used by default.
     */
    ValidationTarget[] targets() default {};
    
    /**
     * Defines several <code>@NumberPattern</code> annotations on the same
     * element.
     * 
     * @author Fox Mulder
     */
    @Retention(RetentionPolicy.RUNTIME)
    @Target({ElementType.ANNOTATION_TYPE, ElementType.METHOD, ElementType.FIELD})
    public static @interface List {
        
        /**
         * @return <code>@NumberPattern</code> annotations.
         */
        NumberPattern[] value();
        
    }
    
}
