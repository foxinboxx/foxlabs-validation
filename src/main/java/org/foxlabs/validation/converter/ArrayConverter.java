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

import java.lang.reflect.Array;

import java.util.List;
import java.util.Map;

import org.foxlabs.validation.ValidationContext;
import org.foxlabs.validation.ValidationTarget;

import org.foxlabs.common.Checks;

import org.foxlabs.util.reflect.Types;

/**
 * This class provides <code>Converter</code> implementation for all
 * one-dimensional array types.
 *
 * @author Fox Mulder
 * @param <V> The type of array elements
 * @see Tokenizer
 * @see ConverterFactory#forArray(Converter)
 * @see ConverterFactory#forArray(Converter, String)
 * @see ConverterFactory#forArray(Converter, Tokenizer)
 */
public final class ArrayConverter<V> extends SequenceConverter<Object> {

    /**
     * Array type.
     */
    private final Class<Object> type;

    /**
     * Converter of array elements.
     */
    private final Converter<V> converter;

    /**
     * Constructs a new <code>ArrayConverter</code> with the specified
     * converter and tokenizer of array elements.
     *
     * @param converter Converter of array elements.
     * @param tokenizer Tokenizer of array elements.
     * @throws IllegalArgumentException if the specified converter or tokenizer
     *         is <code>null</code>.
     */
    ArrayConverter(Converter<V> converter, Tokenizer tokenizer) {
        super(tokenizer);
        this.converter = Checks.checkNotNull(converter, "converter");
        this.type = Types.arrayTypeOf(converter.getType());
    }

    /**
     * Returns array type.
     *
     * @return Array type.
     */
    @Override
    public Class<Object> getType() {
        return type;
    }

    /**
     * Appends <code>converter</code> argument that contains converter of array
     * elements.
     *
     * @param context Validation context.
     * @param arguments Arguments to be substituted into the error message
     *        template.
     * @return <code>true</code>.
     */
    @Override
    public boolean appendMessageArguments(ValidationContext<?> context, Map<String, Object> arguments) {
        super.appendMessageArguments(context, arguments);
        arguments.put("converter", converter);
        return true;
    }

    /**
     * Converts string representations of array elements into array.
     *
     * @param <T> The type of validated entity.
     * @param tokens String representations of array elements.
     * @param context Validation context.
     * @return Decoded array.
     * @throws MalformedValueException if conversion of array elements fails.
     */
    @Override
    protected <T> Object doDecode(String[] tokens, ValidationContext<T> context,
            List<MalformedValueException> violations) {
        Object array = Array.newInstance(converter.getType(), tokens.length);
        context.setCurrentTarget(ValidationTarget.ELEMENTS);
        for (int i = 0; i < tokens.length; i++) {
            try {
                context.setCurrentIndex(i);
                Array.set(array, i, converter.decode(tokens[i], context));
            } catch (MalformedValueException e) {
                violations.add(e);
            }
        }
        return array;
    }

    /**
     * Converts array into string representation of array elements.
     *
     * @param <T> The type of validated entity.
     * @param value Array to be encoded.
     * @param context Validation context.
     * @return String representation of array elements.
     */
    @Override
    @SuppressWarnings("unchecked")
    public <T> String encode(Object value, ValidationContext<T> context) {
        int count = value == null ? 0 : Array.getLength(value);
        if (count == 0)
            return tokenizer.encode(Tokenizer.EMPTY_TOKEN_ARRAY, context);
        String[] tokens = new String[count];
        for (int i = 0; i < count; i++)
            tokens[i] = converter.encode((V) Array.get(value, i), context);
        return tokenizer.encode(tokens, context);
    }

}
