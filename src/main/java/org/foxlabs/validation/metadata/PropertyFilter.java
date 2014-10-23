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

package org.foxlabs.validation.metadata;

import java.util.Arrays;
import java.util.Collection;
import java.util.Set;
import java.util.HashSet;

import org.foxlabs.validation.ConstrainedMap;
import org.foxlabs.validation.ValidationContext;
import org.foxlabs.validation.Validator;

/**
 * Defines a property filter interface.
 * 
 * @author Fox Mulder
 * @see Validator
 * @see ConstrainedMap
 * @see ValidationContext
 */
public interface PropertyFilter {
    
    /**
     * Determines if the specified property should be accepted.
     * 
     * @param metadata Property metadata descriptor.
     * @return <code>true</code> if the specified property should be accepted;
     *         <code>false</code> otherwise.
     */
    boolean accept(PropertyMetaData<?, ?> metadata);
    
    /**
     * Default <code>PropertyFilter</code> that accepts any property.
     */
    PropertyFilter ALL = new PropertyFilter() {
        public boolean accept(PropertyMetaData<?, ?> metadata) {
            return true;
        }
    };
    
    // Not
    
    /**
     * This class provides <code>PropertyFilter</code> implementation that is
     * negation of another filter.
     * 
     * @author Fox Mulder
     */
    class Not implements PropertyFilter {
        
        /**
         * Property filter.
         */
        private final PropertyFilter filter;
        
        /**
         * Constructs a new <code>PropertyFilter.Not</code> with the specified
         * another property filter.
         * 
         * @param filter Another property filter.
         */
        public Not(PropertyFilter filter) {
            this.filter = filter;
        }
        
        /**
         * Checks whether the encapsulated filter not accepts the specified
         * property.
         * 
         * @param metadata Property metadata descriptor.
         * @return <code>true</code> if the encapsulated filter not accepts the
         *         specified property; <code>false</code> otherwise.
         */
        public boolean accept(PropertyMetaData<?, ?> metadata) {
            return !filter.accept(metadata);
        }
        
    }
    
    // And
    
    /**
     * This class provides <code>PropertyFilter</code> implementation that is
     * conjunction of another property filters.
     * 
     * @author Fox Mulder
     */
    class And implements PropertyFilter {
        
        /**
         * Array of property filters.
         */
        private final PropertyFilter[] filters;
        
        /**
         * Constructs a new <code>PropertyFilter.And</code> with the specified
         * array of property filters.
         * 
         * @param filters Array of property filters.
         */
        public And(PropertyFilter... filters) {
            this.filters = filters;
        }
        
        /**
         * Checks whether all of the encapsulated filters accept the specified
         * property.
         * 
         * @param metadata Property metadata descriptor.
         * @return <code>true</code> if all of the encapsulated filters accept
         *         the specified property; <code>false</code> otherwise.
         */
        public boolean accept(PropertyMetaData<?, ?> metadata) {
            for (PropertyFilter filter : filters)
                if (!filter.accept(metadata))
                    return false;
            return true;
        }
        
    }
    
    // Or
    
    /**
     * This class provides <code>PropertyFilter</code> implementation that is
     * disjunction of another property filters.
     * 
     * @author Fox Mulder
     */
    class Or implements PropertyFilter {
        
        /**
         * Array of property filters.
         */
        private final PropertyFilter[] filters;
        
        /**
         * Constructs a new <code>PropertyFilter.Or</code> with the specified
         * array of property filters.
         * 
         * @param filters Array of property filters.
         */
        public Or(PropertyFilter... filters) {
            this.filters = filters;
        }
        
        /**
         * Checks whether at least one of the encapsulated filters accepts the
         * specified property.
         * 
         * @param metadata Property metadata descriptor.
         * @return <code>true</code> if at least one of the encapsulated
         *         filters accepts the specified property; <code>false</code>
         *         otherwise.
         */
        public boolean accept(PropertyMetaData<?, ?> metadata) {
            for (PropertyFilter filter : filters)
                if (filter.accept(metadata))
                    return true;
            return false;
        }
        
    }
    
    // NameSet
    
    /**
     * This class provides <code>PropertyFilter</code> implementation that
     * accepts only properties with particular names.
     * 
     * @author Fox Mulder
     */
    class NameSet implements PropertyFilter {
        
        /**
         * Set of property names.
         */
        private final Set<String> names;
        
        /**
         * Constructs a new <code>PropertyFilter.NameSet</code> with the
         * specified array of property names.
         * 
         * @param names Array of property names.
         */
        public NameSet(String... names) {
            this.names = new HashSet<String>(Arrays.asList(names));
        }
        
        /**
         * Constructs a new <code>PropertyFilter.NameSet</code> with the
         * specified collection of property names.
         * 
         * @param names Collection of property names.
         */
        public NameSet(Collection<String> names) {
            this.names = names instanceof Set ? (Set<String>) names : new HashSet<String>(names);
        }
        
        /**
         * Determines if set of provided property names contains name of the
         * specified property.
         * 
         * @param metadata Property metadata descriptor.
         * @return <code>true</code> if set of provided property names contains
         *         name of the specified property; <code>false</code> otherwise.
         */
        public boolean accept(PropertyMetaData<?, ?> metadata) {
            return names.contains(metadata.getName());
        }
        
    }
    
    // NameStartsWith
    
    /**
     * This class provides <code>PropertyFilter</code> implementation that
     * accepts only properties which names starts with a particular prefix.
     * 
     * @author Fox Mulder
     */
    class NameStartsWith implements PropertyFilter {
        
        /**
         * Prefix of property name.
         */
        final String prefix;
        
        /**
         * Constructs a new <code>PropertyFilter.NameStartsWith</code> with the
         * specified prefix.
         * 
         * @param prefix Prefix of property name.
         */
        public NameStartsWith(String prefix) {
            this.prefix = prefix;
        }
        
        /**
         * Determines if name of the specified property starts with provided
         * prefix.
         * 
         * @param metadata Property metadata descriptor.
         * @return <code>true</code> if name of the specified property starts
         *         with provided prefix; <code>false</code> otherwise.
         */
        public boolean accept(PropertyMetaData<?, ?> metadata) {
            return metadata.getName().startsWith(prefix);
        }
        
    }
    
    // NameEndsWith
    
    /**
     * This class provides <code>PropertyFilter</code> implementation that
     * accepts only properties which names ends with a particular suffix.
     * 
     * @author Fox Mulder
     */
    class NameEndsWith implements PropertyFilter {
        
        /**
         * Suffix of property name.
         */
        final String suffix;
        
        /**
         * Constructs a new <code>PropertyFilter.NameEndsWith</code> with the
         * specified suffix.
         * 
         * @param suffix Suffix of property name.
         */
        public NameEndsWith(String suffix) {
            this.suffix = suffix;
        }
        
        /**
         * Determines if name of the specified property ends with provided
         * suffix.
         * 
         * @param metadata Property metadata descriptor.
         * @return <code>true</code> if name of the specified property ends
         *         with provided suffix; <code>false</code> otherwise.
         */
        public boolean accept(PropertyMetaData<?, ?> metadata) {
            return metadata.getName().endsWith(suffix);
        }
        
    }
    
}
