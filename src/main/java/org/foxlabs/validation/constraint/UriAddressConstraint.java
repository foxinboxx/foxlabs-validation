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

import java.util.Map;
import java.util.Set;
import java.util.Collections;

import java.net.URI;
import java.net.URISyntaxException;

import org.foxlabs.validation.ValidationContext;

import org.foxlabs.util.Assert;

/**
 * This class provides <code>CheckConstraint</code> implementation that checks
 * whether a string is valid URI reference. Also the allowed set of schemes
 * can be configured.
 * 
 * @author Fox Mulder
 * @see UriAddress
 * @see ConstraintFactory#uriAddress()
 * @see ConstraintFactory#uriAddress(String...)
 */
public final class UriAddressConstraint extends CheckConstraint<String> {
    
    /**
     * <code>UriAddressConstraint</code> default instance initialized with
     * empty set of schemes.
     */
    public static final UriAddressConstraint DEFAULT = new UriAddressConstraint((String[]) null);
    
    /**
     * Set of allowed schemes (empty set means all schemes are allowed).
     */
    private final Set<String> schemes;
    
    /**
     * Constructs a new <code>UriAddressConstraint</code> with the specified
     * array of allowed schemes.
     * 
     * @param schemes Array of allowed schemes.
     * @throws IllegalArgumentException if the specified array of schemes
     *         contains <code>null</code> or empty elements.
     */
    UriAddressConstraint(String[] schemes) {
        this.schemes = schemes == null || schemes.length == 0
            ? Collections.<String>emptySet()
            : Collections.unmodifiableSet(Assert.noEmptyStringSet(schemes, "schemes"));
    }
    
    /**
     * Constructs a new <code>UriAddressConstraint</code> from the specified
     * annotation.
     * 
     * @param annotation Constraint annotation.
     * @throws IllegalArgumentException if the specified annotation defines
     *         array of schemes that contains empty elements.
     */
    UriAddressConstraint(UriAddress annotation) {
        this(annotation.schemes());
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
     * Returns set of allowed schemes (empty set means all schemes are
     * allowed).
     * 
     * @return Set of allowed schemes.
     */
    public Set<String> getSchemes() {
        return schemes;
    }
    
    /**
     * Appends <code>schemes</code> argument that contains set of allowed
     * schemes.
     * 
     * @param context Validation context.
     * @param arguments Arguments to be substituted into the error message
     *        template.
     * @return <code>true</code>.
     */
    @Override
    public boolean appendMessageArguments(ValidationContext<?> context, Map<String, Object> arguments) {
        super.appendMessageArguments(context, arguments);
        if (schemes.size() > 0)
            arguments.put("schemes", schemes);
        return true;
    }
    
    /**
     * Checks whether the specified string is valid URI reference with allowed
     * scheme.
     * 
     * @param value URI string.
     * @param context Validation context.
     * @return <code>true</code> if the specified string is valid URI reference
     *         with allowed scheme; <code>false</code> otherwise.
     */
    @Override
    protected <T> boolean check(String value, ValidationContext<T> context) {
        if (value == null)
            return true;
        try {
            URI uri = new URI(value);
            String scheme = uri.getScheme();
            return schemes.isEmpty() || scheme != null && schemes.contains(scheme.toLowerCase());
        } catch (URISyntaxException e) {}
        return false;
    }
    
}
