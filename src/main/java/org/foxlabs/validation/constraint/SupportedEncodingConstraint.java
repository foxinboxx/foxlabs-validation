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

import java.nio.charset.Charset;

/**
 * This class provides <code>EnumerationConstraint</code> implementation that
 * checks whether a string is in list of available charset names supported by
 * JRE.
 * 
 * @author Fox Mulder
 * @see SupportedEncoding
 * @see ConstraintFactory#supportedEncoding()
 */
public final class SupportedEncodingConstraint extends IgnoreCaseEnumerationConstraint {
    
    /**
     * <code>SupportedEncodingConstraint</code> single instance.
     */
    public static final SupportedEncodingConstraint DEFAULT = new SupportedEncodingConstraint();
    
    /**
     * Constructs default <code>SupportedEncodingConstraint</code>.
     */
    private SupportedEncodingConstraint() {
        super(Charset.availableCharsets().keySet());
    }
    
}
