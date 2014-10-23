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

import java.util.Map;
import java.util.HashMap;

import org.foxlabs.validation.Validation;
import org.foxlabs.validation.ValidationContext;

/**
 * This class provides base <code>MessageBuilder</code> implementation.
 * 
 * @author Fox Mulder
 */
public abstract class AbstractMessageBuilder implements MessageBuilder {
    
    /**
     * Builds localized error message for the specified validation component
     * and context.
     * 
     * @param component Validation component.
     * @param context Validation context.
     * @return Localized error message for the specified validation component
     *         and context.
     */
    @Override
    public final String buildMessage(Validation<?> component, ValidationContext<?> context) {
        String template = component.getMessageTemplate(context);
        if (template != null) {
            String message = template;
            Map<String, Object> arguments = new HashMap<String, Object>(4);
            if (component.appendMessageArguments(context, arguments))
                message = renderTemplate(template, arguments, context);
            message = message.trim();
            if (message.length() > 0)
                return message;
        }
        return null;
    }
    
    /**
     * Renders the specified message template using the specified arguments.
     * 
     * @param template Message template.
     * @param arguments Arguments to be substituted into the message template.
     * @param context Validation context.
     * @return Rendered message.
     */
    protected abstract String renderTemplate(String template, Map<String, Object> arguments,
            ValidationContext<?> context);
    
}
