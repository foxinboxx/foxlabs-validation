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

import org.foxlabs.validation.ValidationContext;

/**
 * This class provides <code>CheckConstraint</code> implementation that checks
 * existence of a file or directory.
 * 
 * @author Fox Mulder
 * @see FileExists
 * @see ConstraintFactory#fileExists()
 * @see ConstraintFactory#directoryExists()
 */
public final class FileExistsConstraint extends CheckConstraint<File> {
    
    /**
     * <code>FileExistsConstraint</code> single instance that checks existence
     * of a file.
     */
    public static final FileExistsConstraint FILE = new FileExistsConstraint(false);
    
    /**
     * <code>FileExistsConstraint</code> single instance that checks existence
     * of a directory.
     */
    public static final FileExistsConstraint DIRECTORY = new FileExistsConstraint(true);
    
    /**
     * Determines whether file or directory existence should be checked.
     */
    private final boolean directory;
    
    /**
     * Constructs a new <code>FileExistsConstraint</code>.
     * 
     * @param directory Determines whether file or directory existence should
     *        be checked.
     */
    private FileExistsConstraint(boolean directory) {
        this.directory = directory;
    }
    
    /**
     * Constructs a new <code>FileExistsConstraint</code> from the specified
     * annotation.
     * 
     * @param annotation Constraint annotation.
     */
    FileExistsConstraint(FileExists annotation) {
        this(annotation.directory());
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
     * Determines whether file or directory existence should be checked.
     * 
     * @return <code>true</code> if directory existence should be checked;
     *         <code>false</code> otherwise.
     */
    public boolean isDirectory() {
        return directory;
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
                (directory ? ".directory" : ".file"));
    }
    
    /**
     * Checks existence of the specified file or directory.
     * 
     * <p>Note that if this constraint is configured to check files then
     * checking of existing directory will fail and vise versa.</p>
     * 
     * @param file File or directory which existence to be checked.
     * @param context Validation context.
     * @return <code>true</code> if the specified file or directory exists;
     *         <code>false</code> otherwise.
     */
    @Override
    protected <T> boolean check(File file, ValidationContext<T> context) {
        return file == null || (directory ? file.isDirectory() : file.isFile());
    }
    
}
