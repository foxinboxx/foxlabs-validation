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

package org.foxlabs.validation;

import java.lang.annotation.Annotation;

/**
 * Thrown to indicate that <code>Validation</code> instantiation fails.
 * 
 * @author Fox Mulder
 */
public class ValidationInstantiationException extends ValidationDeclarationException {
    private static final long serialVersionUID = 5923394325210180831L;
    
    /**
     * Constructs a new <code>ValidationInstantiationException</code> with the
     * specified annotation and cause.
     * 
     * @param annotation <code>Validation</code> annotation.
     * @param cause Cause of the <code>Validation</code> instantiation failure.
     */
    public ValidationInstantiationException(Annotation annotation, Throwable cause) {
        super(annotation, "Cannot create validation annotated with @" +
                annotation.annotationType().getName() + ": " + cause.toString());
    }
    
}
