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

import java.util.regex.Pattern;

/**
 * This class provides <code>RegexConstraint</code> implementation that checks
 * whether a string is valid MIME type representation.
 * 
 * @author Fox Mulder
 * @see MimeType
 * @see ConstraintFactory#mimetype()
 */
public final class MimeTypeConstraint extends RegexConstraint {
    
    /**
     * <code>MimeTypeConstraint</code> single instance.
     */
    public static final MimeTypeConstraint DEFAULT = new MimeTypeConstraint();
    
    /**
     * Constructs default <code>MimeTypeConstraint</code>.
     */
    private MimeTypeConstraint() {
        super("^[a-z]([a-z0-9_\\-\\.]*[a-z0-9_])*/[a-z]([a-z0-9_\\-\\.]*[a-z0-9_])*$",
              Pattern.CASE_INSENSITIVE);
    }
    
}
