/*
 * Copyright (C) 2020 FoxLabs
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

/**
 * This class provides {@code Constraint} implementation that just returns
 * value unchanged.
 *
 * @author Fox Mulder
 * @see ConstraintFactory#identity()
 */
public class IdentityConstraint implements Constraint<Object> {

    /**
     * The {@code IdentityConstraint} single instance.
     */
    public static final IdentityConstraint DEFAULT = new IdentityConstraint();

    /**
     * Constructs default {@code IdentityConstraint}.
     */
    private IdentityConstraint() {}

    /**
     * Returns {@code java.lang.Object} type.
     *
     * @return {@code java.lang.Object} type.
     */
    @Override
    public Class<?> getType() {
        return Object.class;
    }

    /**
     * Returns {@code null}.
     *
     * @param context Validation context.
     * @return {@code null}.
     */
    @Override
    public String getMessageTemplate(ValidationContext<?> context) {
        return null;
    }

    /**
     * Returns {@code false}.
     *
     * @param context Validation context.
     * @param arguments Arguments to be substituted into the error message
     *        template.
     * @return {@code false}.
     */
    @Override
    public boolean appendMessageArguments(ValidationContext<?> context, Map<String, Object> arguments) {
        return false;
    }

    /**
     * Returns the specified value.
     *
     * @param value Value to be checked.
     * @param context Validation context.
     * @return The specified value unchanged.
     */
    @Override
    public <T> Object validate(Object value, ValidationContext<T> context) {
        return value;
    }

}
