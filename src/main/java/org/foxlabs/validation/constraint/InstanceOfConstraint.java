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

import org.foxlabs.validation.ValidationContext;

import org.foxlabs.common.Checks;

/**
 * This class provides <code>CheckConstraint</code> implementation that checks
 * whether a value is instance of one of allowed types.
 *
 * @author Fox Mulder
 * @see InstanceOf
 * @see ConstraintFactory#instanceOf(Class...)
 */
public final class InstanceOfConstraint extends CheckConstraint<Object> {

    /**
     * Array of allowed types.
     */
    private final Class<?>[] types;

    /**
     * Constructs a new <code>InstanceOfConstraint</code> with the specified
     * array of allowed type.
     *
     * @param types Array of allowed types.
     * @throws IllegalArgumentException if the specified array of types is
     *         <code>null</code> or empty or contains <code>null</code>
     *         elements.
     */
    InstanceOfConstraint(Class<?>[] types) {
        this.types = Checks.checkAllNotNull(
            Checks.checkThat(types, types != null && types.length > 0, "types cannot be null or empty"),
            "types[%s]");
    }

    /**
     * Constructs a new <code>InstanceOfConstraint</code> from the specified
     * annotation.
     *
     * @param annotation Constraint annotation.
     */
    InstanceOfConstraint(InstanceOf annotation) {
        this(annotation.value());
    }

    /**
     * Returns <code>java.lang.Object</code> type.
     *
     * @return <code>java.lang.Object</code> type.
     */
    @Override
    public Class<?> getType() {
        return Object.class;
    }

    /**
     * Appends <code>types</code> argument that contains array of allowed types.
     *
     * @param context Validation context.
     * @param arguments Arguments to be substituted into the error message
     *        template.
     * @return <code>true</code>.
     */
    @Override
    public boolean appendMessageArguments(ValidationContext<?> context, Map<String, Object> arguments) {
        super.appendMessageArguments(context, arguments);
        arguments.put("types", types);
        return true;
    }

    /**
     * Checks whether the specified value is instance of one of allowed types.
     *
     * @param value Value to be checked.
     * @param context Validation context.
     * @return <code>true</code> if the specified value is instance of one of
     *         allowed types; <code>false</code> otherwise.
     */
    @Override
    protected <T> boolean check(Object value, ValidationContext<T> context) {
        if (value == null)
            return true;
        Class<?> type = value.getClass();
        for (int i = 0; i < types.length; i++)
            if (types[i].isAssignableFrom(type))
                return true;
        return false;
    }

}
