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

import java.util.Map;

/**
 * This class provides base implementation of the <code>Validation</code>
 * interface.
 * 
 * @author Fox Mulder
 * @param <V> The type of value to be validated
 */
public abstract class AbstractValidation<V> implements Validation<V> {
    
    /**
     * Returns localized error message template using class name as message key.
     * 
     * @param context Validation context.
     * @return Localized error message template using class name as message key.
     */
    @Override
    public String getMessageTemplate(ValidationContext<?> context) {
        return context.resolveMessage(getClass().getName());
    }
    
    /**
     * Appends <code>context</code> argument that contains current validation
     * context.
     * 
     * @param context Validation context.
     * @param arguments Arguments to be substituted into the error message
     *        template.
     * @return <code>true</code>.
     */
    @Override
    public boolean appendMessageArguments(ValidationContext<?> context, Map<String, Object> arguments) {
        arguments.put("context", context);
        return true;
    }
    
}
