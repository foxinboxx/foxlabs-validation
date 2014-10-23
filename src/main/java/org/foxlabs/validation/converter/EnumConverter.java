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
 * This class provides <code>Converter</code> implementation for all
 * <code>java.lang.Enum</code> enumeration types.
 * 
 * @author Fox Mulder
 * @param <V> Enumeration type
 * @see ConverterFactory#forEnum(Class, String)
 */
public final class EnumConverter<V extends Enum<V>> extends AbstractConverter<V> {
    
    /**
     * Enumeration type.
     */
    private final Class<V> type;
    
    /**
     * Array of enumeration constants.
     */
    private final V[] constants;
    
    /**
     * Message keys for enumeration constants.
     */
    private final String[] keys;
    
    /**
     * Constructs a new <code>EnumConverter</code> for the specified
     * enumeration type and prefix.
     * 
     * @param type Enumeration type.
     * @param prefix Prefix of message keys of enumeration constants.
     * @throws IllegalArgumentException if the specified type or prefix is
     *         <code>null</code>.
     */
    EnumConverter(Class<V> type, String prefix) {
        Assert.notNull(prefix, "prefix");
        this.type = Assert.notNull(type, "type");
        this.constants = type.getEnumConstants();
        this.keys = new String[this.constants.length];
        for (int i = 0; i < this.keys.length; i++)
            this.keys[i] = prefix + "." + this.constants[i].name();
    }
    
    /**
     * Returns enumeration type.
     * 
     * @return Enumeration type.
     */
    @Override
    public Class<V> getType() {
        return type;
    }
    
    /**
     * Appends <code>constants</code> argument that contains set of allowed
     * enumeration constants.
     * 
     * @param context Validation context.
     * @param arguments Arguments to be substituted into the error message
     *        template.
     * @return <code>true</code>.
     */
    @Override
    public boolean appendMessageArguments(ValidationContext<?> context, Map<String, Object> arguments) {
        super.appendMessageArguments(context, arguments);
        String[] argument = new String[constants.length];
        for (int i = 0; i < constants.length; i++)
            argument[i] = doEncode(constants[i], context);
        arguments.put("constants", argument);
        return true;
    }
    
    /**
     * Converts string representation of enumeration value into
     * <code>java.lang.Enum</code> object.
     * 
     * @param value String representation of enumeration value.
     * @param context Validation context.
     * @return Decoded <code>java.lang.Enum</code> object.
     * @throws MalformedValueException if the specified string could not be
     *         parsed as an enumeration constant.
     */
    @Override
    protected <T> V doDecode(String value, ValidationContext<T> context) {
        if (context.isLocalizedConvert())
            for (V constant : constants)
                if (doEncode(constant, context).equalsIgnoreCase(value))
                    return constant;
        
        try {
            return Enum.valueOf(type, value);
        } catch (IllegalArgumentException e) {}
        
        try {
            int ordinal = Integer.valueOf(value);
            if (ordinal >= 0 && ordinal < constants.length)
                return constants[ordinal];
        } catch (NumberFormatException e) {}
        
        throw new MalformedValueException(this, context, value);
    }
    
    /**
     * Converts <code>java.lang.Enum</code> object into string representation.
     * 
     * @param value <code>java.lang.Enum</code> object to be encoded.
     * @param context Validation context.
     * @return String representation of enumeration value.
     */
    @Override
    protected <T> String doEncode(V value, ValidationContext<T> context) {
        if (context.isLocalizedConvert()) {
            try {
                return context.resolveMessage(keys[value.ordinal()]);
            } catch (MissingResourceException e) {}
        }
        
        return value.name();
    }
    
}
