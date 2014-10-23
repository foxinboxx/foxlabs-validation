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

package org.foxlabs.validation;

/**
 * Thrown to indicate that validation of a single element fails.
 * 
 * @author Fox Mulder
 * @see ValidationException
 */
public class ViolationException extends IllegalArgumentException {
    private static final long serialVersionUID = 2349519563689987258L;
    
    /**
     * Validation component that is source of this exception.
     */
    private Validation<?> component;
    
    /**
     * Type of the entity.
     */
    private Class<?> entityType;
    
    /**
     * Type of the element defined on the entity.
     */
    private Class<?> elementType;
    
    /**
     * Name of the element defined on the entity.
     */
    private String elementName;
    
    /**
     * Root entity being validated.
     */
    private Object rootEntity;
    
    /**
     * Leaf entity being validated.
     */
    private Object leafEntity;
    
    /**
     * Type of the invalid value.
     */
    private Class<?> invalidType;
    
    /**
     * Validation target of the invalid value.
     */
    private ValidationTarget invalidTarget;
    
    /**
     * Index of the invalid value.
     */
    private Object invalidIndex;
    
    /**
     * Invalid value.
     */
    private Object invalidValue;
    
    /**
     * Constructs a new <code>ViolationException</code> with the specified
     * validation component, context and invalid value.
     * 
     * @param component Validation component that is source of this exception.
     * @param context Validation context.
     * @param value Invalid value.
     */
    public ViolationException(Validation<?> component, ValidationContext<?> context,
            Object value) {
        this(component, context, value, null);
    }
    
    /**
     * Constructs a new <code>ViolationException</code> with the specified
     * validation component, context, invalid value and cause.
     * 
     * @param component Validation component that is source of this exception.
     * @param context Validation context.
     * @param value Invalid value.
     * @param cause Cause to be wrapped.
     */
    public ViolationException(Validation<?> component, ValidationContext<?> context,
            Object value, Throwable cause) {
        super(context.buildMessage(component), cause);
        this.component = component;
        this.entityType = context.getEntityType();
        this.elementType = context.getElementType();
        this.elementName = context.getElementName();
        this.rootEntity = context.getRootEntity();
        this.leafEntity = context.getCurrentEntity();
        this.invalidTarget = context.getCurrentTarget();
        this.invalidIndex = context.getCurrentIndex();
        this.invalidType = component.getType();
        this.invalidValue = value;
    }
    
    /**
     * Returns validation component that is source of this exception.
     * 
     * @return Validation component that is source of this exception.
     */
    public Validation<?> getComponent() {
        return component;
    }
    
    /**
     * Returns type of the entity.
     * 
     * @return Type of the entity.
     */
    public Class<?> getEntityType() {
        return entityType;
    }
    
    /**
     * Returns type of the element defined on the entity.
     * 
     * @return Type of the element defined on the entity.
     */
    public Class<?> getElementType() {
        return elementType;
    }
    
    /**
     * Returns name of the element defined on the entity.
     * 
     * @return Name of the element defined on the entity.
     */
    public String getElementName() {
        return elementName;
    }
    
    /**
     * Returns root entity being validated.
     * 
     * @return Root entity being validated.
     */
    public Object getRootEntity() {
        return rootEntity;
    }
    
    /**
     * Returns leaf entity being validated.
     * 
     * @return Leaf entity being validated.
     */
    public Object getLeafEntity() {
        return leafEntity;
    }
    
    /**
     * Returns type of the invalid value.
     * 
     * @return Type of the invalid value.
     */
    public Class<?> getInvalidType() {
        return invalidType;
    }
    
    /**
     * Returns validation target of the invalid value.
     * 
     * @return Validation target of the invalid value.
     */
    public ValidationTarget getInvalidTarget() {
        return invalidTarget;
    }
    
    /**
     * Returns index of the invalid value.
     * 
     * @return Index of the invalid value.
     */
    public Object getInvalidIndex() {
        return invalidIndex;
    }
    
    /**
     * Returns invalid value.
     * 
     * @return Invalid value.
     */
    public Object getInvalidValue() {
        return invalidValue;
    }
    
}
