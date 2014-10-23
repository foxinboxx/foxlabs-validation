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

package org.foxlabs.validation.metadata;

/**
 * Throw to indicate that a bean class has illegal validation definition.
 * 
 * @author Fox Mulder
 * @see BeanMetaData
 */
public class BeanDefinitionException extends IllegalStateException {
    private static final long serialVersionUID = 3510580748100569813L;
    
    /**
     * Bean type.
     */
    private Class<?> beanType;
    
    /**
     * Property name if any.
     */
    private String propertyName;
    
    /**
     * Constructs a new <code>BeanDefinitionException</code> with the specified
     * bean type and cause.
     * 
     * @param type Bean type.
     * @param cause Failure of the bean definition.
     */
    public BeanDefinitionException(Class<?> type, Throwable cause) {
        super("Bean " + type.getName() + " specification error: " +
                cause.getMessage());
        this.beanType = type;
    }
    
    /**
     * Constructs a new <code>BeanDefinitionException</code> with the specified
     * bean type, property name and cause.
     * 
     * @param type Bean type.
     * @param property Property name.
     * @param cause Failure of the bean property definition.
     */
    public BeanDefinitionException(Class<?> type, String property, Throwable cause) {
        super("Bean " + type.getName() + " property \"" + property +
                "\" specification error: " + cause.getMessage());
        this.beanType = type;
        this.propertyName = property;
    }
    
    /**
     * Returns bean type.
     * 
     * @return Bean type.
     */
    public Class<?> getBeanType() {
        return beanType;
    }
    
    /**
     * Returns property name if any.
     * 
     * @return Property name if any.
     */
    public String getPropertyName() {
        return propertyName;
    }
    
}
