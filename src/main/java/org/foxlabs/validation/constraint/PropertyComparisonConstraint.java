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

import java.util.Comparator;
import java.util.Map;

import org.foxlabs.validation.ValidationContext;
import org.foxlabs.validation.Validator;

import org.foxlabs.common.Checks;
import org.foxlabs.common.Strings;

/**
 * This class provides base implementation of the <code>CheckConstraint</code>
 * that applies binary comparison operator to a value and other property.
 *
 * @author Fox Mulder
 * @param <V> The type of operands
 */
public abstract class PropertyComparisonConstraint<V> extends CheckConstraint<V> {

    /**
     * Type of the operands.
     */
    protected final Class<?> type;

    /**
     * Property name of the second operand.
     */
    protected final String propertyName;

    /**
     * Comparator to be used for operands comparison.
     */
    protected final Comparator<V> comparator;

    /**
     * Constructs a new <code>PropertyComparisonConstraint</code> with the
     * specified type of the operands, property name and comparator.
     *
     * @param type Type of the operands.
     * @param propertyName Property name of the second operand.
     * @param comparator Comparator to be used for operands comparison.
     * @throws NullPointerException if the specified type or propertyName or
     *         comparator is <code>null</code>.
     */
    protected PropertyComparisonConstraint(Class<?> type, String propertyName, Comparator<V> comparator) {
        this.type = Checks.checkNotNull(type, "type");
        this.propertyName = Checks.checkThat(propertyName, Strings.isNonEmpty(propertyName), "propertyName");
        this.comparator = Checks.checkNotNull(comparator, "comparator");
    }

    /**
     * Returns property name of the second operand.
     *
     * @return Property name of the second operand.
     */
    public final String getPropertyName() {
        return propertyName;
    }

    /**
     * Returns comparator to be used for operands comparison.
     *
     * @return Comparator to be used for operands comparison.
     */
    public final Comparator<V> getComparator() {
        return comparator;
    }

    /**
     * Returns type of the operands.
     *
     * @return Type of the operands.
     */
    @Override
    public final Class<?> getType() {
        return type;
    }

    /**
     * Returns localized error message template.
     *
     * @param context Validation context.
     * @return Localized error message template.
     */
    @Override
    public String getMessageTemplate(ValidationContext<?> context) {
        return context.resolveMessage(PropertyComparisonConstraint.class.getName() +
                "." + getClass().getSimpleName());
    }

    /**
     * Appends <code>property</code> argument that contains property name of
     * the second operand.
     *
     * @param context Validation context.
     * @param arguments Arguments to be substituted into the error message
     *        template.
     * @return <code>true</code>.
     */
    @Override
    public boolean appendMessageArguments(ValidationContext<?> context, Map<String, Object> arguments) {
        super.appendMessageArguments(context, arguments);
        arguments.put("property", propertyName);
        return true;
    }

    /**
     * Applies binary comparison operator to the specified value and other
     * property.
     *
     * @param value1 First operand.
     * @param context Validation context.
     * @return <code>true</code> if comparison was performed successfully;
     *         <code>false</code> otherwise.
     */
    @Override
    protected <T> boolean check(V value1, ValidationContext<T> context) {
        Validator<T> validator = context.getValidator();
        V value2 = validator.getValue(context.getCurrentEntity(), propertyName);
        return compare(value1, value2);
    }

    /**
     * Applies binary comparison operator between two values.
     *
     * @param value1 First operand.
     * @param value2 Second operand.
     * @return <code>true</code> if comparison was performed successfully;
     *         <code>false</code> otherwise.
     */
    protected abstract boolean compare(V value1, V value2);

    // EqualTo

    /**
     * This class provides <code>PropertyComparisonConstraint</code>
     * implementation that applies <code>==</code> operator to a value and
     * other property.
     *
     * @author Fox Mulder
     * @param <V> The type of operands
     * @see EqualTo
     * @see ConstraintFactory#eq(Class, String)
     * @see ConstraintFactory#eq(Class, String, Comparator)
     */
    public static final class EqualToOp<V> extends PropertyComparisonConstraint<V> {

