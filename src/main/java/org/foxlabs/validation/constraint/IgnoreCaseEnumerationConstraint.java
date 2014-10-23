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
import java.util.Set;
import java.util.TreeSet;
import java.util.Comparator;

import org.foxlabs.util.Assert;

/**
 * This class provides <code>EnumerationConstraint</code> implementation based
 * on static set of case insensitive strings.
 * 
 * @author Fox Mulder
 * @see IgnoreCaseEnumeration
 * @see ConstraintFactory#ignoreCaseEnumeration(String...)
 * @see ConstraintFactory#ignoreCaseEnumeration(Collection)
 */
public class IgnoreCaseEnumerationConstraint extends EnumerationConstraint.Default<String> {
    
    /**
     * Constructs a new <code>IgnoreCaseEnumerationConstraint</code> with the
     * specified array of allowed strings.
     * 
     * @param constants Array of allowed strings.
     * @throws IllegalArgumentException if the specified array of allowed
     *         strings is <code>null</code> or empty or contains
     *         <code>null</code> elements.
     */
    protected IgnoreCaseEnumerationConstraint(String... constants) {
        super(String.class, toIgnoreCaseSet(Assert.notEmpty(constants, "constants")));
    }
    
    /**
     * Constructs a new <code>IgnoreCaseEnumerationConstraint</code> with the
     * specified collection of allowed strings.
     * 
     * @param constants Collection of allowed strings.
     * @throws IllegalArgumentException if the specified collection of allowed
     *         strings is <code>null</code> or empty or contains
     *         <code>null</code> elements.
     */
    protected IgnoreCaseEnumerationConstraint(Collection<String> constants) {
        super(String.class, toIgnoreCaseSet(Assert.notEmpty(constants, "constants")));
    }
    
    /**
     * Constructs a new <code>IgnoreCaseEnumerationConstraint</code> from the
     * specified annotation.
     * 
     * @param annotation Constraint annotation.
     * @throws IllegalArgumentException if the specified annotation defines
     *         empty set of allowed strings.
     */
    protected IgnoreCaseEnumerationConstraint(IgnoreCaseEnumeration annotation) {
        this(annotation.value());
    }
    
    /**
     * Case insensitive string comparator.
     */
    static final Comparator<String> ignoreCaseComparator = new Comparator<String>() {
        public int compare(String s1, String s2) {
            return s1.toUpperCase().compareTo(s2.toUpperCase());
        }
    };
    
    /**
     * Returns case insensitive set of strings from the specified array of
     * strings.
     * 
     * @param constants Array of strings.
     * @return Case insensitive set of strings.
     */
    public static Set<String> toIgnoreCaseSet(String... constants) {
        TreeSet<String> constantSet = new TreeSet<String>(ignoreCaseComparator);
        for (String constant : constants)
            constantSet.add(constant);
        return constantSet;
    }
    
    /**
     * Returns case insensitive set of strings from the specified collection of
     * strings.
     * 
     * @param constants Collection of strings.
     * @return Case insensitive set of strings.
     */
    public static Set<String> toIgnoreCaseSet(Collection<String> constants) {
        TreeSet<String> constantSet = new TreeSet<String>(ignoreCaseComparator);
        constantSet.addAll(constants);
        return constantSet;
    }
    
}
