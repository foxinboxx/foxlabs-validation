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

package org.foxlabs.validation.converter;

import java.io.File;

import org.foxlabs.validation.ValidationContext;

/**
 * This class provides <code>Converter</code> implementation for the
 * <code>java.io.File</code> type.
 * 
 * @author Fox Mulder
 * @see ParentDirectory
 * @see ConverterFactory#forFile(String)
 */
public final class FileConverter extends AbstractConverter<File> {
    
    /**
     * <code>FileConverter</code> default instance.
     */
    public static final FileConverter DEFAULT = new FileConverter((String) null);
    
    /**
     * Parent directory pathname.
     */
    private final String directory;
    
    /**
     * Constructs a new <code>FileConverter</code> with the specified parent
     * directory pathname.
     * 
     * @param directory Parent directory pathname.
     */
    FileConverter(String directory) {
        this.directory = directory;
    }
    
    /**
     * Constructs a new <code>FileConverter</code> from the specified
     * annotation.
     * 
     * @param annotation Converter annotation.
     */
    FileConverter(ParentDirectory annotation) {
        this(annotation.path());
    }
    
    /**
     * Returns <code>java.io.File</code> type.
     * 
     * @return <code>java.io.File</code> type.
     */
    @Override
    public Class<File> getType() {
        return File.class;
    }
    
    /**
     * <code>FileConverter</code> has no error message template.
     * 
     * @param context Validation context.
     * @return <code>null</code>.
     */
    @Override
    public String getMessageTemplate(ValidationContext<?> context) {
        return null;
    }
    
    /**
     * Returns <code>java.io.File</code> object for the specified path.
     * 
     * <p>This method simply creates a <code>java.io.File</code> object and is
     * not responsible for verifying the existence of a file.</p>
     * 
     * @param value File pathname.
     * @param context Validation context.
     * @return <code>java.io.File</code> object for the specified path.
     */
    @Override
    protected <T> File doDecode(String value, ValidationContext<T> context) {
        return new File(directory, value);
    }
    
    /**
     * Returns pathname for the specified <code>java.io.File</code> object.
     * 
     * @param value <code>java.io.File</code> object to be encoded.
     * @param context Validation context.
     * @return Pathname for the specified <code>java.io.File</code> object.
     */
    @Override
    protected <T> String doEncode(File value, ValidationContext<T> context) {
        String path = value.getPath();
        if (directory != null) {
            File file = value;
            File parent = new File(directory);
            while (!(file == null || parent.equals(file)))
                file = file.getParentFile();
            if (file != null)
                path = file.equals(value) ? "" : path.substring(parent.getPath().length() + 1);
        }
        return path;
    }
    
}