        /**
         * Constructs a new <code>PropertyComparisonConstraint.EqualToOp</code>
         * with the specified type of the operands, property name and
         * comparator.
         *
         * @param type Type of the operands.
         * @param propertyName Property name of the second operand.
         * @param comparator Comparator to be used for operands comparison.
         * @throws NullPointerException if the specified type or propertyName
         *         or comparator is <code>null</code>.
         */
        EqualToOp(Class<?> type, String propertyName, Comparator<V> comparator) {
            super(type, propertyName, comparator);
        }

        /**
         * Constructs a new <code>PropertyComparisonConstraint.EqualTo</code>
         * from the specified annotation and type of the operands.
         *
         * @param annotation Constraint annotation.
         * @param type Type of the operands.
         * @throws UnsupportedOperationException if the specified annotation
         *         defines comparator that is not applicable to the specified
         *         operands type.
         */
        @SuppressWarnings("unchecked")
        EqualToOp(EqualTo annotation, Class<V> type) {
            super(type, annotation.value(), annotation.comparator() == DefaultComparator.class
                ? (Comparator<V>) DefaultComparator.INSTANCE
                : DefaultComparator.getInstance(type, annotation.comparator()));
        }

        /**
         * Compares if the first specified operand equals to the second.
         *
         * @param value1 First operand.
         * @param value2 Second operand.
         * @return <code>true</code> if the first specified operand equals to
         *         the second; <code>false</code> otherwise.
         */
        @Override
        protected boolean compare(V value1, V value2) {
            if (comparator instanceof DefaultComparator)
                return value1 == null ? value2 == null : value1.equals(value2);
            return value1 == null || value2 == null || comparator.compare(value1, value2) == 0;
        }

    }

    // NotEqualTo

    /**
     * This class provides <code>PropertyComparisonConstraint</code>
     * implementation that applies <code>!=</code> operator to a value and
     * other property.
     *
     * @author Fox Mulder
     * @param <V> The type of operands
     * @see NotEqualTo
     * @see ConstraintFactory#ne(Class, String)
     * @see ConstraintFactory#ne(Class, String, Comparator)
     */
    public static final class NotEqualToOp<V> extends PropertyComparisonConstraint<V> {

        /**
         * Constructs a new <code>PropertyComparisonConstraint.NotEqualToOp</code>
         * with the specified type of the operands, property name and
         * comparator.
         *
         * @param type Type of the operands.
         * @param propertyName Property name of the second operand.
         * @param comparator Comparator to be used for operands comparison.
         * @throws NullPointerException if the specified type or propertyName
         *         or comparator is <code>null</code>.
         */
        NotEqualToOp(Class<?> type, String propertyName, Comparator<V> comparator) {
            super(type, propertyName, comparator);
        }

        /**
         * Constructs a new <code>PropertyComparisonConstraint.NotEqualToOp</code>
         * from the specified annotation and type of the operands.
         *
         * @param annotation Constraint annotation.
         * @param type Type of the operands.
         * @throws UnsupportedOperationException if the specified annotation
         *         defines comparator that is not applicable to the specified
         *         operands type.
         */
        @SuppressWarnings("unchecked")
        NotEqualToOp(NotEqualTo annotation, Class<V> type) {
            super(type, annotation.value(), annotation.comparator() == DefaultComparator.class
                ? (Comparator<V>) DefaultComparator.INSTANCE
                : DefaultComparator.getInstance(type, annotation.comparator()));
        }

        /**
         * Compares if the first specified operand not equals to the second.
         *
         * @param value1 First operand.
         * @param value2 Second operand.
         * @return <code>true</code> if the first specified operand not equals
         *         to the second; <code>false</code> otherwise.
         */
        @Override
        protected boolean compare(V value1, V value2) {
            if (comparator instanceof DefaultComparator)
                return !(value1 == null ? value2 == null : value1.equals(value2));
            return value1 == null || value2 == null || comparator.compare(value1, value2) != 0;
        }

    }

