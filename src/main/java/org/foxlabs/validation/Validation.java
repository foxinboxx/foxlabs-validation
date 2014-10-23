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
 * The root interface in the validation hierarchy. <code>Validation</code> is
 * an abstract component that allows somehow validate values and may generate
 * <code>ViolationException</code> if validation fails.
 * 
 * <p><code>Validation</code> implementations should be immutable.</p>
 * 
 * @author Fox Mulder
 * @param <V> The type of value to be validated
 * @see ValidationContext
 */
public interface Validation<V> {
    
    /**
     * Returns the type of value to be validated.
     * 
     * @return The type of value to be validated.
     */
    Class<?> getType();
    
    /**
     * Returns localized error message template.
     * 
     * <p>Note that this method may return <code>null</code>.</p>
     * 
     * @param context Validation context.
     * @return Localized error message template.
     */
    String getMessageTemplate(ValidationContext<?> context);
    
    /**
     * Determines if error message template returned by the method
     * {@link #getMessageTemplate(ValidationContext)} needs to be rendered and
     * appends arguments to be substituted into the error message template.
     * 
     * @param context Validation context.
     * @param arguments Arguments to be substituted into the error message
     *        template.
     * @return <code>true</code> if error message template needs to be rendered;
     *         <code>false</code> otherwise.
     */
    boolean appendMessageArguments(ValidationContext<?> context, Map<String, Object> arguments);
    
}
