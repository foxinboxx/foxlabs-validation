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

/**
 * This class provides <code>java.util.Comparator</code> implementation for
 * objects that implement <code>java.lang.Comparable</code> interface.
 * 
 * @author Fox Mulder
 * @see RangeConstraint
 * @see PropertyComparisonConstraint
 */
public final class DefaultComparator implements Comparator<Comparable<Object>> {
    
    /**
     * <code>DefaultComparator</code> single instance.
     */
    public static final DefaultComparator INSTANCE = new DefaultComparator();
    
    /**
     * Constructs default <code>DefaultComparator</code>.
     */
    private DefaultComparator() {}
    
    /**
     * Compares two <code>java.lang.Comparable</code> objects.
     * 
     * @param o1 The first object to be compared.
     * @param o2 The second object to be compared.
     * @return A negative integer, zero, or a positive integer as the first
     *         object is less than, equal to, or greater than the second.
     */
    @Override
    public int compare(Comparable<Object> o1, Comparable<Object> o2) {
        return o1.compareTo(o2);
    }
    
    /**
     * Returns comparator for the specified object type and comparator type.
     * 
     * @param <V> The value type.
     * @param type Type of the objects to be compared.
     * @param comparatorType Type of the comparator.
     * @return Comparator instance.
     * @throws UnsupportedOperationException if the specified comparator is not
     *         applicable to the specified object type.
     * @throws IllegalArgumentException if comparator instantiation fails.
     */
    @SuppressWarnings("unchecked")
    public static <V> Comparator<V> getInstance(Class<V> type, Class<? extends Comparator<?>> comparatorType) {
        if (comparatorType == DefaultComparator.class) {
            if (Comparable.class.isAssignableFrom(type) || type.isPrimitive())
                return (Comparator<V>) INSTANCE;
            throw new UnsupportedOperationException();
        }
        try {
            return (Comparator<V>) comparatorType.newInstance();
        } catch (InstantiationException e) {
            throw new IllegalArgumentException(e);
        } catch (IllegalAccessException e) {
            throw new IllegalArgumentException(e);
        }
    }
    
}
