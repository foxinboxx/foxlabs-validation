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
 * Thrown to indicate that <code>ValidationTarget</code> defined by the
 * <code>targets</code> property of the annotation can't be applied to the
 * annotated element.
 * 
 * @author Fox Mulder
 * @see ValidationTarget
 */
public class ValidationTargetException extends ValidationDeclarationException {
    private static final long serialVersionUID = -8022962008726402331L;
    
    /**
     * Constructs a new <code>ValidationTargetException</code> with the
     * specified annotation and target.
     * 
     * @param annotation <code>Validation</code> annotation.
     * @param target <code>ValidationTarget</code> constant.
     */
    public ValidationTargetException(Annotation annotation, ValidationTarget target) {
        super(annotation, "Illegal reference to the target " + target +
                " from validation annotation @" + annotation.annotationType().getName());
    }
    
}
