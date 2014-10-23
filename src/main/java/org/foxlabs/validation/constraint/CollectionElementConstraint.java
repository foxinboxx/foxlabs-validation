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
import java.util.ListIterator;
import java.util.List;
import java.util.LinkedList;

import org.foxlabs.validation.ValidationContext;
import org.foxlabs.validation.ValidationTarget;

/**
 * This class provides <code>SequenceElementConstraint</code> implementation
 * that performs validation of collection elements.
 * 
 * @author Fox Mulder
 * @param <V> The type of collection elements
 * @see ConstraintFactory#collectionElement(Constraint)
 */
public final class CollectionElementConstraint<V> extends SequenceElementConstraint<Collection<V>, V> {
    
    /**
     * Constructs a new <code>CollectionElementConstraint</code> with the
     * specified constraint of collection elements.
     * 
     * @param constraint Constraint to be used for validation of collection
     *        elements.
     * @throws IllegalArgumentException if the specified constraint is
     *         <code>null</code>.
     */
    CollectionElementConstraint(Constraint<V> constraint) {
        super(constraint);
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
     * Checks whether all elements of the specified collection conforms to the
     * constraint of collection elements.
     * 
     * @param collection Collection whose elements to be checked.
     * @param context Validation context.
     * @param violations List of violations.
     * @return Collection with possibly modified elements if constraint of
     *         collection elements can modify values; unmodified collection
     *         otherwise.
     */
    @Override
    protected <T> Collection<V> doValidate(Collection<V> collection, ValidationContext<T> context,
            List<ConstraintViolationException> violations) {
        context.setCurrentTarget(ValidationTarget.ELEMENTS);
        if (collection instanceof List) {
            ListIterator<V> itr = ((List<V>) collection).listIterator();
            for (int i = 0; itr.hasNext(); i++) {
                try {
                    context.setCurrentIndex(i);
                    V oldValue = itr.next();
                    V newValue = constraint.validate(oldValue, context);
                    if (oldValue != newValue)
                        itr.set(newValue);
                } catch (ConstraintViolationException e) {
                    violations.add(e);
                }
            }
        } else {
            List<V> newValues = new LinkedList<V>();
            Iterator<V> itr = collection.iterator();
            for (int i = 0; itr.hasNext(); i++) {
                try {
                    context.setCurrentIndex(i);
                    V oldValue = itr.next();
                    V newValue = constraint.validate(oldValue, context);
                    if (oldValue == null ? newValue != null : !oldValue.equals(newValue)) {
                        newValues.add(newValue);
                        itr.remove();
                    }
                } catch (ConstraintViolationException e) {
                    violations.add(e);
                }
            }
            collection.addAll(newValues);
        }
        return collection;
    }
    
}
