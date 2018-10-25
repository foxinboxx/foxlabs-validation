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

/**
 * This class provides base implementation of the <code>CheckConstraint</code>
 * that checks whether a value is not empty.
 * 
 * @author Fox Mulder
 * @param <V> The type of value to be validated
 * @see NotEmpty
 */
public abstract class NotEmptyConstraint<V> extends CheckConstraint<V> {
    
    /**
     * Returns localized error message template.
     * 
     * @param context Validation context.
     * @return Localized error message template.
     */
    @Override
    public String getMessageTemplate(ValidationContext<?> context) {
        return context.resolveMessage(NotEmptyConstraint.class.getName());
    }
    
    /**
     * Checks whether the specified value is not empty.
     * 
     * @param value Value to be checked for empty.
     * @param context Validation context.
     * @return <code>true</code> if the specified value is not empty;
     *         <code>false</code> otherwise.
     */
    @Override
    protected final <T> boolean check(V value, ValidationContext<T> context) {
        return value == null || !isEmpty(value, context);
    }
    
    /**
     * Determines whether the specified value is empty.
     * 
     * @param <T> The type of validated entity.
     * @param value Value to be checked for empty.
     * @param context Validation context.
     * @return <code>true</code> if the specified value is empty;
     *         <code>false</code> otherwise.
     */
    protected abstract <T> boolean isEmpty(V value, ValidationContext<T> context);
    
    // StringType
    
    /**
     * This class provides <code>NotEmptyConstraint</code> implementation for
     * <code>java.lang.String</code> type.
     * 
     * @author Fox Mulder
     * @see ConstraintFactory#stringNotEmpty()
     */
    public static final class StringType extends NotEmptyConstraint<String> {
        
        /**
         * <code>NotEmptyConstraint.StringType</code> single instance.
         */
        public static final StringType DEFAULT = new StringType();
        
        /**
         * Constructs default <code>NotEmptyConstraint.StringType</code>.
         */
        private StringType() {}
        
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
         * Determines whether the specified string is empty.
         * 
         * @param value String to be checked for empty.
         * @param context Validation context.
         * @return <code>true</code> if the specified string is empty;
         *         <code>false</code> otherwise.
         */
        protected <T> boolean isEmpty(String value, ValidationContext<T> context) {
            return value.isEmpty();
        }
        
    }
    
    // ArrayType
    
    /**
     * This class provides <code>NotEmptyConstraint</code> implementation for
     * all array types.
     * 
     * @author Fox Mulder
     * @see ConstraintFactory#arrayNotEmpty()
     */
    public static final class ArrayType extends NotEmptyConstraint<Object> {
        
        /**
         * <code>NotEmptyConstraint.ArrayType</code> single instance.
         */
        public static final ArrayType DEFAULT = new ArrayType();
        
        /**
         * Constructs default <code>NotEmptyConstraint.ArrayType</code>.
         */
        private ArrayType() {}
        
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
         * Determines whether the specified array is empty.
         * 
         * @param value Array to be checked for empty.
         * @param context Validation context.
         * @return <code>true</code> if the specified array is empty;
         *         <code>false</code> otherwise.
         */
        protected <T> boolean isEmpty(Object value, ValidationContext<T> context) {
            return Array.getLength(value) == 0;
        }
        
    }
    
    // CollectionType
    
    /**
     * This class provides <code>NotEmptyConstraint</code> implementation for
     * all JDK <code>java.util.Collection</code> types.
     * 
     * @author Fox Mulder
     * @see ConstraintFactory#collectionNotEmpty()
     */
    public static final class CollectionType extends NotEmptyConstraint<Collection<?>> {
        
        /**
         * <code>NotEmptyConstraint.CollectionType</code> single instance.
         */
        public static final CollectionType DEFAULT = new CollectionType();
        
        /**
         * Constructs default <code>NotEmptyConstraint.CollectionType</code>.
         */
        private CollectionType() {}
        
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
         * Determines whether the specified collection is empty.
         * 
         * @param value Collection to be checked for empty.
         * @param context Validation context.
         * @return <code>true</code> if the specified collection is empty;
         *         <code>false</code> otherwise.
         */
        protected <T> boolean isEmpty(Collection<?> value, ValidationContext<T> context) {
            return value.isEmpty();
        }
        
    }
    
    // MapType
    
    /**
     * This class provides <code>NotEmptyConstraint</code> implementation for
     * all JDK <code>java.util.Map</code> types.
     * 
     * @author Fox Mulder
     * @see ConstraintFactory#mapNotEmpty()
     */
    public static final class MapType extends NotEmptyConstraint<Map<?, ?>> {
        
        /**
         * <code>NotEmptyConstraint.MapType</code> single instance.
         */
        public static final MapType DEFAULT = new MapType();
        
        /**
         * Constructs default <code>NotEmptyConstraint.MapType</code>.
         */
        private MapType() {}
        
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
         * Determines whether the specified map is empty.
         * 
         * @param value Map to be checked for empty.
         * @param context Validation context.
         * @return <code>true</code> if the specified map is empty;
         *         <code>false</code> otherwise.
         */
        protected <T> boolean isEmpty(Map<?, ?> value, ValidationContext<T> context) {
            return value.isEmpty();
        }
        
    }
    
}
