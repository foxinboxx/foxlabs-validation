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

import java.lang.reflect.Array;

import java.util.Collection;
import java.util.Map;

import org.foxlabs.validation.ValidationContext;

import org.foxlabs.common.Predicates;

/**
 * This class provides base implementation of the <code>CheckConstraint</code>
 * that checks whether the size of a value is within allowed minimum and
 * maximum bounds.
 *
 * @author Fox Mulder
 * @param <V> The type of value to be validated
 */
public abstract class SizeConstraint<V> extends CheckConstraint<V> {

    /**
     * Allowed minimum size.
     */
    private final int minSize;

    /**
     * Allowed maximum size.
     */
    private final int maxSize;

    /**
     * Constructs a new <code>SizeConstraint</code> with the specified minimum
     * and maximum sizes.
     *
     * @param minSize Minimum allowed size.
     * @param maxSize Maximum allowed size.
     * @throws IllegalArgumentException if the specified minimum or maximum size
     *         is negative.
     */
    protected SizeConstraint(int minSize, int maxSize) {
        Predicates.require(minSize, Predicates.INT_POSITIVE_OR_ZERO, "minSize");
        Predicates.require(maxSize, Predicates.INT_POSITIVE_OR_ZERO, "maxSize");

        if (minSize < maxSize) {
            this.minSize = minSize;
            this.maxSize = maxSize;
        } else {
            this.minSize = maxSize;
            this.maxSize = minSize;
        }
    }

    /**
     * Returns allowed minimum size.
     *
     * @return Allowed minimum size.
     */
    public final int getMinSize() {
        return minSize;
    }

    /**
     * Returns allowed maximum size.
     *
     * @return Allowed maximum size.
     */
    public final int getMaxSize() {
        return maxSize;
    }

    /**
     * Returns localized error message template.
     *
     * @param context Validation context.
     * @return Localized error message template.
     */
    @Override
    public String getMessageTemplate(ValidationContext<?> context) {
        return context.resolveMessage(SizeConstraint.class.getName() +
                (minSize > 0 ? maxSize == Integer.MAX_VALUE ? ".min" : "" : maxSize < Integer.MAX_VALUE ? ".max" : ""));
    }

    /**
     * Appends <code>minSize</code> and <code>maxSize</code> arguments that
     * contain minimum and maximum allowed size respectively.
     *
     * @param context Validation context.
     * @param arguments Arguments to be substituted into the error message
     *        template.
     * @return <code>true</code>.
     */
    @Override
    public boolean appendMessageArguments(ValidationContext<?> context, Map<String, Object> arguments) {
        super.appendMessageArguments(context, arguments);
        arguments.put("minSize", minSize);
        arguments.put("maxSize", maxSize);
        return true;
    }

    /**
     * Checks whether the size of the specified value is within allowed
     * minimum and maximum bounds.
     *
     * @param value Value which size to be checked.
     * @param context Validation context.
     * @return <code>true</code> if the size of the specified value is within
     *         allowed bounds; <code>false</code> otherwise.
     */
    @Override
    protected final <T> boolean check(V value, ValidationContext<T> context) {
        if (value == null)
            return true;
        int size = getSize(value, context);
        return size >= minSize && size <= maxSize;
    }

    /**
     * Returns size of the specified value.
     *
     * @param <T> The type of validated entity.
     * @param value Value which size to be returned.
     * @param context Validation context.
     * @return Size of the specified value.
     */
    protected abstract <T> int getSize(V value, ValidationContext<T> context);

    // StringType

    /**
     * This class provides <code>SizeConstraint</code> implementation for
     * <code>java.lang.String</code> type.
     *
     * @author Fox Mulder
     * @see Size
     * @see MinSize
     * @see MaxSize
     * @see ConstraintFactory#stringSize(int, int)
     * @see ConstraintFactory#stringMinSize(int)
     * @see ConstraintFactory#stringMaxSize(int)
     */
    public static final class StringType extends SizeConstraint<String> {

        /**
         * Constructs a new <code>SizeConstraint.StringType</code> with the
         * specified minimum and maximum lengths of the string.
         *
         * @param minSize Minimum length of the string.
         * @param maxSize Maximum length of the string.
         * @throws IllegalArgumentException if the specified minimum or maximum
         *         length is negative.
         */
        StringType(int minSize, int maxSize) {
            super(minSize, maxSize);
        }

