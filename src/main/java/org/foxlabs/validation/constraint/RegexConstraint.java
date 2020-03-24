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

import java.util.Map;
import java.util.regex.Pattern;

import org.foxlabs.validation.ValidationContext;

import org.foxlabs.common.Checks;

/**
 * This class provides <code>CheckConstraint</code> implementation that checks
 * whether a string matches the regular expression.
 *
 * @author Fox Mulder
 * @see Regex
 * @see ConstraintFactory#regex(String)
 * @see ConstraintFactory#regex(String, int)
 */
public class RegexConstraint extends CheckConstraint<String> {

    /**
     * Compiled representation of a regular expression.
     */
    protected final Pattern pattern;

    /**
     * Constructs a new <code>RegexConstraint</code> with the specified regular
     * expression and flags.
     *
     * @param regex Regular expression pattern.
     * @param flags Match flags.
     * @throws IllegalArgumentException if the specified regular expression is
     *         <code>null</code> or invalid.
     */
    protected RegexConstraint(String regex, int flags) {
        this.pattern = Pattern.compile(Checks.checkNotNull(regex, "regex"), flags);
    }

    /**
     * Constructs a new <code>RegexConstraint</code> from the specified
     * annotation.
     *
     * @param annotation Constraint annotation.
     * @throws IllegalArgumentException if the specified annotation defines
     *         invalid regular expression.
     */
    RegexConstraint(Regex annotation) {
        this(annotation.pattern(), annotation.flags());
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
     * Appends <code>pattern</code> argument that contains regular expression
     * pattern.
     *
     * @param context Validation context.
     * @param arguments Arguments to be substituted into the error message
     *        template.
     * @return <code>true</code>.
     */
    @Override
    public boolean appendMessageArguments(ValidationContext<?> context, Map<String, Object> arguments) {
        super.appendMessageArguments(context, arguments);
        arguments.put("pattern", pattern.toString());
        return true;
    }

    /**
     * Checks whether the specified string matches the regular expression.
     *
     * @param value String to be checked.
     * @param context Validation context.
     * @return <code>true</code> if the specified string matches the regular
     *         expression; <code>false</code> otherwise.
     */
    @Override
    protected <T> boolean check(String value, ValidationContext<T> context) {
        return value == null || pattern.matcher(value).matches();
    }

}
