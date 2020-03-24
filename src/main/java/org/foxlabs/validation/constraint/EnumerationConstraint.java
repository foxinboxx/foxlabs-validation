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

import java.util.Arrays;
import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.LinkedHashSet;
import java.util.Collections;
import java.util.Locale;

import org.foxlabs.common.Checks;
import org.foxlabs.util.reflect.Types;

import org.foxlabs.validation.ValidationContext;
import org.foxlabs.validation.converter.ConverterFactory;

/**
 * This class provides base implementation of the <code>CheckConstraint</code>
 * that checks whether the specified value is one of the allowed constants.
 *
 * @author Fox Mulder
 * @param <V> The type of the enumeration constants
 */
public abstract class EnumerationConstraint<V> extends CheckConstraint<V> {

    /**
     * Returns unmodifiable set of allowed constants.
     *
     * @return Unmodifiable set of allowed constants.
     */
    public abstract Set<V> getConstants();

    /**
     * Returns set of constants in order according to the specified locale.
     * Invokes {@link #getConstants()} by default.
     *
     * @param locale Locale to be used for constants sorting.
     * @return Set of constants in order according to the specified locale.
     */
    public Set<V> getSortedConstants(Locale locale) {
        return getConstants();
    }

    /**
     * Appends <code>constants</code> argument that contains set of allowed
     * constants.
     *
     * @param context Validation context.
     * @param arguments Arguments to be substituted into the error message
     *        template.
     * @return <code>true</code>.
     */
    @Override
    public boolean appendMessageArguments(ValidationContext<?> context, Map<String, Object> arguments) {
        super.appendMessageArguments(context, arguments);
        arguments.put("constants", getConstants());
        return true;
    }

    /**
     * Checks whether the specified value is one of the allowed constants.
     *
     * @param value Value to be checked.
     * @param context Validation context.
     * @return <code>true</code> if the specified value is one of the allowed
     *         constants; <code>false</code> otherwise.
     */
    @Override
    protected <T> boolean check(V value, ValidationContext<T> context) {
        return value == null || getConstants().contains(value);
    }

    // Default

    /**
     * This class provides default <code>EnumerationConstraint</code>
     * implementation based on static set of constants.
     *
     * @author Fox Mulder
     * @param <V> The type of enumeration constants
     * @see Enumeration
     * @see ConstraintFactory#enumeration(Class)
     * @see ConstraintFactory#enumeration(Object...)
     * @see ConstraintFactory#enumeration(Collection)
     */
    public static class Default<V> extends EnumerationConstraint<V> {

        /**
         * Type of enumeration constants.
         */
        protected final Class<?> type;

        /**
         * Unmodifiable set of allowed constants.
         */
        protected final Set<V> constants;

        /**
         * Constructs a new <code>EnumerationConstraint.Default</code> with the
         * specified array of allowed constants.
         *
         * @param constants Array of allowed constants.
         * @throws IllegalArgumentException if the specified array of allowed
         *         constants is <code>null</code> or empty or contains
         *         <code>null</code> elements.
         */
        @SafeVarargs
        protected Default(V... constants) {
            this(Types.superTypeOf(Types.typesOf(
                Checks.checkThat(constants, constants != null && constants.length > 0, "constants"))),
                Arrays.asList(constants));
        }

        /**
         * Constructs a new <code>EnumerationConstraint.Default</code> with the
         * specified array of allowed constants and their type.
         *
         * @param type Type of enumeration constants.
         * @param constants Array of allowed constants.
         * @throws IllegalArgumentException if the specified type is
         *         <code>null</code> or the specified array of allowed
         *         constants is <code>null</code> or empty or contains
         *         <code>null</code> elements.
         */
        @SafeVarargs
        protected Default(Class<?> type, V... constants) {
            this(type, Arrays.asList(Checks.checkThat(constants, constants != null && constants.length > 0, "constants")));
        }

        /**
         * Constructs a new <code>EnumerationConstraint.Default</code> with the
         * specified collection of allowed constants.
         *
         * @param constants Collection of allowed constants.
         * @throws IllegalArgumentException if the specified collection of
         *         allowed constants is <code>null</code> or empty or contains
         *         <code>null</code> elements.
         */
        protected Default(Collection<V> constants) {
            this(Types.superTypeOf(
                Types.typesOf(Checks.checkThat(constants, constants != null && constants.size() > 0, "constants").toArray())),
                constants);
        }

        /**
         * Constructs a new <code>EnumerationConstraint.Default</code> with the
         * specified collection of allowed constants and their type.
         *
         * @param type Type of enumeration constants.
         * @param constants Collection of allowed constants.
         * @throws IllegalArgumentException if the specified type is
         *         <code>null</code> or the specified collection of allowed
         *         constants is <code>null</code> or empty or contains
         *         <code>null</code> elements.
         */
        protected Default(Class<?> type, Collection<V> constants) {
            this(type, constants instanceof Set
                ? (Set<V>) constants
                : new LinkedHashSet<V>(Checks.checkThat(constants, constants != null && constants.size() > 0, "constants")));
        }

        /**
         * Constructs a new <code>EnumerationConstraint.Default</code> with the
         * specified set of allowed constants and their type.
         *
         * @param type Type of enumeration constants.
         * @param constants Set of allowed constants.
         * @throws IllegalArgumentException if the specified type is
         *         <code>null</code> or the specified set of allowed
         *         constants is <code>null</code> or empty or contains
         *         <code>null</code> elements.
         */
        protected Default(Class<?> type, Set<V> constants) {
            this.type = Checks.checkNotNull(type, "type");
            this.constants = Collections.unmodifiableSet(Checks.checkAllNotNull(
                Checks.checkThat(constants, constants != null && constants.size() > 0, "constants cannot be null or empty"),
                "constants[%s]"));
        }

        /**
         * Constructs a new <code>EnumerationConstraint.Default</code> from the
         * specified annotation and type.
         *
         * @param type Type of enumeration constants.
         * @param annotation Constraint annotation.
         * @throws IllegalArgumentException if the specified annotation defines
         *         empty set of allowed constants.
         */
        protected Default(Class<V> type, Enumeration annotation) {
            this(type, Arrays.asList(ConverterFactory.decode(type, annotation.value())));
        }

        /**
         * Returns type of enumeration constants.
         *
         * @return Type of enumeration constants.
         */
        @Override
        public final Class<?> getType() {
            return type;
        }

        /**
         * Returns unmodifiable set of allowed constants.
         *
         * @return Unmodifiable set of allowed constants.
         */
        @Override
        public final Set<V> getConstants() {
            return constants;
        }

    }

}
