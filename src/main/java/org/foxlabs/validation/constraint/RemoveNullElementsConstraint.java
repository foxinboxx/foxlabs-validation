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

import java.util.Collection;
import java.util.Iterator;
import java.util.Arrays;

import org.foxlabs.validation.ValidationContext;

/**
 * This class provides base implementation of the <code>CorrectConstraint</code>
 * that removes all <code>null</code> elements from a sequence.
 * 
 * @author Fox Mulder
 * @param <V> The type of value to be validated
 * @see RemoveNullElements
 */
public abstract class RemoveNullElementsConstraint<V> extends CorrectConstraint<V> {
    
    // ArrayType
    
    /**
     * This class provides <code>RemoveNullElementsConstraint</code>
     * implementation for all array types.
     * 
     * @author Fox Mulder
     * @see ConstraintFactory#arrayRemoveNullElements()
     */
    public static final class ArrayType extends RemoveNullElementsConstraint<Object[]> {
        
        /**
         * <code>RemoveNullElementsConstraint.ArrayType</code> single instance.
         */
        public static final ArrayType DEFAULT = new ArrayType();
        
        /**
         * Constructs default <code>RemoveNullElementsConstraint.ArrayType</code>.
         */
        private ArrayType() {}
        
        /**
         * Returns <code>java.lang.Object</code> array type.
         * 
         * @return <code>java.lang.Object</code> array type.
         */
        @Override
        public Class<?> getType() {
            return Object[].class;
        }
        
        /**
         * Removes all <code>null</code> elements from the specified array.
         * 
         * @param array Array to be corrected.
         * @param context Validation context.
         * @return Array without <code>null</code> elements.
         */
        @Override
        public <T> Object[] validate(Object[] array, ValidationContext<T> context) {
            if (!(array == null || array.length == 0)) {
                int length = 0;
                for (int i = 0; i < array.length; i++) {
                    if (array[i] != null)
                        array[length++] = array[i];
                }
                if (length < array.length)
                    array = Arrays.copyOf(array, length);
            }
            return array;
        }
        
    }
    
    // CollectionType
    
    /**
     * This class provides <code>RemoveNullElementsConstraint</code>
     * implementation for all JDK <code>java.util.Collection</code> types.
     * 
     * @author Fox Mulder
     * @see ConstraintFactory#collectionRemoveNullElements()
     */
    public static final class CollectionType extends RemoveNullElementsConstraint<Collection<?>> {
        
        /**
         * <code>RemoveNullElementsConstraint.CollectionType</code> single
         * instance.
         */
        public static final CollectionType DEFAULT = new CollectionType();
        
        /**
         * Constructs default <code>RemoveNullElementsConstraint.CollectionType</code>.
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
         * Removes all <code>null</code> elements from the specified collection.
         * 
         * @param collection Collection to be corrected.
         * @param context Validation context.
         * @return Collection without <code>null</code> elements.
         */
        @Override
        public <T> Collection<?> validate(Collection<?> collection, ValidationContext<T> context) {
            if (!(collection == null || collection.isEmpty())) {
                Iterator<?> itr = collection.iterator();
                while (itr.hasNext()) {
                    if (itr.next() == null) {
                        itr.remove();
                    }
                }
            }
            return collection;
        }
        
    }
    
}
