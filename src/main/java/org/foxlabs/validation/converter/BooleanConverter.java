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

package org.foxlabs.validation.converter;

import java.util.Map;
import java.util.MissingResourceException;

import org.foxlabs.validation.ValidationContext;

import org.foxlabs.util.Assert;

/**
 * This class provides <code>Converter</code> implementation for the
 * <code>java.lang.Boolean</code> type.
 * 
 * @author Fox Mulder
 * @see ConverterFactory#forBoolean(Class, String)
 */
public final class BooleanConverter extends AbstractConverter<Boolean> {
    
    /**
     * <code>BooleanConverter</code> primitive type instance.
     */
    public static final BooleanConverter SIMPLE = new BooleanConverter(
            Boolean.TYPE, Boolean.class.getName());
    
    /**
     * <code>BooleanConverter</code> object type instance.
     */
    public static final BooleanConverter OBJECT = new BooleanConverter(
            Boolean.class, Boolean.class.getName());
    
    /**
     * Default string representations of boolean constants (odd elements are
     * TRUE constants, even elements are FALSE constants).
     */
    private static final String[] DEFAULT_CONSTANTS = new String[]{
        "true", "false", "yes", "no", "y", "n", "on", "off", "1", "0"
    };
    
    /**
     * Boolean type (can be either primitive or object type).
     */
    private final Class<Boolean> type;
    
    /**
     * Message key for TRUE constant.
     */
    private final String trueKey;
    
    /**
     * Message key for FALSE constant.
     */
    private final String falseKey;
    
    /**
     * Constructs a new <code>BooleanConverter</code> with the specified boolean
     * type and prefix.
     * 
     * @param type Boolean type.
     * @param prefix Prefix of message keys of boolean constants.
     * @throws IllegalArgumentException if the specified type or prefix is
     *         <code>null</code>.
     */
    BooleanConverter(Class<Boolean> type, String prefix) {
        Assert.notNull(prefix, "prefix");
        this.type = Assert.notNull(type, "type");
        this.trueKey = prefix + ".TRUE";
        this.falseKey = prefix + ".FALSE";
    }
    
    /**
     * Returns <code>java.lang.Boolean</code> type.
     * 
     * @return <code>java.lang.Boolean</code> type.
     */
    @Override
    public Class<Boolean> getType() {
        return type;
    }
    
    /**
     * Appends <code>constants</code> argument that contains set of allowed
     * boolean constants.
     * 
     * @param context Validation context.
     * @param arguments Arguments to be substituted into the error message
     *        template.
     * @return <code>true</code>.
     */
    @Override
    public boolean appendMessageArguments(ValidationContext<?> context, Map<String, Object> arguments) {
        super.appendMessageArguments(context, arguments);
        arguments.put("constants", context.isLocalizedConvert()
                ? new String[]{doEncode(Boolean.TRUE, context), doEncode(Boolean.FALSE, context)}
                : DEFAULT_CONSTANTS);
        return true;
    }
    
    /**
     * Converts string representation of boolean value into
     * <code>java.lang.Boolean</code> object.
     * 
     * @param value String representation of boolean value.
     * @param context Validation context.
     * @return Decoded <code>java.lang.Boolean</code> object.
     * @throws MalformedValueException if the specified string could not be
     *         parsed as a boolean.
     */
    @Override
    protected <T> Boolean doDecode(String value, ValidationContext<T> context) {
        if (context.isLocalizedConvert()) {
            if (value.equalsIgnoreCase(doEncode(Boolean.TRUE, context)))
                return Boolean.TRUE;
            if (value.equalsIgnoreCase(doEncode(Boolean.FALSE, context)))
                return Boolean.FALSE;
        }
        
        for (int i = 0; i < DEFAULT_CONSTANTS.length; i++)
            if (DEFAULT_CONSTANTS[i].equalsIgnoreCase(value))
                return i % 2 == 0 ? Boolean.TRUE : Boolean.FALSE;
        
        throw new MalformedValueException(this, context, value);
    }
    
    /**
     * Converts <code>java.lang.Boolean</code> object into string
     * representation.
     * 
     * @param value <code>java.lang.Boolean</code> object to be encoded.
     * @param context Validation context.
     * @return String representation of boolean value.
     */
    @Override
    protected <T> String doEncode(Boolean value, ValidationContext<T> context) {
        if (context.isLocalizedConvert()) {
            try {
                return context.resolveMessage(value ? trueKey : falseKey);
            } catch (MissingResourceException e) {}
        }
        
        return DEFAULT_CONSTANTS[value ? 0 : 1];
    }
    
}
