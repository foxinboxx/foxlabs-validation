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

import java.util.List;
import java.util.Map;

import org.foxlabs.validation.ValidationContext;
import org.foxlabs.validation.ValidationTarget;

/**
 * This class provides <code>SequenceElementConstraint</code> implementation
 * that performs validation of map values.
 * 
 * @author Fox Mulder
 * @param <V> The type of map values
 * @see ConstraintFactory#mapValue(Constraint)
 */
public final class MapValueConstraint<V> extends SequenceElementConstraint<Map<Object, V>, V> {
    
    /**
     * Constructs a new <code>MapValueConstraint</code> with the specified
     * constraint of map values.
     * 
     * @param constraint Constraint to be used for validation of map values.
     * @throws NullPointerException if the specified constraint is
     *         <code>null</code>.
     */
    MapValueConstraint(Constraint<V> constraint) {
        super(constraint);
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
     * Checks whether all values of the specified map conforms to the constraint
     * of map values.
     * 
     * @param map Map whose values to be checked.
     * @param context Validation context.
     * @param violations List of violations.
     * @return Map with possibly modified values if constraint of map values
     *         can modify values; unmodified map otherwise.
     */
    @Override
    protected <T> Map<Object, V> doValidate(Map<Object, V> map, ValidationContext<T> context,
            List<ConstraintViolationException> violations) {
        context.setCurrentTarget(ValidationTarget.ELEMENTS);
        for (Map.Entry<Object, V> entry : map.entrySet()) {
            try {
                context.setCurrentIndex(entry.getKey());
                V oldValue = entry.getValue();
                V newValue = constraint.validate(oldValue, context);
                if (oldValue == null ? newValue != null : !oldValue.equals(newValue))
                    entry.setValue(newValue);
            } catch (ConstraintViolationException e) {
                violations.add(e);
            }
        }
        return map;
    }
    
}