    // LessThan

    /**
     * This class provides <code>PropertyComparisonConstraint</code>
     * implementation that applies <code>&lt;</code> operator to a value and
     * other property.
     *
     * @author Fox Mulder
     * @param <V> The type of operands
     * @see LessThan
     * @see ConstraintFactory#lt(Class, String)
     * @see ConstraintFactory#lt(Class, String, Comparator)
     */
    public static final class LessThanOp<V> extends PropertyComparisonConstraint<V> {

        /**
         * Constructs a new <code>PropertyComparisonConstraint.LessThanOp</code>
         * with the specified type of the operands, property name and
         * comparator.
         *
         * @param type Type of the operands.
         * @param propertyName Property name of the second operand.
         * @param comparator Comparator to be used for operands comparison.
         * @throws NullPointerException if the specified type or propertyName
         *         or comparator is <code>null</code>.
         */
        LessThanOp(Class<?> type, String propertyName, Comparator<V> comparator) {
            super(type, propertyName, comparator);
        }

        /**
         * Constructs a new <code>PropertyComparisonConstraint.LessThanOp</code>
         * from the specified annotation and type of the operands.
         *
         * @param annotation Constraint annotation.
         * @param type Type of the operands.
         * @throws UnsupportedOperationException if the specified annotation
         *         defines comparator that is not applicable to the specified
         *         operands type.
         */
        LessThanOp(LessThan annotation, Class<V> type) {
            super(type, annotation.value(), DefaultComparator.getInstance(type, annotation.comparator()));
        }

        /**
         * Compares if the first specified operand is less than the second.
         *
         * @param value1 First operand.
         * @param value2 Second operand.
         * @return <code>true</code> if the first specified operand is less
         *         than the second; <code>false</code> otherwise.
         */
        @Override
        protected boolean compare(V value1, V value2) {
            return value1 == null || value2 == null || comparator.compare(value1, value2) < 0;
        }

    }

    // LessThanEqual

    /**
     * This class provides <code>PropertyComparisonConstraint</code>
     * implementation that applies <code>&lt;=</code> operator to a value and
     * other property.
     *
     * @author Fox Mulder
     * @param <V> The type of operands
     * @see LessThanEqual
     * @see ConstraintFactory#lte(Class, String)
     * @see ConstraintFactory#lte(Class, String, Comparator)
     */
    public static final class LessThanEqualOp<V> extends PropertyComparisonConstraint<V> {

        /**
         * Constructs a new <code>PropertyComparisonConstraint.LessThanEqualOp</code>
         * with the specified type of the operands, property name and
         * comparator.
         *
         * @param type Type of the operands.
         * @param propertyName Property name of the second operand.
         * @param comparator Comparator to be used for operands comparison.
         * @throws NullPointerException if the specified type or propertyName
         *         or comparator is <code>null</code>.
         */
        LessThanEqualOp(Class<?> type, String propertyName, Comparator<V> comparator) {
            super(type, propertyName, comparator);
        }

        /**
         * Constructs a new <code>PropertyComparisonConstraint.LessThanEqualOp</code>
         * from the specified annotation and type of the operands.
         *
         * @param annotation Constraint annotation.
         * @param type Type of the operands.
         * @throws UnsupportedOperationException if the specified annotation
         *         defines comparator that is not applicable to the specified
         *         operands type.
         */
        LessThanEqualOp(LessThanEqual annotation, Class<V> type) {
            super(type, annotation.value(), DefaultComparator.getInstance(type, annotation.comparator()));
        }

        /**
         * Compares if the first specified operand is less than or equal to the
         * second.
         *
         * @param value1 First operand.
         * @param value2 Second operand.
         * @return <code>true</code> if the first specified operand is less
         *         than or equal to the second; <code>false</code> otherwise.
         */
        @Override
        protected boolean compare(V value1, V value2) {
            return value1 == null || value2 == null || comparator.compare(value1, value2) <= 0;
        }

    }

    // GreaterThan

