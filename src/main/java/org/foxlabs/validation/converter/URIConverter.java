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

import java.net.URI;
import java.net.URISyntaxException;

import org.foxlabs.validation.ValidationContext;

/**
 * This class provides <code>Converter</code> implementation for the
 * <code>java.net.URI</code> type.
 * 
 * @author Fox Mulder
 */
public final class URIConverter extends AbstractConverter<URI> {
    
    /**
     * <code>URIConverter</code> single instance.
     */
    public static final URIConverter DEFAULT = new URIConverter();
    
    /**
     * Constructs default <code>URIConverter</code>.
     */
    private URIConverter() {}
    
    /**
     * Returns <code>java.net.URI</code> type.
     * 
     * @return <code>java.net.URI</code> type.
     */
    @Override
    public Class<URI> getType() {
        return URI.class;
    }
    
    /**
     * Converts URI string into <code>java.net.URI</code> object.
     * 
     * @param value URI string.
     * @param context Validation context.
     * @return Decoded <code>java.net.URI</code> object.
     * @throws MalformedValueException if the specified string could not be
     *         parsed as an URI reference.
     */
    @Override
    protected <T> URI doDecode(String value, ValidationContext<T> context) {
        try {
            return new URI(value);
        } catch (URISyntaxException e) {
            throw new MalformedValueException(this, context, value);
        }
    }
    
}
