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

import javax.imageio.ImageIO;

/**
 * This class provides <code>EnumerationConstraint</code> implementation that
 * checks whether a string is in list of available image MIME types supported
 * by <code>javax.imageio.ImageIO</code>.
 * 
 * @author Fox Mulder
 * @see SupportedImageMime
 * @see ConstraintFactory#supportedImageMime()
 */
public final class SupportedImageMimeConstraint extends IgnoreCaseEnumerationConstraint {
    
    /**
     * <code>SupportedImageMimeConstraint</code> single instance.
     */
    public static final SupportedImageMimeConstraint DEFAULT = new SupportedImageMimeConstraint();
    
    /**
     * Constructs default <code>SupportedImageMimeConstraint</code>.
     */
    private SupportedImageMimeConstraint() {
        super(ImageIO.getReaderMIMETypes());
    }
    
}
