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

import java.net.URL;
import java.net.MalformedURLException;

import org.foxlabs.validation.ValidationContext;

/**
 * This class provides <code>Converter</code> implementation for the
 * <code>java.net.URL</code> type.
 * 
 * @author Fox Mulder
 */
public final class URLConverter extends AbstractConverter<URL> {
    
    /**
     * <code>URLConverter</code> single instance.
     */
    public static final URLConverter DEFAULT = new URLConverter();
    
    /**
     * Constructs default <code>URLConverter</code>.
     */
    private URLConverter() {}
    
    /**
     * Returns <code>java.net.URL</code> type.
     * 
     * @return <code>java.net.URL</code> type.
     */
    @Override
    public Class<URL> getType() {
        return URL.class;
    }
    
    /**
     * Converts URL string into <code>java.net.URL</code> object.
     * 
     * @param value URL string.
     * @param context Validation context.
     * @return Decoded <code>java.net.URL</code> object.
     * @throws MalformedValueException if the specified string could not be
     *         parsed as an URL reference.
     */
    @Override
    protected <T> URL doDecode(String value, ValidationContext<T> context) {
        try {
            return new URL(value);
        } catch (MalformedURLException e) {
            throw new MalformedValueException(this, context, value);
        }
    }
    
}
