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

import java.util.regex.Pattern;

import org.foxlabs.validation.ValidationContext;

import org.foxlabs.util.Assert;

/**
 * This class provides <code>CorrectConstraint</code> implementation that
 * Replaces each substring of a string that matches the given regular
 * expression with the given replacement.
 * 
 * @author Fox Mulder
 * @see Replace
 * @see ConstraintFactory#replace(String, String)
 * @see ConstraintFactory#replace(String, String, int)
 */
public class ReplaceConstraint extends CorrectConstraint<String> {
    
    /**
     * Compiled representation of a regular expression.
     */
    protected final Pattern pattern;
    
    /**
     * String to be substituted for each match.
     */
    protected final String replacement;
    
    /**
     * Constructs a new <code>ReplaceConstraint</code> with the specified
     * regular expression, substitution string and match flags.
     * 
     * @param regex Regular expression pattern.
     * @param replacement String to be substituted for each match.
     * @param flags Match flags.
     * @throws IllegalArgumentException if the specified regular expression is
     *         <code>null</code> or invalid or the specified replacement string
     *         is <code>null</code>.
     */
    protected ReplaceConstraint(String regex, String replacement, int flags) {
        this.pattern = Pattern.compile(Assert.notNull(regex, "regex"), flags);
        this.replacement = Assert.notNull(replacement, "replacement");
    }
    
    /**
     * Constructs a new <code>ReplaceConstraint</code> from the specified
     * annotation.
     * 
     * @param annotation Constraint annotation.
     * @throws IllegalArgumentException if the specified annotation defines
     *         invalid regular expression.
     */
    ReplaceConstraint(Replace annotation) {
        this(annotation.pattern(), annotation.replacement(), annotation.flags());
    }
    
    /**
     * Returns <code>java.lang.String</code> type.
     * 
     * @return <code>java.lang.String</code> type.
     */
    @Override
    public final Class<?> getType() {
        return String.class;
    }
    
    /**
     * Returns compiled representation of a regular expression.
     * 
     * @return Compiled representation of a regular expression.
     */
    public final Pattern getPattern() {
        return pattern;
    }
    
    /**
     * Returns string to be substituted for each match.
     * 
     * @return String to be substituted for each match.
     */
    public final String getReplacement() {
        return replacement;
    }
    
    /**
     * Replaces each substring of the specified string that matches the given
     * regular expression with the given replacement.
     * 
     * @param value Source string.
     * @param context Validation context.
     * @return The resulting string.
     */
    @Override
    public <T> String validate(String value, ValidationContext<T> context) {
        return value == null ? null : pattern.matcher(value).replaceAll(replacement);
    }
    
}
