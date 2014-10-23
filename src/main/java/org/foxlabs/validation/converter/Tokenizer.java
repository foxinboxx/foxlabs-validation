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
 * This class provides base <code>Converter</code> implementation for the
 * string array type. In other words, this class is a string tokenizer that
 * splits one string into array of strings and vice versa.
 * 
 * @author Fox Mulder
 */
public abstract class Tokenizer extends AbstractConverter<String[]> {
    
    /**
     * Empty token array.
     */
    public static final String[] EMPTY_TOKEN_ARRAY = new String[0];
    
    /**
     * Returns string array type.
     * 
     * @return String array type.
     */
    @Override
    public final Class<String[]> getType() {
        return String[].class;
    }
    
    /**
     * Returns array of tokens extracted from the source string.
     * 
     * <p>Invokes {@link #doDecode(String, ValidationContext)} if source string
     * is not <code>null</code> or empty; returns empty token array otherwise.
     * </p>
     * 
     * @param value Source string to be tokenized.
     * @param context Validation context.
     * @return Array of tokens extracted from the source string.
     * @throws MalformedValueException if conversion fails.
     * @see #doDecode(String, ValidationContext)
     */
    @Override
    public <T> String[] decode(String value, ValidationContext<T> context) {
        return value == null || value.isEmpty() ? EMPTY_TOKEN_ARRAY : doDecode(value, context);
    }
    
    /**
     * Builds string from the specified array of tokens.
     * 
     * <p>Invokes {@link #doEncode(Object, ValidationContext)} if array of
     * tokens is not <code>null</code> or empty; returns empty string otherwise.
     * </p>
     * 
     * @param value Array of tokens to be encoded.
     * @param context Validation context.
     * @return String from the specified array of tokens.
     * @see #doEncode(Object, ValidationContext)
     */
    @Override
    public <T> String encode(String[] value, ValidationContext<T> context) {
        return value == null || value.length == 0 ? "" : doEncode(value, context);
    }
    
}
