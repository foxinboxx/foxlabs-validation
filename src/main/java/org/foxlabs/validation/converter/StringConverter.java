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

import org.foxlabs.validation.ValidationContext;

/**
 * This class provides <code>Converter</code> implementation for the
 * <code>java.lang.String</code> type.
 * 
 * @author Fox Mulder
 */
public final class StringConverter extends AbstractConverter<String> {
    
    /**
     * <code>StringConverter</code> single instance.
     */
    public static final StringConverter DEFAULT = new StringConverter();
    
    /**
     * Constructs default <code>StringConverter</code>.
     */
    private StringConverter() {}
    
    /**
     * Returns <code>java.lang.String</code> type.
     * 
     * @return <code>java.lang.String</code> type.
     */
    @Override
    public Class<String> getType() {
        return String.class;
    }
    
    /**
     * <code>StringConverter</code> has no error message.
     * 
     * @param context Validation context.
     * @return <code>null</code>.
     */
    @Override
    public String getMessageTemplate(ValidationContext<?> context) {
        return null;
    }
    
    /**
     * Returns value as is.
     * 
     * @param value String value.
     * @param context Validation context.
     * @return Value as is.
     */
    @Override
    public <T> String decode(String value, ValidationContext<T> context) {
        return value;
    }
    
    /**
     * Returns empty string if value is <code>null</code>; value as is otherwise.
     * 
     * @param value String value.
     * @param context Validation context.
     * @return Empty string if value is <code>null</code>; value as is otherwise.
     */
    @Override
    public <T> String encode(String value, ValidationContext<T> context) {
        return value == null ? "" : value;
    }
    
}
