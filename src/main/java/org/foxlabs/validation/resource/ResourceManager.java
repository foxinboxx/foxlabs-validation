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

package org.foxlabs.validation.resource;

import java.util.Locale;
import java.util.TimeZone;
import java.util.MissingResourceException;

import org.foxlabs.util.resource.MessageBundle;
import org.foxlabs.util.resource.ResourceHelper;

/**
 * Resource manager class encapsulates all required resources. 
 * 
 * @author Fox Mulder
 */
public abstract class ResourceManager {
    
    /**
     * Directory on the classpath of the framework resources.
     */
    public static final String RESOURCE_DIRECTORY =
        ResourceHelper.getResourcePath(ResourceManager.class);
    
    /**
     * Message bundle for default validation messages.
     */
    public static final MessageBundle VALIDATION_BUNDLE =
        MessageBundle.getInstance(RESOURCE_DIRECTORY + "/validation-messages");
    
    /**
     * Message bundle for time zone names.
     */
    public static final MessageBundle TIME_ZONE_BUNDLE =
        MessageBundle.getInstance(RESOURCE_DIRECTORY + "/time-zones");
    
    // Can't be inherited.
    private ResourceManager() {}
    
    /**
     * Returns localized name for the specified time zone.
     * 
     * @param zone Time zone which name to be returned.
     * @param locale Desired locale.
     * @return Localized name for the specified time zone.
     */
    public static String getTimeZoneName(TimeZone zone, Locale locale) {
        try {
            return TIME_ZONE_BUNDLE.get(zone.getID(), locale);
        } catch (MissingResourceException e) {
            return zone.getDisplayName(locale);
        }
    }
    
}
