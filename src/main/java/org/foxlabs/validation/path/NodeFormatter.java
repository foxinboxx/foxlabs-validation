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

import org.foxlabs.validation.ViolationException;

/**
 * Defines interface that allows to format violation nodes on the path.
 * 
 * @author Fox Mulder
 * @see PathIterator
 */
public interface NodeFormatter {
    
    /**
     * <code>NodeFormatter</code> default instance.
     */
    NodeFormatter DEFAULT = new DefaultNodeFormatter(".");
    
    /**
     * Appends path separator to the specified path buffer.
     * 
     * @param buf Path buffer to append.
     */
    void appendSeparator(StringBuilder buf);
    
    /**
     * Appends the specified violation node to the specified path buffer.
     * 
     * @param node Node to be appended.
     * @param buf Path buffer to append.
     */
    void appendNode(ViolationException node, StringBuilder buf);
    
}
