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

/**
 * Defines a chain of <code>MessageResolver</code>s.
 * 
 * @author Fox Mulder
 */
public final class MessageResolverChain implements MessageResolver {
    
    /**
     * Chain of message resolvers.
     */
    private final MessageResolver[] resolverChain;
    
    /**
     * Constructs a new <code>MessageResolverChain</code> with the specified
     * chain of message resolvers.
     * 
     * @param chain Chain of message resolvers.
     */
    public MessageResolverChain(MessageResolver... chain) {
        this.resolverChain = chain;
    }
    
    /**
     * Returns message for the specified key and locale.
     * 
     * @param key Message key.
     * @param locale Message locale.
     * @return Message for the specified key and locale.
     * @throws MissingResourceException if message for the specified key is
     *         not found in all of the provided message resolvers.
     */
    @Override
    public String resolveMessage(String key, Locale locale) {
        for (MessageResolver resolver : resolverChain) {
            try {
                return resolver.resolveMessage(key, locale);
            } catch (MissingResourceException e) {}
        }
        throw new MissingResourceException("Can't find message with key \"" + key + "\"",
                getClass().getName(), key);
    }
    
}
