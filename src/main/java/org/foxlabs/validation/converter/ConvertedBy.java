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
 * Link between a converter annotation and its <code>Converter</code>
 * implementations.
 * 
 * A given converter annotation should be annotated by a
 * <code>@ConvertedBy</code> annotation which refers to its converter
 * implementations.
 * 
 * @author Fox Mulder
 * @see Converter
 */
@Target(ElementType.ANNOTATION_TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface ConvertedBy {
    
    /**
     * @return The converter implementation classes to be used for value conversion.
     */
    @SuppressWarnings("rawtypes")
    Class<? extends Converter>[] value();
    
}
