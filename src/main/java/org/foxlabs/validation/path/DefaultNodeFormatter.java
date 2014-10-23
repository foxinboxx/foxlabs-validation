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

package org.foxlabs.validation.path;

import org.foxlabs.validation.ValidationTarget;
import org.foxlabs.validation.ViolationException;

import org.foxlabs.util.UnicodeSet;

/**
 * This class provides default implementation of the <code>NodeFormatter</code>
 * interface.
 * 
 * @author Fox Mulder
 */
public class DefaultNodeFormatter implements NodeFormatter {
    
    /**
     * Path separator.
     */
    protected final String separator;
    
    /**
     * Open [0] and close [1] brackets for element indexes.
     */
    protected final String[] ebrackets;
    
    /**
     * Open [0] and close [1] brackets for key indexes.
     */
    protected final String[] kbrackets;
    
    /**
     * Constructs a new <code>DefaultNodeFormatter</code> with the specified
     * path separator.
     * 
     * @param separator Path separator.
     */
    public DefaultNodeFormatter(String separator) {
        this(separator, new String[]{"[", "]"}, new String[]{"{", "}"});
    }
    
    /**
     * Constructs a new <code>DefaultNodeFormatter</code> with the specified
     * path separator and index brackets.
     * 
     * @param separator Path separator.
     * @param ebrackets Open [0] and close [1] brackets for element indexes.
     * @param kbrackets Open [0] and close [1] brackets for key indexes.
     */
    public DefaultNodeFormatter(String separator, String[] ebrackets, String[] kbrackets) {
        this.separator = separator;
        this.ebrackets = ebrackets;
        this.kbrackets = kbrackets;
    }
    
    /**
     * Appends path separator to the specified path buffer.
     * 
     * @param buf Path buffer to append.
     */
    @Override
    public void appendSeparator(StringBuilder buf) {
        buf.append(separator);
    }
    
    /**
     * Appends the specified violation node to the specified path buffer.
     * 
     * @param node Node to be appended.
     * @param buf Path buffer to append.
     */
    @Override
    public void appendNode(ViolationException node, StringBuilder buf) {
        buf.append(node.getElementName());
        ValidationTarget target = node.getInvalidTarget();
        if (target != ValidationTarget.VALUE) {
            String[] brackets = target == ValidationTarget.ELEMENTS ? ebrackets : kbrackets;
            buf.append(brackets[0]);
            appendIndex(node, buf);
            buf.append(brackets[1]);
        }
    }
    
    /**
     * Appends index value of the specified violation node to the specified
     * path buffer.
     * 
     * @param node Node which index to be appended.
     * @param buf Path buffer to append.
     */
    protected void appendIndex(ViolationException node, StringBuilder buf) {
        Object index = node.getInvalidIndex();
        if (index == null || index instanceof Number || index instanceof Boolean) {
            buf.append(index);
        } else if (index instanceof String || index instanceof Character) {
            buf.append('\"');
            UnicodeSet.escape(index.toString(), buf);
            buf.append('\"');
        } else {
            buf.append('@').append(Integer.toHexString(index.hashCode()));
        }
    }
    
}
