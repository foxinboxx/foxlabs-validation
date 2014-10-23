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
import java.util.logging.Level;

import static java.util.logging.Level.*;

import org.foxlabs.validation.ValidationContext;

import org.foxlabs.util.resource.MessageBundle;

/**
 * This class provides <code>Converter</code> implementation for the
 * <code>java.util.logging.Level</code> type.
 * 
 * @author Fox Mulder
 */
public final class LogLevelConverter extends AbstractConverter<Level> {
    
    /**
     * <code>LogLevelConverter</code> single instance.
     */
    public static final LogLevelConverter DEFAULT = new LogLevelConverter();
    
    /**
     * Array of known levels.
     */
    private static final Level[] KNOWN_LEVELS = new Level[]{
        OFF, SEVERE, WARNING, INFO, CONFIG, FINE, FINER, FINEST, ALL
    };
    
    /**
     * <code>MessageBundle</code> that contains localized definitions of known
     * log levels.
     */
    private static final MessageBundle LEVEL_BUNDLE = MessageBundle.getInstance(ALL.getResourceBundleName());
    
    /**
     * Constructs default <code>LogLevelConverter</code>.
     */
    private LogLevelConverter() {}
    
    /**
     * Returns <code>java.util.logging.Level</code> type.
     * 
     * @return <code>java.util.logging.Level</code> type.
     */
    @Override
    public Class<Level> getType() {
        return Level.class;
    }
    
    /**
     * Appends <code>levels</code> argument that contains set of known log
     * levels.
     * 
     * @param context Validation context.
     * @param arguments Arguments to be substituted into the error message
     *        template.
     * @return <code>true</code>.
     */
    @Override
    public boolean appendMessageArguments(ValidationContext<?> context, Map<String, Object> arguments) {
        super.appendMessageArguments(context, arguments);
        String[] levels = new String[KNOWN_LEVELS.length];
        for (int i = 0; i < KNOWN_LEVELS.length; i++)
            levels[i] = doEncode(KNOWN_LEVELS[i], context);
        arguments.put("levels", levels);
        return true;
    }
    
    /**
     * Converts string representation of log level into
     * <code>java.util.logging.Level</code> object.
     * 
     * @param value String representation of log level.
     * @param context Validation context.
     * @return Decoded <code>java.util.logging.Level</code> object.
     * @throws MalformedValueException if the specified string could not be
     *         parsed as a log level.
     */
    @Override
    protected <T> Level doDecode(String value, ValidationContext<T> context) {
        if (context.isLocalizedConvert())
            for (Level level : KNOWN_LEVELS)
                if (value.equalsIgnoreCase(doEncode(level, context)))
                    return level;
        
        for (Level level : KNOWN_LEVELS)
            if (value.equalsIgnoreCase(level.getName()))
                return level;
        
        try {
            int number = Integer.valueOf(value);
            for (Level level : KNOWN_LEVELS)
                if (level.intValue() == number)
                    return level;
        } catch (NumberFormatException e) {}
        
        throw new MalformedValueException(this, context, value);
    }
    
    /**
     * Converts <code>java.util.logging.Level</code> object into string
     * representation.
     * 
     * @param value <code>java.util.logging.Level</code> object to be encoded.
     * @param context Validation context.
     * @return String representation of log level.
     */
    @Override
    protected <T> String doEncode(Level value, ValidationContext<T> context) {
        if (context.isLocalizedConvert()) {
            try {
                return LEVEL_BUNDLE.get(value.getName(), context.getMessageLocale());
            } catch (MissingResourceException e) {}
        }
        return value.getName();
    }
    
}
