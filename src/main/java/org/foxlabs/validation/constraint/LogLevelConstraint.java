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

import java.util.logging.Level;

import static java.util.logging.Level.*;

import org.foxlabs.validation.ValidationContext;

/**
 * This class provides <code>EnumerationConstraint</code> implementation for
 * <code>java.util.logging.Level</code> known constants.
 * 
 * @author Fox Mulder
 * @see ConstraintFactory#logLevel()
 */
public final class LogLevelConstraint extends EnumerationConstraint.Default<Level> {
    
    /**
     * <code>LogLevelConstraint</code> single instance.
     */
    public static final LogLevelConstraint DEFAULT = new LogLevelConstraint();
    
    /**
     * Constructs default <code>LogLevelConstraint</code>.
     */
    private LogLevelConstraint() {
        super(new Level[]{OFF, SEVERE, WARNING, INFO, CONFIG, FINE, FINER, FINEST, ALL});
    }
    
    /**
     * Returns localized error message template.
     * 
     * @param context Validation context.
     * @return Localized error message template.
     */
    @Override
    public String getMessageTemplate(ValidationContext<?> context) {
        return context.resolveMessage(EnumerationConstraint.class.getName());
    }
    
}
