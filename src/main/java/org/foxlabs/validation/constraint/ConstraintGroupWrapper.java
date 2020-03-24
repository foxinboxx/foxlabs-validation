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

import java.util.Set;
import java.util.Collections;

import org.foxlabs.common.Checks;
import org.foxlabs.common.Sets;

import org.foxlabs.validation.ValidationContext;

/**
 * This class provides ability to override groups of another constraint. Any
 * constraint annotations can define <code>groups</code> property of the
 * <code>java.lang.String[]</code> type.
 *
 * @author Fox Mulder
 * @param <V> The type of value to be validated
 * @see ConstraintFactory#wrapGroups(Constraint)
 * @see ConstraintFactory#wrapGroups(Constraint, String...)
 */
public final class ConstraintGroupWrapper<V> extends ConstraintWrapper<V> {

    /**
     * Set of groups the constraint is applied on.
     */
    private final Set<String> groups;

    /**
     * Constructs a new <code>ConstraintGroupWrapper</code> with the specified
     * constraint and array of groups.
     *
     * @param constraint Constraint to be wrapped.
     * @param groups Array of groups the constraint is applied on.
     * @throws IllegalArgumentException if the specified constraint is
     *         <code>null</code> or the specified array of groups is
     *         <code>null</code> or empty or contains <code>null</code>
     *         elements.
     */
    ConstraintGroupWrapper(Constraint<V> constraint, String[] groups) {
        this(constraint, Sets.toImmutableLinkedHashSet(Checks.checkAllNotNull(
            Checks.checkThat(groups, groups != null && groups.length > 0, "groups cannot be null or empty"),
            "groups[%s]")));
    }

    /**
     * Constructs a new <code>ConstraintGroupWrapper</code> with the specified
     * constraint and set of groups.
     *
     * @param constraint Constraint to be wrapped.
     * @param groups Set of groups the constraint is applied on.
     * @throws IllegalArgumentException if the specified constraint is
     *         <code>null</code>.
     */
    ConstraintGroupWrapper(Constraint<V> constraint, Set<String> groups) {
        super(constraint);
        this.groups = Collections.unmodifiableSet(groups);
    }

    /**
     * Returns unmodifiable set of groups the constraint is applied on.
     *
     * @return Unmodifiable set of groups the constraint is applied on.
     */
    public Set<String> getGroups() {
        return groups;
    }

    /**
     * Returns localized error message template of the wrapped constraint or
     * <code>null</code> if no group of this wrapper matches groups provided by
     * validation context.
     *
     * @param context Validation context.
     * @return Localized error message template of the wrapped constraint or
     *         <code>null</code> if no group of this wrapper matches groups
     *         provided by validation context.
     */
    @Override
    public String getMessageTemplate(ValidationContext<?> context) {
        if (acceptConstraint(context))
            return constraint.getMessageTemplate(context);
        return null;
    }

    /**
     * Validates the specified value using context if needed and returns
     * possibly modified value.
     *
     * <p>This method performs validation only if one or more groups provided
     * by validation context match at least one group of this wrapper;
     * otherwise it returns value as is.</p>
     *
     * @param value Value to be validated.
     * @param context Validation context.
     * @return Possibly modified value.
     * @throws ConstraintViolationException if validation fails.
     */
    @Override
    public <T> V validate(V value, ValidationContext<T> context) {
        if (acceptConstraint(context)) {
            try {
                return constraint.validate(value, context);
            } catch (ConstraintViolationException e) {
                throw new ConstraintViolationException(this, context, value, e);
            }
        }
        return value;
    }

    /**
     * Determines if the specified constraint should be accepted. This method
     * checks whether the specified constraint belongs to the current
     * validating groups.
     *
     * @return <code>true</code> if the specified constraint should be accepted;
     *         <code>false</code> otherwise.
     */
    private boolean acceptConstraint(ValidationContext<?> context) {
        String[] validatingGroups = context.getValidatingGroups();
        if (validatingGroups.length == 0)
            return true;
        for (String group : validatingGroups)
            if (groups.contains(group))
                return true;
        return false;
    }

}
