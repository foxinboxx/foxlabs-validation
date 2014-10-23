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

import java.util.Iterator;
import java.util.LinkedList;
import java.util.NoSuchElementException;

import org.foxlabs.validation.ValidationException;
import org.foxlabs.validation.ViolationException;

/**
 * This iterator used by <code>ValidationException</code> to iterate over all
 * violations hierarchy and allows to obtain path of the current violation.
 * 
 * @author Fox Mulder
 * @see NodeFormatter
 * @see ValidationException#iterator()
 * @see ValidationException#iterator(NodeFormatter)
 */
public final class PathIterator implements Iterator<ViolationException> {
    
    /**
     * Next violation.
     */
    private ViolationException next;
    
    /**
     * Last violation.
     */
    private ViolationException last;
    
    /**
     * Current iterator.
     */
    private Iterator<ViolationException> iterator;
    
    /**
     * List of parent iterators.
     */
    private final LinkedList<ItrNode> iterators;
    
    /**
     * Violations path.
     */
    private final LinkedList<ViolationException> path;
    
    /**
     * Formatter to be used for formatting violation nodes on the path.
     */
    private final NodeFormatter formatter;
    
    /**
     * Constructs a new <code>PathIterator</code> with the specified violations
     * hierarhy and path separator.
     * 
     * @param violations Violations hierarchy.
     * @param formatter Formatter to be used for formatting violation nodes on
     *        the path.
     */
    public PathIterator(ValidationException violations, NodeFormatter formatter) {
        this.iterator = violations.getRootViolations().iterator();
        this.iterators = new LinkedList<ItrNode>();
        this.path = new LinkedList<ViolationException>();
        this.formatter = formatter;
    }
    
    /**
     * Determines if the iteration has more elements.
     * 
     * @return <code>true</code> if the iteration has more elements;
     *         <code>false</code> otherwise.
     */
    @Override
    public boolean hasNext() {
        if (next == null) {
            if (iterator.hasNext()) {
                next = iterator.next();
            } else {
                if (iterators.isEmpty()) 
                    return false;
                ItrNode node = iterators.removeLast();
                iterator = node.iterator;
                if (node.cascade)
                    path.removeLast();
                return hasNext();
            }
            Throwable cause = next.getCause();
            while (!(cause == null || cause instanceof ValidationException))
                cause = cause.getCause();
            if (cause instanceof ValidationException) {
                boolean cascade = ((ValidationException) cause).isCascade();
                iterators.addLast(new ItrNode(iterator, cascade));
                if (cascade)
                    path.addLast(next);
                iterator = ((ValidationException) cause).getRootViolations().iterator();
                next = null;
                return hasNext();
            }
        }
        return true;
    }
    
    /**
     * Returns next violation in the hierarhy.
     * 
     * @return Next violation in the hierarhy.
     */
    @Override
    public ViolationException next() {
        if (hasNext()) {
            last = next;
            next = null;
            return last;
        }
        throw new NoSuchElementException();
    }
    
    /**
     * Returns array of violation nodes of the current path.
     * 
     * @return Array of violation nodes of the current path.
     */
    public ViolationException[] nodes() {
        if (last == null)
            throw new NoSuchElementException();
        ViolationException[] nodes = new ViolationException[path.size() + 1];
        path.toArray(nodes);
        nodes[nodes.length - 1] = last;
        return nodes;
    }
    
    /**
     * Returns path of the current violation.
     * 
     * @return Path of the current violation.
     */
    public String path() {
        StringBuilder buf = new StringBuilder();
        ViolationException[] nodes = nodes();
        if (nodes[0].getElementType() != null)
            formatter.appendNode(nodes[0], buf);
        for (int i = 1; i < nodes.length; i++) {
            if (nodes[i].getElementType() != null) {
                formatter.appendSeparator(buf);
                formatter.appendNode(nodes[i], buf);
            }
        }
        return buf.toString();
    }
    
    /**
     * Unsupported operation.
     */
    @Override
    public void remove() {
        throw new UnsupportedOperationException();
    }
    
    // ItrNode
    
    /**
     * Iterator node.
     * 
     * @author Fox Mulder
     */
    private static final class ItrNode {
        
        /**
         * Iterator of violations.
         */
        private final Iterator<ViolationException> iterator;
        
        /**
         * Determines if iterator is over result of cascade validation.
         */
        private final boolean cascade;
        
        /**
         * Constructs a new <code>ItrNode</code> with the specified iterator
         * and cascade flag.
         * 
         * @param iterator Iterator of violations.
         * @param cascade Determines if iterator is over result of cascade
         *        validation.
         */
        private ItrNode(Iterator<ViolationException> iterator, boolean cascade) {
            this.iterator = iterator;
            this.cascade = cascade;
        }
        
    }
    
}
