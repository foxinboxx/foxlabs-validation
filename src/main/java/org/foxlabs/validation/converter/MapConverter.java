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

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.foxlabs.validation.ValidationContext;
import org.foxlabs.validation.ValidationException;
import org.foxlabs.validation.ValidationTarget;

import org.foxlabs.util.Assert;
import org.foxlabs.util.reflect.Types;

/**
 * This class provides <code>Converter</code> implementation for all JDK
 * <code>java.util.Map</code> types.
 * 
 * <p>All odd elements returned from tokenizer are map keys, all even elements
 * are map values.</p>
 * 
 * @author Fox Mulder
 * @param <K> The type of map keys
 * @param <V> The type of map values
 * @see Tokenizer
 * @see ConverterFactory#forMap(Class, Converter, Converter)
 * @see ConverterFactory#forMap(Class, Converter, Converter, String)
 * @see ConverterFactory#forMap(Class, Converter, Converter, Tokenizer)
 */
public final class MapConverter<K, V> extends SequenceConverter<Map<K, V>> {
    
    /**
     * Map type.
     */
    private final Class<Map<K, V>> type;
    
    /**
     * Converter of map keys.
     */
    private final Converter<K> kconverter;
    
    /**
     * Converter of map values.
     */
    private final Converter<V> vconverter;
    
    /**
     * Constructs a new <code>MapConverter</code> with the specified map type,
     * key and value converters, key-value pairs tokenizer.
     * 
     * @param type Map type.
     * @param kconverter Converter of map keys.
     * @param vconverter Converter of map values.
     * @param tokenizer Tokenizer of key-value pairs.
     * @throws IllegalArgumentException if the specified type or kconverter or
     *         vconverter or tokenizer is <code>null</code>.
     */
    MapConverter(Class<? extends Map<?, ?>> type,
            Converter<K> kconverter, Converter<V> vconverter,
            Tokenizer tokenizer) {
        super(tokenizer);
        this.type = Types.cast(Assert.notNull(type, "type"));
        this.kconverter = Assert.notNull(kconverter, "kconverter");
        this.vconverter = Assert.notNull(vconverter, "vconverter");
    }
    
    /**
     * Returns map type.
     * 
     * @return Map type.
     */
    @Override
    public Class<Map<K, V>> getType() {
        return type;
    }
    
    /**
     * Appends <code>kconverter</code> and <code>vconverter</code> arguments
     * that contain converter of map keys and converter of map values
     * respectively.
     * 
     * @param context Validation context.
     * @param arguments Arguments to be substituted into the error message
     *        template.
     * @return <code>true</code>.
     */
    @Override
    public boolean appendMessageArguments(ValidationContext<?> context, Map<String, Object> arguments) {
        super.appendMessageArguments(context, arguments);
        arguments.put("kconverter", kconverter);
        arguments.put("vconverter", vconverter);
        return true;
    }
    
    /**
     * Converts string representations of key-value pairs into map.
     * 
     * @param tokens String representations of key-value pairs.
     * @param context Validation context.
     * @return Decoded map.
     * @throws MalformedValueException if conversion of key-value pairs fails.
     */
    @Override
    protected <T> Map<K, V> doDecode(String[] tokens, ValidationContext<T> context,
            List<MalformedValueException> violations) {
        Map<K, V> map = Types.newMap(type, tokens.length);
        for (int i = 0; i < tokens.length;) {
            K key = null;
            V value = null;
            boolean success = true;
            context.setCurrentIndex(i / 2);
            try {
                context.setCurrentTarget(ValidationTarget.KEYS);
                key = kconverter.decode(tokens[i++], context);
            } catch (MalformedValueException e) {
                violations.add(e);
                success = false;
            }
            try {
                context.setCurrentTarget(ValidationTarget.ELEMENTS);
                value = vconverter.decode(tokens[i++], context);
            } catch (MalformedValueException e) {
                violations.add(e);
                success = false;
            }
            if (success)
                map.put(key, value);
        }
        return map;
    }
    
    /**
     * Returns array of tokens extracted from the source string. All odd
     * elements returned are map keys, all even elements are map values.
     * 
     * @param value Source string to be tokenized.
     * @param context Validation context.
     * @return Array of tokens extracted from the source string.
     * @throws MalformedValueException if conversion fails.
     */
    @Override
    protected <T> String[] tokenize(String value, ValidationContext<T> context) {
        String[] tokens = super.tokenize(value, context);
        if (tokens.length % 2 > 0)
            throw new MalformedValueException(this, context, value,
                    new ValidationException(new MalformedValueException(tokenizer, context, value)));
        return tokens;
    }
    
    /**
     * Converts map into string representation of key-value pairs.
     * 
     * @param value Map to be encoded.
     * @param context Validation context.
     * @return String representation of key-value pairs.
     */
    @Override
    public <T> String encode(Map<K, V> value, ValidationContext<T> context) {
        int count = value == null ? 0 : value.size();
        if (count == 0)
            return tokenizer.encode(Tokenizer.EMPTY_TOKEN_ARRAY, context);
        String[] tokens = new String[count * 2];
        Iterator<Map.Entry<K, V>> itr = value.entrySet().iterator();
        for (int i = 0; i < tokens.length;) {
            Map.Entry<K, V> entry = itr.next();
            tokens[i++] = kconverter.encode(entry.getKey(), context);
            tokens[i++] = vconverter.encode(entry.getValue(), context);
        }
        return tokenizer.encode(tokens, context);
    }
    
}
