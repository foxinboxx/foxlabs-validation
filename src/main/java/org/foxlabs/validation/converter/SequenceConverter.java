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

import java.util.List;
import java.util.LinkedList;
import java.util.Map;

import org.foxlabs.validation.AbstractValidation;
import org.foxlabs.validation.ValidationContext;
import org.foxlabs.validation.ValidationException;
import org.foxlabs.validation.ValidationTarget;

import org.foxlabs.util.Assert;

/**
 * This class provides base <code>Converter</code> implementation for
 * converters of elements sequence.
 * 
 * @author Fox Mulder
 * @see Tokenizer
 */
public abstract class SequenceConverter<V> extends AbstractValidation<V> implements Converter<V> {
    
    /**
     * Tokenizer of elements sequence.
     */
    protected final Tokenizer tokenizer;
    
    /**
     * Constructs a new <code>SequenceConverter</code> with the specified
     * tokenizer of elements sequence.
     * 
     * @param tokenizer Tokenizer of elements sequence.
     * @throws IllegalArgumentException if the specified tokenizer is
     *         <code>null</code>.
     */
    protected SequenceConverter(Tokenizer tokenizer) {
        this.tokenizer = Assert.notNull(tokenizer, "tokenizer");
    }
    
    /**
     * Appends <code>tokenizer</code> argument that contains tokenizer of
     * elements sequence.
     * 
     * @param context Validation context.
     * @param arguments Arguments to be substituted into the error message
     *        template.
     * @return <code>true</code>.
     */
    @Override
    public boolean appendMessageArguments(ValidationContext<?> context, Map<String, Object> arguments) {
        super.appendMessageArguments(context, arguments);
        arguments.put("tokenizer", tokenizer);
        return true;
    }
    
    /**
     * Converts string representation of elements sequence into object.
     * 
     * @param <T> The type of validated entity.
     * @param value String representation of elements sequence.
     * @param context Validation context.
     * @return Decoded elements sequence.
     * @throws MalformedValueException if conversion fails.
     */
    @Override
    public <T> V decode(String value, ValidationContext<T> context) {
        V sequence = null;
        String[] tokens = tokenize(value, context);
        Object index = context.getCurrentIndex();
        ValidationTarget target = context.getCurrentTarget();
        List<MalformedValueException> violations = new LinkedList<MalformedValueException>();
        try {
            sequence = doDecode(tokens, context, violations);
        } finally {
            context.setCurrentIndex(index);
            context.setCurrentTarget(target);
        }
        if (violations.isEmpty())
            return sequence;
        throw new MalformedValueException(this, context, value, new ValidationException(violations));
    }
    
    /**
     * Returns array of tokens extracted from the source string.
     * 
     * @param <T> The type of validated entity.
     * @param value Source string to be tokenized.
     * @param context Validation context.
     * @return Array of tokens extracted from the source string.
     * @throws MalformedValueException if conversion fails.
     * @see Tokenizer#decode(String, ValidationContext)
     */
    protected <T> String[] tokenize(String value, ValidationContext<T> context) {
        try {
            return tokenizer.decode(value, context);
        } catch (MalformedValueException e) {
            throw new MalformedValueException(this, context, value, new ValidationException(e));
        }
    }
    
    /**
     * Converts string representations of elements into elements sequence.
     * This method should not throw <code>MalformedValueException</code> and
     * all violations should be stored in the specified list of violations.
     * 
     * @param <T> The type of validated entity.
     * @param tokens String representations of elements.
     * @param context Validation context.
     * @param violations List of violations.
     * @return Decoded elements sequence.
     */
    protected abstract <T> V doDecode(String[] tokens, ValidationContext<T> context,
            List<MalformedValueException> violations);
    
}
