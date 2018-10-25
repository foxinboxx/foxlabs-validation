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

/**
 * The string tokenizer annotation that should be used to split string into
 * tokens. This annotation can be applied to array or collection or map types
 * only.
 * 
 * @author Fox Mulder
 * @see SimpleTokenizer
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.ANNOTATION_TYPE, ElementType.METHOD, ElementType.FIELD})
@ConvertedBy(SimpleTokenizer.class)
public @interface TokenDelimiters {
    
    /**
     * @return Allowed elements delimiter characters.
     */
    String value();
    
    /**
     * @return Overriding error message template. Empty string means that default
     *         message template should be used.
     */
    String message() default "";
    
}
