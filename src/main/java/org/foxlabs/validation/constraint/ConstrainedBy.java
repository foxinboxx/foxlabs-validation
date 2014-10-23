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

/**
 * Link between a constraint annotation and its <code>Constraint</code>
 * implementations.
 * 
 * A given constraint annotation should be annotated by a
 * <code>@ConstrainedBy</code> annotation which refers to its constraint
 * implementations.
 * 
 * @author Fox Mulder
 * @see Constraint
 */
@Target(ElementType.ANNOTATION_TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface ConstrainedBy {
    
    /**
     * Specifies the constraint implementation classes to be used for value
     * validation.
     */
    @SuppressWarnings("rawtypes")
    Class<? extends Constraint>[] value();
    
}
