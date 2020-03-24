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
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.foxlabs.validation.ValidationContext;
import org.foxlabs.validation.ValidationTarget;

import org.foxlabs.common.Checks;

import org.foxlabs.util.reflect.Types;

/**
 * This class provides <code>Converter</code> implementation for all JDK
 * <code>java.util.Collection</code> types.
 *
 * @author Fox Mulder
 * @param <V> The type of collection elements
 * @see Tokenizer
 * @see ConverterFactory#forCollection(Class, Converter)
 * @see ConverterFactory#forCollection(Class, Converter, String)
 * @see ConverterFactory#forCollection(Class, Converter, Tokenizer)
 */
public final class CollectionConverter<V> extends SequenceConverter<Collection<V>> {

    /**
     * Collection type.
     */
    private final Class<Collection<V>> type;

    /**
     * Converter of collection elements.
     */
    private final Converter<V> converter;

    /**
     * Constructs a new <code>CollectionConverter</code> with the specified
     * collection type, converter and tokenizer of collection elements.
     *
     * @param type Collection type.
     * @param converter Converter of collection elements.
     * @param tokenizer Tokenizer of collection elements.
     * @throws IllegalArgumentException if the specified type or converter or
     *         tokenizer is <code>null</code>.
     */
    CollectionConverter(Class<? extends Collection<?>> type,
            Converter<V> converter, Tokenizer tokenizer) {
        super(tokenizer);
        this.type = Types.cast(Checks.checkNotNull(type, "type"));
        this.converter = Checks.checkNotNull(converter, "converter");
    }

    /**
     * Returns collection type.
     *
     * @return Collection type.
     */
    @Override
    public Class<Collection<V>> getType() {
        return type;
    }

    /**
     * Appends <code>converter</code> argument that contains converter of
     * collection elements.
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
     * Converts string representations of collection elements into collection.
     *
     * @param tokens String representations of collection elements.
     * @param context Validation context.
     * @return Decoded collection.
     * @throws MalformedValueException if conversion of collection elements
     *         fails.
     */
    @Override
    protected <T> Collection<V> doDecode(String[] tokens, ValidationContext<T> context,
            List<MalformedValueException> violations) {
        Collection<V> collection = Types.newCollection(type, tokens.length);
        context.setCurrentTarget(ValidationTarget.ELEMENTS);
        for (int i = 0; i < tokens.length; i++) {
            try {
                context.setCurrentIndex(i);
                collection.add(converter.decode(tokens[i], context));
            } catch (MalformedValueException e) {
                violations.add(e);
            }
        }
        return collection;
    }

    /**
     * Converts collection into string representation of collection elements.
     *
     * @param value Collection to be encoded.
     * @param context Validation context.
     * @return String representation of collection elements.
     */
    @Override
    public <T> String encode(Collection<V> value, ValidationContext<T> context) {
        int count = value == null ? 0 : value.size();
        if (count == 0)
            return tokenizer.encode(Tokenizer.EMPTY_TOKEN_ARRAY, context);
        String[] tokens = new String[count];
        Iterator<V> itr = value.iterator();
        for (int i = 0; i < tokens.length; i++)
            tokens[i] = converter.encode(itr.next(), context);
        return tokenizer.encode(tokens, context);
    }

}
