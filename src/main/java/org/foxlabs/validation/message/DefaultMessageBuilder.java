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
import java.util.List;
import java.util.ArrayList;

import org.foxlabs.validation.Validation;
import org.foxlabs.validation.ValidationContext;
import org.foxlabs.validation.converter.Converter;
import org.foxlabs.validation.converter.ConverterFactory;

import org.foxlabs.util.reflect.Types;

/**
 * This class provides default <code>MessageBuilder</code> implementation.
 * 
 * <p>The following rules are applied to error message templates:</p>
 * <ul>
 *   <li>The <code>\</code> character is considered as escape character.</li>
 *   <li>The string enclosed into <code>{}</code> characters is considered as
 *       optional argument.</li>
 *   <li>If value of optional argument is <code>null</code> then it will be
 *       replaced with empty string; if value is not defined then no
 *       replacement will occur.</li>
 *   <li>The string enclosed into <code>&lt;&gt;</code> characters is considered as
 *       required argument.</li>
 *   <li>If value of required argument is not defined or <code>null</code> then
 *       whole template will be rendered as <code>null</code>.</li>
 *   <li>The string enclosed into <code>()</code> characters is considered as
 *       subtemplate.</li>
 *   <li>If subtemplate is <code>null</code> then it will be replaced with
 *       empty string.</li>
 *   <li>Argument names must not contain <code>\{}&lt;&gt;()</code> characters.</li>
 * </ul>
 * 
 * @author Fox Mulder
 */
public class DefaultMessageBuilder extends AbstractMessageBuilder {
    
    /**
     * Escape character.
     */
    private static final char ESCAPE = '\\';
    
    /**
     * Left brackets of message argument.
     */
    private static final String LBRACKETS = "{<(";
    
    /**
     * Right brackets of message argument.
     */
    private static final String RBRACKETS = "}>)";
    
    /**
     * Error marker.
     */
    private static final String ERROR = " ---> ";
    
    /**
     * Constructs default <code>DefaultMessageBuilder</code>.
     */
    protected DefaultMessageBuilder() {}
    
    /**
     * Renders the specified message template using the specified arguments.
     * 
     * @param template Message template.
     * @param arguments Arguments to be substituted into the message template.
     * @param context Validation context.
     * @return Rendered message.
     */
    @Override
    protected String renderTemplate(String template, Map<String, Object> arguments,
            ValidationContext<?> context) {
        int length = template.length();
        StringBuilder message = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            char ch = template.charAt(i);
            if (ch == ESCAPE) {
                if (++i < length)
                    ch = template.charAt(i);
                message.append(ch);
            } else {
                int bracket = LBRACKETS.indexOf(ch);
                if (bracket < 0) {
                    message.append(ch);
                } else {
                    int start = i++;
                    char rb = RBRACKETS.charAt(bracket);
                    if (bracket == 2) {
                        char lb = ch;
                        int level = 1;
                        for (; i < length && level > 0; i++) {
                            ch = template.charAt(i);
                            if (ch == ESCAPE) {
                                i++;
                            } else if (ch == rb) {
                                level--;
                            } else if (ch == lb) {
                                level++;
                            }
                        }
                        if (level > 0) {
                            message.append(ERROR).append(template.substring(start));
                        } else {
                            String subtemplate = template.substring(start + 1, --i);
                            message.append(renderTemplate(subtemplate, arguments, context));
                        }
                    } else {
                        for (; i < length && ch != rb; i++) {
                            ch = template.charAt(i);
                            if (ch != rb && isSpecialChar(ch)) {
                                message.append(template.substring(start, i))
                                       .append(ERROR)
                                       .append(template.substring(i));
                                return message.toString();
                            }
                        }
                        if (ch == rb) {
                            String key = template.substring(start + 1, --i);
                            String value = renderArgument(arguments.get(key), context);
                            if (value != null) {
                                message.append(value);
                            } else if (bracket == 1) {
                                return "";
                            } else if (!arguments.containsKey(key)) {
                                message.append(template.substring(start, i));
                            }
                        } else {
                            message.append(ERROR).append(template.substring(start));
                        }
                    }
                }
            }
        }
        return message.toString();
    }
    
    /**
     * Determines if the specified character is special.
     * 
     * @param ch Character to be tested.
     * @return <code>true</code> if the specified character is special;
     *         <code>false</code> otherwise.
     */
    private static boolean isSpecialChar(char ch) {
        return ch == ESCAPE || LBRACKETS.indexOf(ch) >= 0 || RBRACKETS.indexOf(ch) >= 0;
    }
    
    /**
     * Renders the specified message argument.
     * 
     * @param value Message argument to be rendered.
     * @param context Validation context.
     * @return String representation of the specified message argument.
     */
    protected String renderArgument(Object value, ValidationContext<?> context) {
        if (value == null || value instanceof String)
            return (String) value;
        if (value instanceof ValidationContext)
            return renderContext((ValidationContext<?>) value);
        if (value instanceof Validation)
            return buildMessage((Validation<?>) value, context);
        if (value.getClass().isArray())
            if (Validation.class.isAssignableFrom(value.getClass().getComponentType()))
                return renderComponents((Validation<?>[]) value, context);
        return renderValue(value, context);
    }
    
    /**
     * Renders the specified null-safe value. This method uses default
     * converter for value rendering.
     * 
     * @param value Value to be rendered.
     * @param context Validation context.
     * @return String representation of the specified value.
     */
    protected String renderValue(Object value, ValidationContext<?> context) {
        Class<Object> type = Types.cast(value.getClass());
        Converter<Object> converter = ConverterFactory.getDefaultConverter(type);
        return context.getValidator()
                      .newContext()
                      .setLocalizedConvert(true)
                      .setMessageLocale(context.getMessageLocale())
                      .encodeValue(converter, value);
    }
    
    /**
     * Renders the specified array of validation components. This method
     * concatenates error messages of the specified validation components and
     * may return <code>null</code> if there are no messages available.
     * 
     * @param components Array of validation components to be rendered.
     * @param context Validation context.
     * @return Error messages of the specified validation components.
     */
    protected String renderComponents(Validation<?>[] components, ValidationContext<?> context) {
        List<String> messages = new ArrayList<String>();
        for (Validation<?> component : components) {
            String message = buildMessage(component, context);
            if (message != null)
                messages.add(message);
        }
        return messages.isEmpty() ? null : renderValue(messages, context);
    }
    
    /**
     * Renders the specified validation context. This method simply returns
     * <code>context.toString()</code>.
     * 
     * @param context Validation context to be rendered.
     * @return String representation of the specified validation context.
     */
    protected String renderContext(ValidationContext<?> context) {
        return context.toString();
    }
    
}
