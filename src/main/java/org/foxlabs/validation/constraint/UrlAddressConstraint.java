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
import java.util.LinkedHashSet;
import java.util.Arrays;
import java.util.Collections;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import java.net.URL;

import java.net.MalformedURLException;

import org.foxlabs.validation.ValidationContext;

import static org.foxlabs.common.Predicates.*;

/**
 * This class provides <code>CheckConstraint</code> implementation that checks
 * whether a string is valid URL reference. Also the allowed set of protocols
 * and prefix pattern can be configured.
 *
 * Note that prefix should not describe URL protocol.
 * For example, prefix of the SCM URL <code>scm:svn:http://svn.foxlabs.org</code>
 * should be defined as <code>scm:svn:</code> or <code>scm:[a-z]:</code> regex.
 *
 * @author Fox Mulder
 * @see UrlAddress
 * @see ConstraintFactory#urlAddress()
 * @see ConstraintFactory#urlAddress(String, String...)
 */
public final class UrlAddressConstraint extends CheckConstraint<String> {

    /**
     * <code>UrlAddressConstraint</code> default instance initialized with
     * no prefix and empty set of protocols.
     */
    public static final UrlAddressConstraint DEFAULT = new UrlAddressConstraint(null, (String[]) null);

    /**
     * URL prefix pattern if any.
     */
    private final Pattern prefix;

    /**
     * Set of allowed protocols (empty set means all protocols are allowed).
     */
    private final Set<String> protocols;

    /**
     * Constructs a new <code>UrlAddressConstraint</code> with the specified
     * prefix pattern and array of allowed protocols.
     *
     * @param prefix URL prefix pattern if any.
     * @param protocols Array of allowed protocols.
     * @throws IllegalArgumentException if the specified prefix is invalid
     *         regular expression or array of protocols contains
     *         <code>null</code> or empty elements.
     */
    UrlAddressConstraint(String prefix, String[] protocols) {
        this.prefix = prefix == null || prefix.isEmpty() ? null : Pattern.compile("(" + prefix + ").+");
        this.protocols = protocols == null || protocols.length == 0
            ? Collections.<String>emptySet()
            : Collections.unmodifiableSet(new LinkedHashSet<>(Arrays.asList(
                requireElementsNonNull(
                    require(protocols, OBJECT_ARRAY_NON_EMPTY_OR_NULL, "protocols"),
                    defer((index) -> "protocols[" + index + "] = null")))));
    }

    /**
     * Constructs a new <code>UrlAddressConstraint</code> from the specified
     * annotation.
     *
     * @param annotation Constraint annotation.
     * @throws IllegalArgumentException if the specified annotation defines
     *         invalid prefix regular expression or array of protocols that
     *         contains empty elements.
     */
    UrlAddressConstraint(UrlAddress annotation) {
        this(annotation.prefix(), annotation.protocols());
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
     * Returns prefix pattern if any.
     *
     * @return Prefix pattern if any.
     */
    public Pattern getPrefix() {
        return prefix;
    }

    /**
     * Returns set of allowed protocols (empty set means all protocols are
     * allowed).
     *
     * @return Set of allowed protocols.
     */
    public Set<String> getProtocols() {
        return protocols;
    }

    /**
     * Appends <code>protocols</code> argument that contains set of allowed
     * protocols.
     *
     * @param context Validation context.
     * @param arguments Arguments to be substituted into the error message
     *        template.
     * @return <code>true</code>.
     */
    @Override
    public boolean appendMessageArguments(ValidationContext<?> context, Map<String, Object> arguments) {
        super.appendMessageArguments(context, arguments);
        if (prefix != null)
            arguments.put("prefix", prefix.pattern());
        if (protocols.size() > 0)
            arguments.put("protocols", protocols);
        return true;
    }

    /**
     * Checks whether the specified string is valid URL reference with allowed
     * prefix and protocol.
     *
     * @param value URL string.
     * @param context Validation context.
     * @return <code>true</code> if the specified string is valid URL reference
     *         with allowed protocol and prefix; <code>false</code> otherwise.
     */
    @Override
    protected <T> boolean check(String value, ValidationContext<T> context) {
        if (value == null)
            return true;
        try {
            if (prefix != null) {
                Matcher m = prefix.matcher(value);
                if (m.matches()) {
                    value = value.substring(m.end(1));
                } else {
                    return false;
                }
            }
            URL url = new URL(value);
            return protocols.isEmpty() || protocols.contains(url.getProtocol().toLowerCase());
        } catch (MalformedURLException e) {}
        return false;
    }

}
