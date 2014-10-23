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

/**
 * Defines interface that allows to resolve localized messages.
 * 
 * @author Fox Mulder
 */
public interface MessageResolver {
    
    /**
     * <code>MessageResolver</code> default instance.
     */
    MessageResolver DEFAULT = new ResourceMessageResolver(ResourceManager.VALIDATION_BUNDLE);
    
    /**
     * Returns message for the specified key and locale.
     * 
     * @param key Message key.
     * @param locale Message locale.
     * @return Message for the specified key and locale.
     * @throws MissingResourceException if message for the specified key is
     *         missing.
     */
    String resolveMessage(String key, Locale locale);
    
}
