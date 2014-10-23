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

package org.foxlabs.validation;

import java.io.PrintStream;

import java.util.List;
import java.util.Collections;

import org.foxlabs.validation.path.NodeFormatter;
import org.foxlabs.validation.path.PathIterator;

/**
 * Thrown to indicate that validation of a multiple elements fails.
 * 
 * @author Fox Mulder
 * @see ViolationException
 * @see PathIterator
 */
public class ValidationException extends RuntimeException implements Iterable<ViolationException> {
    private static final long serialVersionUID = 2796596365660835342L;
    
    /**
     * List of violations.
     */
    private List<ViolationException> violations;
    
    /**
     * Determines if it is result of cascade validation.
     */
    private boolean cascade;
    
    /**
     * Constructs a new <code>ValidationException</code> with the specified
     * single violation.
     * 
     * @param violation Single violation.
     */
    public ValidationException(ViolationException violation) {
        this(Collections.singletonList(violation));
    }
    
    /**
     * Constructs a new <code>ValidationException</code> with the specified
     * list of violations.
     * 
     * @param violations List of violations.
     */
    public ValidationException(List<? extends ViolationException> violations) {
        this(violations, false);
    }
    
    /**
     * Constructs a new <code>ValidationException</code> with the specified
     * list of violations and cascade flag.
     * 
     * @param violations List of violations.
     * @param cascade Determines if it is result of cascade validation.
     */
    ValidationException(List<? extends ViolationException> violations, boolean cascade) {
        this.violations = Collections.unmodifiableList(violations);
        this.cascade = cascade;
    }
    
    /**
     * Returns unmodifiable list of root violations.
     * 
     * @return Unmodifiable list of root violations.
     */
    public List<ViolationException> getRootViolations() {
        return violations;
    }
    
    /**
     * Returns first violation in the list.
     * 
     * @return First violation in the list.
     */
    public ViolationException getFirstViolation() {
        return violations.isEmpty() ? null : violations.iterator().next();
    }
    
    /**
     * Determines if it is result of cascade validation.
     * 
     * @return <code>true</code> if it is result of cascade validation;
     *         <code>false</code> otherwise.
     */
    public boolean isCascade() {
        return cascade;
    }
    
    /**
     * Returns iterator over all violations hierarchy.
     * 
     * @return Iterator over all violations hierarchy.
     */
    @Override
    public PathIterator iterator() {
        return iterator(NodeFormatter.DEFAULT);
    }
    
    /**
     * Returns iterator over all violations hierarchy.
     * 
     * @param formatter Formatter to be used for formatting violation nodes on
     *        the path.
     * @return Iterator over all violations hierarchy.
     */
    public PathIterator iterator(NodeFormatter formatter) {
        return new PathIterator(this, formatter);
    }
    
    /**
     * Prints violations to the standard error stream.
     */
    public void printViolations() {
        printViolations(System.err);
    }
    
    /**
     * Prints violations to the specified print stream.
     * 
     * @param stream Stream to use for output.
     */
    public void printViolations(PrintStream stream) {
        synchronized (stream) {
            PathIterator itr = iterator();
            while (itr.hasNext()) {
                ViolationException violation = itr.next();
                String path = itr.path();
                if (path.length() > 0) {
                    stream.print(path);
                    stream.print(": ");
                }
                stream.print(violation.getMessage());
                stream.println();
            }
        }
    }
    
}
