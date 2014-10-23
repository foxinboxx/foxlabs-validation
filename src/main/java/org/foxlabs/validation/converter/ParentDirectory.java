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
 * The annotated file parent directory.
 * 
 * @author Fox Mulder
 * @see FileConverter
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.ANNOTATION_TYPE, ElementType.METHOD, ElementType.FIELD})
@ConvertedBy(FileConverter.class)
public @interface ParentDirectory {
    
    /**
     * Parent directory pathname.
     */
    String path();
    
    /**
     * Overriding error message template. Empty string means that default
     * message template should be used.
     */
    String message() default "";
    
    /**
     * An object part to which converter should be applied.
     * {@link ValidationTarget#VALUE} will be used by default.
     */
    ValidationTarget[] targets() default {};
    
    /**
     * Defines several <code>@ParentDirectory</code> annotations on the same
     * element.
     * 
     * @author Fox Mulder
     */
    @Retention(RetentionPolicy.RUNTIME)
    @Target({ElementType.ANNOTATION_TYPE, ElementType.METHOD, ElementType.FIELD})
    public static @interface List {
        
        /**
         * <code>@ParentDirectory</code> annotations.
         */
        ParentDirectory[] value();
        
    }
    
}
