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
 * Thrown to indicate that <code>Validation</code> implementation class doesn't
 * define valid constructor.
 * 
 * @author Fox Mulder
 */
public class ValidationSignatureException extends ValidationDeclarationException {
    private static final long serialVersionUID = 3183926002307520773L;
    
    /**
     * Constructs a new <code>ValidationSignatureException</code> with the
     * specified annotation and <code>Validation</code> instance type.
     * 
     * @param annotation <code>Validation</code> annotation.
     * @param validationType <code>Validation</code> implementation class.
     */
    public ValidationSignatureException(Annotation annotation, Class<?> validationType) {
        super(annotation, "Validation " + validationType.getName() +
                " must declare one of the following constructors with any access modifier:\n" +
                validationType.getSimpleName() + "()\n" +
                validationType.getSimpleName() + "(Class)\n" +
                validationType.getSimpleName() + "(" + annotation.annotationType().getName() + ")\n" +
                validationType.getSimpleName() + "(Class, " + annotation.annotationType().getName() + ")\n" +
                validationType.getSimpleName() + "(" + annotation.annotationType().getName() + ", Class)");
    }
    
}
