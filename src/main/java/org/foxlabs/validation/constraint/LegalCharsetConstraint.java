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

import org.foxlabs.validation.ValidationContext;

import org.foxlabs.util.Assert;
import org.foxlabs.util.UnicodeSet;

/**
 * This class provides <code>CheckConstraint</code> implementation that checks
 * whether a string contains allowed characters only.
 * 
 * @author Fox Mulder
 * @see LegalCharset
 * @see ConstraintFactory#legalCharset(String)
 * @see ConstraintFactory#legalCharset(UnicodeSet)
 */
public class LegalCharsetConstraint extends CheckConstraint<String> {
    
    /**
     * Allowed character set.
     */
    protected final UnicodeSet charset;
    
    /**
     * Constructs a new <code>LegalCharsetConstraint</code> with the specified
     * allowed character set.
     * 
     * @param charset Allowed character set.
     * @throws IllegalArgumentException if the specified character set is
     *         <code>null</code> or empty.
     */
    protected LegalCharsetConstraint(UnicodeSet charset) {
        Assert.assertTrue(!(charset == null || charset.equals(UnicodeSet.EMPTY)), "charset");
        this.charset = charset;
    }
    
    /**
     * Constructs a new <code>LegalCharsetConstraint</code> from the specified
     * annotation.
     * 
     * @param annotation Constraint annotation.
     * @throws IllegalArgumentException if the specified annotation defines
     *         empty character set.
     */
    LegalCharsetConstraint(LegalCharset annotation) {
        this(UnicodeSet.fromPattern(annotation.value()));
    }
    
    /**
     * Returns <code>java.lang.String</code> type.
     * 
     * @return <code>java.lang.String</code> type.
     */
    @Override
    public final Class<?> getType() {
        return String.class;
    }
    
    /**
     * Returns allowed character set.
     * 
     * @return Allowed character set.
     */
    public final UnicodeSet getCharset() {
        return charset;
    }
    
    /**
     * Appends <code>charset</code> argument that contains allowed character
     * set.
     * 
     * @param context Validation context.
     * @param arguments Arguments to be substituted into the error message
     *        template.
     * @return <code>true</code>.
     */
    @Override
    public boolean appendMessageArguments(ValidationContext<?> context, Map<String, Object> arguments) {
        super.appendMessageArguments(context, arguments);
        arguments.put("charset", charset.toString());
        return true;
    }
    
    /**
     * Checks whether the specified string contains allowed characters only.
     * 
     * @param value String to be checked.
     * @param context Validation context.
     * @return <code>true</code> if the specified string contains allowed
     *         characters only; <code>false</code> otherwise.
     */
    @Override
    protected <T> boolean check(String value, ValidationContext<T> context) {
        if (value == null)
            return true;
        int length = value.length();
        for (int i = 0; i < length; i++)
            if (!charset.contains(value.charAt(i)))
                return false;
        return true;
    }
    
}
