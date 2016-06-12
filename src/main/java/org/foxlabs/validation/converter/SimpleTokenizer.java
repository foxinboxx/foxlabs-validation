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

import org.foxlabs.validation.ValidationContext;

import org.foxlabs.util.Assert;
import org.foxlabs.util.Strings;

/**
 * This class provides simple <code>Tokenizer</code> implementation based on
 * delimiter characters like <code>java.util.StringTokenizer</code>.
 * 
 * @author Fox Mulder
 */
public final class SimpleTokenizer extends Tokenizer {
    
    /**
     * <code>SimpleTokenizer</code> default instance initialized with
     * <code>,;| \t\n\r</code> elements delimiters.
     */
    public static final SimpleTokenizer DEFAULT = new SimpleTokenizer(",;| \t\n\r");
    
    /**
     * Allowed elements delimiter characters.
     */
    private final String delims;
    
    /**
     * Constructs a new <code>SimpleTokenizer</code> with the specified
     * elements delimiters.
     * 
     * @param delims Allowed elements delimiter characters.
     * @throws IllegalArgumentException if the specified delimiters is
     *         <code>null</code> or empty string.
     */
    SimpleTokenizer(String delims) {
        this.delims = Assert.notEmpty(delims, "delims");
    }
    
    /**
     * Constructs a new <code>SimpleTokenizer</code> from the specified
     * annotation.
     * 
     * @param annotation Tokenizer annotation.
     * @throws IllegalArgumentException if the specified annotation defines
     *         empty delimiters.
     */
    SimpleTokenizer(TokenDelimiters annotation) {
        this(annotation.value());
    }
    
    /**
     * Appends <code>delims</code> argument that contains allowed elements
     * delimiter characters.
     * 
     * @param context Validation context.
     * @param arguments Arguments to be substituted into the error message
     *        template.
     * @return <code>true</code>.
     */
    @Override
    public boolean appendMessageArguments(ValidationContext<?> context, Map<String, Object> arguments) {
        super.appendMessageArguments(context, arguments);
        arguments.put("delims", Strings.escape(delims));
        return true;
    }
    
    /**
     * Returns array of tokens extracted from the source string.
     * 
     * @param value Source string to be tokenized.
     * @param context Validation context.
     * @return Array of tokens extracted from the source string.
     */
    @Override
    protected <T> String[] doDecode(String value, ValidationContext<T> context) {
        List<String> tokens = new LinkedList<String>();
        boolean delim = false;
        int start = 0, nulls = 0, length = value.length();
        for (int i = 0; i < length; i++) {
            char ch = value.charAt(i);
            if (delims.indexOf(ch) < 0) {
                if (delim)
                    start = i;
                if (i + 1 == length)
                    tokens.add(value.substring(start));
                delim = false;
                nulls = 0;
            } else {
                if (!(delim || i == start))
                    tokens.add(value.substring(start, i));
                if (!Character.isWhitespace(ch)) {
                    if (nulls++ > 0 || tokens.isEmpty())
                        tokens.add(null);
                    if (i + 1 == length)
                        tokens.add(null);
                }
                delim = true;
            }
        }
        return tokens.isEmpty() ? EMPTY_TOKEN_ARRAY : tokens.toArray(new String[tokens.size()]);
    }
    
    /**
     * Builds string from the specified array of tokens.
     * 
     * @param value Array of tokens.
     * @param context Validation context.
     * @return String from the specified array of tokens.
     */
    @Override
    protected <T> String doEncode(String[] value, ValidationContext<T> context) {
        char delim = delims.charAt(0);
        StringBuilder buf = new StringBuilder(value[0]);
        for (int i = 1; i < value.length; i++)
            buf.append(delim).append(value[i]);
        return buf.toString();
    }
    
}
