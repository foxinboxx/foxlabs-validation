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

package org.foxlabs.validation.message;

import java.util.Locale;
import java.util.MissingResourceException;

import org.foxlabs.validation.resource.ResourceManager;

import org.foxlabs.util.resource.MessageBundle;

/**
 * This class uses resource bundle to resolve messages.
 * 
 * @author Fox Mulder
 */
public class ResourceMessageResolver implements MessageResolver {
    
    /**
     * Message bundle.
     */
    private final MessageBundle bundle;
    
    /**
     * Constructs a new <code>ResourceMessageResolver</code> with the specified
     * message bundle name.
     * 
     * @param bundleName Message bundle name.
     */
    public ResourceMessageResolver(String bundleName) {
        this(MessageBundle.getInstance(bundleName, ResourceManager.VALIDATION_BUNDLE));
    }
    
    /**
     * Constructs a new <code>ResourceMessageResolver</code> with the specified
     * message bundle.
     * 
     * @param bundle Message bundle.
     */
    public ResourceMessageResolver(MessageBundle bundle) {
        this.bundle = bundle;
    }
    
    /**
     * Returns message for the specified key and locale.
     * 
     * @param key Message key.
     * @param locale Message locale.
     * @return Message for the specified key and locale.
     * @throws MissingResourceException if message for the specified key is
     *         not found in provided message bundle.
     */
    @Override
    public String resolveMessage(String key, Locale locale) {
        return bundle.get(key, locale);
    }
    
}
