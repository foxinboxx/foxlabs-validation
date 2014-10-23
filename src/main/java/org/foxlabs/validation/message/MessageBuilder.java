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

package org.foxlabs.validation.message;

import org.foxlabs.validation.Validation;
import org.foxlabs.validation.ValidationContext;

/**
 * Defines interface that allows to build localized error messages of
 * validation components.
 * 
 * @author Fox Mulder
 */
public interface MessageBuilder {
    
    /**
     * <code>MessageBuilder</code> default instance.
     */
    MessageBuilder DEFAULT = new DefaultMessageBuilder();
    
    /**
     * Builds localized error message for the specified validation component
     * and context.
     * 
     * @param component Validation component.
     * @param context Validation context.
     * @return Localized error message for the specified validation component
     *         and context.
     */
    String buildMessage(Validation<?> component, ValidationContext<?> context);
    
}