    /**
     * This class provides <code>PropertyComparisonConstraint</code>
     * implementation that applies <code>&gt;</code> operator to a value and
     * other property.
     *
     * @author Fox Mulder
     * @param <V> The type of operands
     * @see GreaterThan
     * @see ConstraintFactory#gt(Class, String)
     * @see ConstraintFactory#gt(Class, String, Comparator)
     */
    public static final class GreaterThanOp<V> extends PropertyComparisonConstraint<V> {

        /**
         * Constructs a new <code>PropertyComparisonConstraint.GreaterThanOp</code>
         * with the specified type of the operands, property name and
         * comparator.
         *
         * @param type Type of the operands.
         * @param propertyName Property name of the second operand.
         * @param comparator Comparator to be used for operands comparison.
         * @throws NullPointerException if the specified type or propertyName
         *         or comparator is <code>null</code>.
         */
        GreaterThanOp(Class<?> type, String propertyName, Comparator<V> comparator) {
            super(type, propertyName, comparator);
        }

        /**
         * Constructs a new <code>PropertyComparisonConstraint.GreaterThanOp</code>
         * from the specified annotation and type of the operands.
         *
         * @param annotation Constraint annotation.
         * @param type Type of the operands.
         * @throws UnsupportedOperationException if the specified annotation
         *         defines comparator that is not applicable to the specified
         *         operands type.
         */
        GreaterThanOp(GreaterThan annotation, Class<V> type) {
            super(type, annotation.value(), DefaultComparator.getInstance(type, annotation.comparator()));
        }

        /**
         * Compares if the first specified operand is greater than the second.
         *
         * @param value1 First operand.
         * @param value2 Second operand.
         * @return <code>true</code> if the first specified operand is greater
         *         than the second; <code>false</code> otherwise.
         */
        @Override
        protected boolean compare(V value1, V value2) {
            return value1 == null || value2 == null || comparator.compare(value1, value2) > 0;
        }

    }

    // GreaterThanEqual

    /**
     * This class provides <code>PropertyComparisonConstraint</code>
     * implementation that applies <code>&gt;=</code> operator to a value and
     * other property.
     *
     * @author Fox Mulder
     * @param <V> The type of operands
     * @see GreaterThanEqual
     * @see ConstraintFactory#gte(Class, String)
     * @see ConstraintFactory#gte(Class, String, Comparator)
     */
    public static final class GreaterThanEqualOp<V> extends PropertyComparisonConstraint<V> {

        /**
         * Constructs a new <code>PropertyComparisonConstraint.GreaterThanEqualOp</code>
         * with the specified type of the operands, property name and
         * comparator.
         *
         * @param type Type of the operands.
         * @param propertyName Property name of the second operand.
         * @param comparator Comparator to be used for operands comparison.
         * @throws NullPointerException if the specified type or propertyName
         *         or comparator is <code>null</code>.
         */
        GreaterThanEqualOp(Class<?> type, String propertyName, Comparator<V> comparator) {
            super(type, propertyName, comparator);
        }

        /**
         * Constructs a new <code>PropertyComparisonConstraint.GreaterThanEqualOp</code>
         * from the specified annotation and type of the operands.
         *
         * @param annotation Constraint annotation.
         * @param type Type of the operands.
         * @throws UnsupportedOperationException if the specified annotation
         *         defines comparator that is not applicable to the specified
         *         operands type.
         */
        GreaterThanEqualOp(GreaterThanEqual annotation, Class<V> type) {
            super(type, annotation.value(), DefaultComparator.getInstance(type, annotation.comparator()));
        }

        /**
         * Compares if the first specified operand is greater than or equal to
         * the second.
         *
         * @param value1 First operand.
         * @param value2 Second operand.
         * @return <code>true</code> if the first specified operand is greater
         *         than or equal to the second; <code>false</code> otherwise.
         */
        @Override
        protected boolean compare(V value1, V value2) {
            return value1 == null || value2 == null || comparator.compare(value1, value2) >= 0;
        }

    }

}
