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
 * Thrown to indicate that declaration of <code>Validation</code> annotation
 * is not legal.
 * 
 * @author Fox Mulder
 */
public class ValidationDeclarationException extends IllegalStateException {
    private static final long serialVersionUID = 957593746081440494L;
    
    /**
     * <code>Validation</code> annotation.
     */
    private Annotation annotation;
    
    /**
     * Constructs a new <code>ValidationDeclarationException</code> with the
     * specified annotation and detail message.
     * 
     * @param annotation <code>Validation</code> annotation.
     * @param message Detail message.
     */
    public ValidationDeclarationException(Annotation annotation, String message) {
        super(message);
        this.annotation = annotation;
    }
    
    /**
     * Constructs a new <code>ValidationDeclarationException</code> with the
     * specified annotation, detail message and cause.
     * 
     * @param annotation <code>Validation</code> annotation.
     * @param message Detail message.
     * @param cause Cause to be wrapped.
     */
    public ValidationDeclarationException(Annotation annotation, String message, Throwable cause) {
        super(message, cause);
        this.annotation = annotation;
    }
    
    /**
     * Constructs a new <code>ValidationDeclarationException</code> with the
     * specified annotation and cause.
     * 
     * @param annotation <code>Validation</code> annotation.
     * @param cause Cause to be wrapped.
     */
    public ValidationDeclarationException(Annotation annotation, Throwable cause) {
        super(cause);
        this.annotation = annotation;
    }
    
    /**
     * Returns <code>Validation</code> annotation.
     * 
     * @return <code>Validation</code> annotation.
     */
    public Annotation getAnnotation() {
        return annotation;
    }
    
}
