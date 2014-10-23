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

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.IdentityHashMap;

import org.foxlabs.validation.ValidationContext;
import org.foxlabs.validation.ValidationTarget;

/**
 * This class provides <code>SequenceElementConstraint</code> implementation
 * that performs validation of map keys.
 * 
 * @author Fox Mulder
 * @param <V> The type of map keys
 * @see ConstraintFactory#mapKey(Constraint)
 */
public final class MapKeyConstraint<V> extends SequenceElementConstraint<Map<V, Object>, V> {
    
    /**
     * Constructs a new <code>MapKeyConstraint</code> with the specified
     * constraint of map keys.
     * 
     * @param constraint Constraint to be used for validation of map keys.
     * @throws NullPointerException if the specified constraint is
     *         <code>null</code>.
     */
    MapKeyConstraint(Constraint<V> constraint) {
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
     * Checks whether all keys of the specified map conforms to the constraint
     * of map keys.
     * 
     * @param map Map whose keys to be checked.
     * @param context Validation context.
     * @param violations List of violations.
     * @return Map with possibly modified keys if constraint of map keys can
     *         modify values; unmodified map otherwise.
     */
    @Override
    protected <T> Map<V, Object> doValidate(Map<V, Object> map, ValidationContext<T> context,
            List<ConstraintViolationException> violations) {
        context.setCurrentTarget(ValidationTarget.KEYS);
        Map<V, Object> newKeys = new IdentityHashMap<V, Object>();
        Iterator<Map.Entry<V, Object>> itr = map.entrySet().iterator();
        while (itr.hasNext()) {
            try {
                Map.Entry<V, Object> entry = itr.next();
                context.setCurrentIndex(entry.getKey());
                V oldKey = entry.getKey();
                V newKey = constraint.validate(oldKey, context);
                if (oldKey == null ? newKey != null : !oldKey.equals(newKey)) {
                    newKeys.put(newKey, entry.getValue());
                    itr.remove();
                }
            } catch (ConstraintViolationException e) {
                violations.add(e);
            }
        }
        map.putAll(newKeys);
        return map;
    }
    
}