        /**
         * Constructs a new <code>SizeConstraint.StringType</code> from the
         * specified annotation.
         *
         * @param annotation Constraint annotation.
         * @throws IllegalArgumentException if the specified annotation defines
         *         negative minimum length.
         */
        StringType(MinSize annotation) {
            this(annotation.value(), Integer.MAX_VALUE);
        }

        /**
         * Constructs a new <code>SizeConstraint.StringType</code> from the
         * specified annotation.
         *
         * @param annotation Constraint annotation.
         * @throws IllegalArgumentException if the specified annotation defines
         *         negative maximum length.
         */
        StringType(MaxSize annotation) {
            this(0, annotation.value());
        }

        /**
         * Constructs a new <code>SizeConstraint.StringType</code> from the
         * specified annotation.
         *
         * @param annotation Constraint annotation.
         * @throws IllegalArgumentException if the specified annotation defines
         *         negative minimum or maximum length.
         */
        StringType(Size annotation) {
            this(annotation.min(), annotation.max());
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
         * Returns length of the specified string.
         *
         * @param value String.
         * @param context Validation context.
         * @return Length of the specified string.
         */
        @Override
        protected <T> int getSize(String value, ValidationContext<T> context) {
            return value.length();
        }

    }

    // ArrayType

    /**
     * This class provides <code>SizeConstraint</code> implementation for all
     * array types.
     *
     * @author Fox Mulder
     * @see Size
     * @see MinSize
     * @see MaxSize
     * @see ConstraintFactory#arraySize(int, int)
     * @see ConstraintFactory#arrayMinSize(int)
     * @see ConstraintFactory#arrayMaxSize(int)
     */
    public static final class ArrayType extends SizeConstraint<Object> {

        /**
         * Constructs a new <code>SizeConstraint.ArrayType</code> with the
         * specified minimum and maximum lengths of the array.
         *
         * @param minSize Minimum length of the array.
         * @param maxSize Maximum length of the array.
         * @throws IllegalArgumentException if the specified minimum or maximum
         *         length is negative.
         */
        ArrayType(int min, int max) {
            super(min, max);
        }

        /**
         * Constructs a new <code>SizeConstraint.ArrayType</code> from the
         * specified annotation.
         *
         * @param annotation Constraint annotation.
         * @throws IllegalArgumentException if the specified annotation defines
         *         negative minimum length.
         */
        ArrayType(MinSize annotation) {
            this(annotation.value(), Integer.MAX_VALUE);
        }

        /**
         * Constructs a new <code>SizeConstraint.ArrayType</code> from the
         * specified annotation.
         *
         * @param annotation Constraint annotation.
         * @throws IllegalArgumentException if the specified annotation defines
         *         negative maximum length.
         */
        ArrayType(MaxSize annotation) {
            this(0, annotation.value());
        }

        /**
         * Constructs a new <code>SizeConstraint.ArrayType</code> from the
         * specified annotation.
         *
         * @param annotation Constraint annotation.
         * @throws IllegalArgumentException if the specified annotation defines
         *         negative minimum or maximum length.
         */
        ArrayType(Size annotation) {
            this(annotation.min(), annotation.max());
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
         * Returns length of the specified array.
         *
         * @param value Array.
         * @param context Validation context.
         * @return Length of the specified array.
         */
        @Override
        protected <T> int getSize(Object value, ValidationContext<T> context) {
            return Array.getLength(value);
        }

    }

    // CollectionType

    /**
     * This class provides <code>SizeConstraint</code> implementation for all
     * JDK <code>java.util.Collection</code> types.
     *
     * @author Fox Mulder
     * @see Size
     * @see MinSize
     * @see MaxSize
     * @see ConstraintFactory#collectionSize(int, int)
     * @see ConstraintFactory#collectionMinSize(int)
     * @see ConstraintFactory#collectionMaxSize(int)
     */
    public static final class CollectionType extends SizeConstraint<Collection<?>> {

