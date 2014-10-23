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

package org.foxlabs.validation.constraint;

import org.foxlabs.validation.ValidationContext;

/**
 * This class provides <code>CorrectConstraint</code> implementation that
 * replaces all whitespaces with single character in a string.
 * 
 * @author Fox Mulder
 * @see Coalesce
 * @see ConstraintFactory#coalesce()
 * @see ConstraintFactory#coalesce(char, boolean)
 */
public final class CoalesceConstraint extends CorrectConstraint<String> {
    
    /**
     * <code>CoalesceConstraint</code> default instance initialized with
     * <code>\u0020</code> whitespaces replacement character.
     */
    public static final CoalesceConstraint DEFAULT = new CoalesceConstraint('\u0020', false);
    
    /**
     * Whitespaces replacement character.
     */
    private final char replacement;
    
    /**
     * Determines if new line characters should be preserved.
     */
    private final boolean multiline;
    
    /**
     * Constructs a new <code>CoalesceConstraint</code> with the specified
     * whitespaces replacement character.
     * 
     * @param replacement Whitespaces replacement character.
     * @param multiline Determines if new line characters should be preserved.
     */
    CoalesceConstraint(char replacement, boolean multiline) {
        this.replacement = replacement;
        this.multiline = multiline;
    }
    
    /**
     * Constructs a new <code>CoalesceConstraint</code> from the specified
     * annotation.
     * 
     * @param annotation Constraint annotation.
     */
    CoalesceConstraint(Coalesce annotation) {
        this(annotation.replacement(), annotation.multiline());
    }
    
    /**
     * Returns whitespaces replacement character.
     * 
     * @return Whitespaces replacement character.
     */
    public char getReplacement() {
        return replacement;
    }
    
    /**
     * Flag determines if new line characters should be preserved.
     * 
     * @return <code>true</code> if new line characters should be preserved;
     *         <code>false</code> otherwise.
     */
    public boolean isMultiline() {
        return multiline;
    }
    
    /**
     * Returns <code>java.lang.String</code> type.
     * 
     * @return <code>java.lang.String</code> type.
     */
    @Override
    public Class<?> getType() {
        return String.class;
    }
    
    /**
     * Replaces all whitespaces with replacement character in the specified
     * string. Note that leading and trailing whitespaces will be removed.
     * 
     * @param value String to be coalesced.
     * @param context Validation context.
     * @return Coalesced string.
     */
    @Override
    public <T> String validate(String value, ValidationContext<T> context) {
        int length = value == null ? 0 : value.length();
        if (length == 0)
            return value;
        
        StringBuilder lines = new StringBuilder(4);
        StringBuilder buf = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            char ch = value.charAt(i);
            if (Character.isWhitespace(ch)) {
                lines.delete(0, lines.length());
                for (; i < length && Character.isWhitespace(ch = value.charAt(i)); i++)
                    if (ch == '\n' || ch == '\r')
                        lines.append(ch);
                if (i < length) {
                    if (buf.length() > 0) {
                        if (multiline && lines.length() > 0) {
                            buf.append(lines);
                        } else {
                            buf.append(replacement);
                        }
                    }
                    buf.append(ch);
                }
            } else {
                buf.append(ch);
            }
        }
        
        return buf.toString();
    }
    
}
