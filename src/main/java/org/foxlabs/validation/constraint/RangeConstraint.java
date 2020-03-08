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
import java.util.Comparator;

import org.foxlabs.validation.ValidationContext;

import static org.foxlabs.validation.converter.ConverterFactory.*;

import org.foxlabs.common.Predicates;

/**
 * This class provides <code>CheckConstraint</code> implementation that checks
 * whether a value is within allowed minimum and maximum range.
 *
 * @author Fox Mulder
 * @param <V> The type of value to be validated
 * @see Range
 * @see Min
 * @see Max
 * @see ConstraintFactory#range(Comparable, Comparable)
 * @see ConstraintFactory#range(Object, Object, Comparator)
 * @see ConstraintFactory#min(Comparable)
 * @see ConstraintFactory#min(Object, Comparator)
 * @see ConstraintFactory#max(Comparable)
 * @see ConstraintFactory#max(Object, Comparator)
 */
public final class RangeConstraint<V> extends CheckConstraint<V> {

    /**
     * Minimum value.
     */
    private final V min;

    /**
     * Maximum value.
     */
    private final V max;

    /**
     * Comparator to be used for range checking.
     */
    private final Comparator<V> comparator;

    /**
     * Constructs a new <code>RangeConstraint</code> with the specified minimum
     * and maximum range and comparator.
     *
     * @param min Minimum value.
     * @param max Maximum value.
     * @param comparator Comparator to be used for range checking.
     * @throws IllegalArgumentException if the specified comparator is
     *         <code>null</code> or there is no range specified.
     */
    RangeConstraint(V min, V max, Comparator<V> comparator) {
        this.comparator = Predicates.requireNonNull(comparator, "comparator");

        if (min == null) {
            if (max == null)
                throw new IllegalArgumentException("range");
            this.min = null;
            this.max = max;
        } else if (max == null) {
            this.min = min;
            this.max = null;
        } else {
            if (comparator.compare(min, max) < 0) {
                this.min = min;
                this.max = max;
            } else {
                this.min = max;
                this.max = min;
            }
        }
    }

    /**
     * Constructs a new <code>RangeConstraint</code> with the specified value
     * type, minimum and maximum range and comparator type.
     *
     * @param type The type of value to be validated.
     * @param min String representation of the minimum value.
     * @param max String representation of the maximum value.
     * @param comparatorType Type of the comparator to be used for range
     *        checking.
     * @throws IllegalArgumentException if there is no range specified.
     * @throws UnsupportedOperationException if the specified comparator is not
     *         applicable to the specified value type.
     * @throws MalformedValueException if string representation of minimum or
     *         maximum value can't be parsed.
     */
    RangeConstraint(Class<V> type, String min, String max, Class<? extends Comparator<?>> comparatorType) {
        this(decode(type, min), decode(type, max), DefaultComparator.getInstance(type, comparatorType));
    }

    /**
     * Constructs a new <code>RangeConstraint</code> from the specified
     * annotation and value type.
     *
     * @param annotation Constraint annotation.
     * @param type The type of value to be validated.
     * @throws IllegalArgumentException if the specified annotation doesn't
     *         define minimum value.
     * @throws UnsupportedOperationException if the specified annotation defines
     *         comparator that is not applicable to the specified value type.
     * @throws MalformedValueException if the specified annotation defines
     *         string representation of minumum value that can't be parsed.
     */
    RangeConstraint(Min annotation, Class<V> type) {
        this(type, annotation.value(), null, annotation.comparator());
    }

    /**
     * Constructs a new <code>RangeConstraint</code> from the specified
     * annotation and value type.
     *
     * @param annotation Constraint annotation.
     * @param type The type of value to be validated.
     * @throws IllegalArgumentException if the specified annotation doesn't
     *         define maximum value.
     * @throws UnsupportedOperationException if the specified annotation defines
     *         comparator that is not applicable to the specified value type.
     * @throws MalformedValueException if the specified annotation defines
     *         string representation of maxumum value that can't be parsed.
     */
    RangeConstraint(Max annotation, Class<V> type) {
        this(type, null, annotation.value(), annotation.comparator());
    }

    /**
     * Constructs a new <code>RangeConstraint</code> from the specified
     * annotation and value type.
     *
     * @param annotation Constraint annotation.
     * @param type The type of value to be validated.
     * @throws IllegalArgumentException if the specified annotation doesn't
     *         define minimum and maximum values.
     * @throws UnsupportedOperationException if the specified annotation defines
     *         comparator that is not applicable to the specified value type.
     * @throws MalformedValueException if the specified annotation defines
     *         string representation of minumum or maximum value that can't be
     *         parsed.
     */
    RangeConstraint(Range annotation, Class<V> type) {
        this(type, annotation.min(), annotation.max(), annotation.comparator());
    }

    /**
     * Returns the type of value to be validated.
     *
     * @return The type of value to be validated.
     */
    @Override
    public Class<?> getType() {
        return min == null ? max.getClass() : min.getClass();
    }

    /**
     * Returns minimum value or <code>null</code> if there is no minimum.
     *
     * @return Minimum value or <code>null</code> if there is no minimum.
     */
    public V getMin() {
        return min;
    }

    /**
     * Returns maximum value or <code>null</code> if there is no maximum.
     *
     * @return Maximum value or <code>null</code> if there is no maximum.
     */
    public V getMax() {
        return max;
    }

    /**
     * Returns comparator to be used for range checking.
     *
     * @return Comparator to be used for range checking.
     */
    public Comparator<V> getComparator() {
        return comparator;
    }

    /**
     * Returns localized error message template.
     *
     * @param context Validation context.
     * @return Localized error message template.
     */
    @Override
    public String getMessageTemplate(ValidationContext<?> context) {
        return context.resolveMessage(getClass().getName() +
                (min == null ? ".max" : max == null ? ".min" : ""));
    }

    /**
     * Appends <code>min</code> and <code>max</code> arguments that contain
     * minimum and maximum value respectively.
     *
     * @param context Validation context.
     * @param arguments Arguments to be substituted into the error message
     *        template.
     * @return <code>true</code>.
     */
    @Override
    public boolean appendMessageArguments(ValidationContext<?> context, Map<String, Object> arguments) {
        super.appendMessageArguments(context, arguments);
        arguments.put("min", min);
        arguments.put("max", max);
        return true;
    }

    /**
     * Checks whether the specified value is within allowed minimum and maximum
     * range.
     *
     * @param value Value to be checked.
     * @param context Validation context.
     * @return <code>true</code> if the specified value is within allowed
     *         minimum and maximum range; <code>false</code> otherwise.
     */
    @Override
    protected <T> boolean check(V value, ValidationContext<T> context) {
        if (value == null)
            return true;
        return (min == null || comparator.compare(min, value) <= 0)
            && (max == null || comparator.compare(max, value) >= 0);
    }

}