        /**
         * Constructs a new <code>SizeConstraint.CollectionType</code> with the
         * specified minimum and maximum sizes of the collection.
         *
         * @param minSize Minimum size of the collection.
         * @param maxSize Maximum size of the collection.
         * @throws IllegalArgumentException if the specified minimum or maximum
         *         size is negative.
         */
        CollectionType(int min, int max) {
            super(min, max);
        }

        /**
         * Constructs a new <code>SizeConstraint.CollectionType</code> from the
         * specified annotation.
         *
         * @param annotation Constraint annotation.
         * @throws IllegalArgumentException if the specified annotation defines
         *         negative minimum size.
         */
        CollectionType(MinSize annotation) {
            this(annotation.value(), Integer.MAX_VALUE);
        }

        /**
         * Constructs a new <code>SizeConstraint.CollectionType</code> from the
         * specified annotation.
         *
         * @param annotation Constraint annotation.
         * @throws IllegalArgumentException if the specified annotation defines
         *         negative maximum size.
         */
        CollectionType(MaxSize annotation) {
            this(0, annotation.value());
        }

        /**
         * Constructs a new <code>SizeConstraint.CollectionType</code> from the
         * specified annotation.
         *
         * @param annotation Constraint annotation.
         * @throws IllegalArgumentException if the specified annotation defines
         *         negative minimum or maximum size.
         */
        CollectionType(Size annotation) {
            this(annotation.min(), annotation.max());
        }

        /**
         * Returns <code>java.util.Collection</code> type.
         *
         * @return <code>java.util.Collection</code> type.
         */
        @Override
        public Class<?> getType() {
            return Collection.class;
        }

        /**
         * Returns size of the specified collection.
         *
         * @param value Collection.
         * @param context Validation context.
         * @return Size of the specified collection.
         */
        @Override
        protected <T> int getSize(Collection<?> value, ValidationContext<T> context) {
            return value.size();
        }

    }

    // MapType

    /**
     * This class provides <code>SizeConstraint</code> implementation for all
     * JDK <code>java.util.Map</code> types.
     *
     * @author Fox Mulder
     * @see Size
     * @see MinSize
     * @see MaxSize
     * @see ConstraintFactory#mapSize(int, int)
     * @see ConstraintFactory#mapMinSize(int)
     * @see ConstraintFactory#mapMaxSize(int)
     */
    public static final class MapType extends SizeConstraint<Map<?, ?>> {

        /**
         * Constructs a new <code>SizeConstraint.MapType</code> with the
         * specified minimum and maximum sizes of the map.
         *
         * @param minSize Minimum size of the map.
         * @param maxSize Maximum size of the map.
         * @throws IllegalArgumentException if the specified minimum or maximum
         *         size is negative.
         */
        MapType(int min, int max) {
            super(min, max);
        }

        /**
         * Constructs a new <code>SizeConstraint.MapType</code> from the
         * specified annotation.
         *
         * @param annotation Constraint annotation.
         * @throws IllegalArgumentException if the specified annotation defines
         *         negative minimum size.
         */
        MapType(MinSize annotation) {
            this(annotation.value(), Integer.MAX_VALUE);
        }

        /**
         * Constructs a new <code>SizeConstraint.MapType</code> from the
         * specified annotation.
         *
         * @param annotation Constraint annotation.
         * @throws IllegalArgumentException if the specified annotation defines
         *         negative maximum size.
         */
        MapType(MaxSize annotation) {
            this(0, annotation.value());
        }

        /**
         * Constructs a new <code>SizeConstraint.MapType</code> from the
         * specified annotation.
         *
         * @param annotation Constraint annotation.
         * @throws IllegalArgumentException if the specified annotation defines
         *         negative minimum or maximum size.
         */
        MapType(Size annotation) {
            this(annotation.min(), annotation.max());
        }

        /**
         * Returns <code>java.util.Map</code> type.
         *
         * @return <code>java.util.Map</code> type.
         */
        @Override
        public Class<?> getType() {
            return Map.class;
        }

        /**
         * Returns size of the specified map.
         *
         * @param value Map.
         * @param context Validation context.
         * @return Size of the specified map.
         */
        @Override
        protected <T> int getSize(Map<?, ?> value, ValidationContext<T> context) {
            return value.size();
        }

    }

}
