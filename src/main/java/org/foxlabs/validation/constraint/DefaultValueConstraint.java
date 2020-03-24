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
import org.foxlabs.validation.converter.ConverterFactory;

import org.foxlabs.common.Checks;

/**
 * This class provides <code>CorrectConstraint</code> implementation that
 * replaces a <code>null</code> value with default value.
 *
 * @author Fox Mulder
 * @param <V> The type of value to be validated
 * @see DefaultValue
 * @see ConstraintFactory#defaultValue(Object)
 */
public final class DefaultValueConstraint<V> extends CorrectConstraint<V> {

    /**
     * Default value.
     */
    private final V defaultValue;

    /**
     * Constructs a new <code>DefaultValueConstraint</code> with the specified
     * default value.
     *
     * @param defaultValue Default value.
     * @throws IllegalArgumentException if the specified default value is
     *         <code>null</code>.
     */
    DefaultValueConstraint(V defaultValue) {
        this.defaultValue = Checks.checkNotNull(defaultValue, "defaultValue");
    }

    /**
     * Constructs a new <code>DefaultValueConstraint</code> from the specified
     * value type and annotation.
     *
     * @param annotation Constraint annotation.
     * @param type Value type.
     * @throws MalformedValueException if string representation of default
     *         value from annotation can't be parsed.
     */
    DefaultValueConstraint(Class<V> type, DefaultValue annotation) {
        this(ConverterFactory.decode(type, annotation.value()));
    }

    /**
     * Returns the type of value to be validated.
     *
     * @return The type of value to be validated.
     */
    @Override
    public Class<?> getType() {
        return defaultValue.getClass();
    }

    /**
     * Returns default value.
     *
     * @return Default value.
     */
    public V getDefaultValue() {
        return defaultValue;
    }

    /**
     * Returns default value if the specified value is <code>null</code>;
     * returns value as is otherwise.
     *
     * @param value Value to be checked for <code>null</code>.
     * @param context Validation context.
     * @return Default value if the specified value is <code>null</code>;
     *         value as is otherwise.
     */
    @Override
    public <T> V validate(V value, ValidationContext<T> context) {
        return value == null ? defaultValue : value;
    }

}
