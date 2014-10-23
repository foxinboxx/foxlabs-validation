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

import java.io.File;

import java.util.Map;

import org.foxlabs.validation.ValidationContext;

import org.foxlabs.util.Assert;

/**
 * This class provides <code>CheckConstraint</code> implementation that checks
 * whether size of a file is within allowed minimum and maximum bounds.
 * 
 * @author Fox Mulder
 * @see FileSize
 * @see ConstraintFactory#fileSize(long, long)
 */
public final class FileSizeConstraint extends CheckConstraint<File> {
    
    /**
     * Minimum allowed file size.
     */
    private final long minSize;
    
    /**
     * Maximum allowed file size.
     */
    private final long maxSize;
    
    /**
     * Constructs a new <code>FileSizeConstraint</code> with the specified
     * minimum and maximum file sizes.
     * 
     * @param minSize Minimum allowed file size.
     * @param maxSize Maximum allowed file size.
     * @throws IllegalArgumentException if the specified minimum or maximum file
     *         size is negative.
     */
    FileSizeConstraint(long minSize, long maxSize) {
        Assert.notNegative(minSize, "minSize");
        Assert.notNegative(maxSize, "maxSize");
        
        if (minSize < maxSize) {
            this.minSize = minSize;
            this.maxSize = maxSize;
        } else {
            this.minSize = maxSize;
            this.maxSize = minSize;
        }
    }
    
    /**
     * Constructs a new <code>FileSizeConstraint</code> from the specified
     * annotation.
     * 
     * @param annotation Constraint annotation.
     * @throws IllegalArgumentException if the specified annotation defines
     *         negative minimum or maximum file size.
     */
    FileSizeConstraint(FileSize annotation) {
        this(annotation.min(), annotation.max());
    }
    
    /**
     * Returns <code>java.io.File</code> type.
     * 
     * @return <code>java.io.File</code> type.
     */
    @Override
    public Class<?> getType() {
        return File.class;
    }
    
    /**
     * Returns minimum allowed file size.
     * 
     * @return Minimum allowed file size.
     */
    public long getMinSize() {
        return minSize;
    }
    
    /**
     * Returns maximum allowed file size.
     * 
     * @return Maximum allowed file size.
     */
    public long getMaxSize() {
        return maxSize;
    }
    
    /**
     * Returns localized error message template.
     * 
     * @param context Validation context.
     * @return Localized error message template.
     */
    @Override
    public String getMessageTemplate(ValidationContext<?> context) {
        return context.resolveMessage(getClass().getName() +
                (minSize > 0L ? maxSize == Long.MAX_VALUE ? ".min" : "" : maxSize < Long.MAX_VALUE ? ".max" : ""));
    }
    
    /**
     * Appends <code>minSize</code> and <code>maxSize</code> arguments that
     * contain minimum and maximum allowed file size respectively.
     * 
     * @param context Validation context.
     * @param arguments Arguments to be substituted into the error message
     *        template.
     * @return <code>true</code>.
     */
    @Override
    public boolean appendMessageArguments(ValidationContext<?> context, Map<String, Object> arguments) {
        super.appendMessageArguments(context, arguments);
        arguments.put("minSize", minSize);
        arguments.put("maxSize", maxSize);
        return true;
    }
    
    /**
     * Checks whether size of the specified file is within allowed minimum and
     * maximum bounds.
     * 
     * @param value File which size to be checked.
     * @param context Validation context.
     * @return <code>true</code> if size of the specified file is within
     *         allowed minimum and maximum bounds; <code>false</code> otherwise.
     */
    @Override
    protected <T> boolean check(File value, ValidationContext<T> context) {
        if (value == null)
            return true;
        long size = value.length();
        return size >= minSize && size <= maxSize;
    }
    
}
