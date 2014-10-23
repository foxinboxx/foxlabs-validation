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

import org.foxlabs.validation.ValidationContext;

/**
 * This class provides <code>Converter</code> implementation for the
 * <code>java.lang.Class</code> type.
 * 
 * @author Fox Mulder
 * @see ConverterFactory#forClass(ClassLoader)
 */
public final class ClassConverter extends AbstractConverter<Class<?>> {
    
    /**
     * <code>ClassConverter</code> default instance.
     */
    public static final ClassConverter DEFAULT = new ClassConverter(null);
    
    /**
     * Class loader to be used for class loading.
     */
    private final ClassLoader loader;
    
    /**
     * Constructs a new <code>ClassConverter</code> with the specified
     * class loader.
     * 
     * @param loader Class loader to be used for class loading.
     */
    ClassConverter(ClassLoader loader) {
        if (loader == null) {
            loader = Thread.currentThread().getContextClassLoader();
            if (loader == null) {
                loader = ClassConverter.class.getClassLoader();
                if (loader == null)
                    loader = ClassLoader.getSystemClassLoader();
            }
        }
        this.loader = loader;
    }
    
    /**
     * Returns <code>java.lang.Class</code> type.
     * 
     * @return <code>java.lang.Class</code> type.
     */
    @Override
    @SuppressWarnings("unchecked")
    public Class<Class<?>> getType() {
        return (Class<Class<?>>) Class.class.getClass();
    }
    
    /**
     * Returns <code>java.lang.Class</code> object for the specified class name.
     * 
     * @param value Class name.
     * @param context Validation context.
     * @return <code>java.lang.Class</code> object.
     * @throws MalformedValueException if class with the specified name
     *         is not found on classpath.
     */
    @Override
    protected <T> Class<?> doDecode(String value, ValidationContext<T> context) {
        try {
            return loader.loadClass(value);
        } catch (ClassNotFoundException e) {
            throw new MalformedValueException(this, context, value);
        }
    }
    
    /**
     * Returns class name for the specified <code>java.lang.Class</code> object.
     * 
     * @param value <code>java.lang.Class</code> object.
     * @param context Validation context.
     * @return Class name.
     */
    @Override
    protected <T> String doEncode(Class<?> value, ValidationContext<T> context) {
        return value.getName();
    }
    
}
