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

import org.foxlabs.util.Assert;

/**
 * This class provides <code>Converter</code> implementation for the
 * <code>java.lang.Character</code> type.
 * 
 * @author Fox Mulder
 */
public final class CharacterConverter extends AbstractConverter<Character> {
    
    /**
     * <code>CharacterConverter</code> primitive type instance.
     */
    public static final CharacterConverter SIMPLE = new CharacterConverter(Character.TYPE);
    
    /**
     * <code>CharacterConverter</code> object type instance.
     */
    public static final CharacterConverter OBJECT = new CharacterConverter(Character.class);
    
    /**
     * Character type (can be either primitive or object type).
     */
    private final Class<Character> type;
    
    /**
     * Constructs default <code>CharacterConverter</code>.
     * 
     * @param type Character type.
     * @throws IllegalArgumentException if the specified type is
     *         <code>null</code>.
     */
    private CharacterConverter(Class<Character> type) {
        this.type = Assert.notNull(type, "type");
    }
    
    /**
     * Returns <code>java.lang.Character</code> type.
     * 
     * @return <code>java.lang.Character</code> type.
     */
    @Override
    public Class<Character> getType() {
        return type;
    }
    
    /**
     * Converts string representation of character value into
     * <code>java.lang.Character</code> object.
     * 
     * @param value String representation of character value.
     * @return Decoded <code>java.lang.Character</code> object.
     * @throws MalformedValueException if the specified string could not be
     *         parsed as a character.
     */
    @Override
    protected <T> Character doDecode(String value, ValidationContext<T> context) {
        if (value.length() == 1)
            return value.charAt(0);
        throw new MalformedValueException(this, context, value);
    }
    
}
