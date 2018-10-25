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

import java.util.List;
import java.util.LinkedList;
import java.util.Map;
import java.util.LinkedHashMap;
import java.util.Locale;

import java.text.NumberFormat;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.DateFormat;
import java.text.SimpleDateFormat;

import org.foxlabs.validation.constraint.Constraint;
import org.foxlabs.validation.constraint.ConstraintViolationException;
import org.foxlabs.validation.converter.Converter;
import org.foxlabs.validation.converter.MalformedValueException;
import org.foxlabs.validation.message.MessageBuilder;
import org.foxlabs.validation.message.MessageResolver;
import org.foxlabs.validation.metadata.ElementMetaData;
import org.foxlabs.validation.metadata.EntityMetaData;
import org.foxlabs.validation.metadata.MetaData;
import org.foxlabs.validation.metadata.PropertyFilter;
import org.foxlabs.validation.metadata.PropertyMetaData;

import org.foxlabs.util.reflect.Types;

/**
 * This class allows to perform validation of entities, properties, etc.
 * 
 * <p><code>Validator</code> instances should be immutable and obtained from
 * {@link ValidatorFactory}.</p>
 * 
 * @author Fox Mulder
 */
public class Validator<T> {
    
    /**
     * Default constraint group.
     */
    public static final String DEFAULT_GROUP = "";
    
    /**
     * Array of constraint groups to be used for validation all the constraints.
     */
    public static final String[] ALL_GROUPS = new String[0];
    
    /**
     * Factory that was used to create this validator.
     */
    final ValidatorFactory factory;
    
    /**
     * Entity metadata.
     */
    final EntityMetaData<T> entityMeta;
    
    /**
     * Message resolver to be used for resolving localized messages.
     */
    final MessageResolver messageResolver;
    
    /**
     * Message builder to be used for building error messages.
     */
    final MessageBuilder messageBuilder;
    
    /**
     * Default pattern for integer numbers.
     */
    final String defaultIntegerPattern;
    
    /**
     * Default pattern for decimal numbers.
     */
    final String defaultDecimalPattern;
    
    /**
     * Default pattern for dates.
     */
    final String defaultDatePattern;
    
    /**
     * Default date style.
     */
    final int defaultDateStyle;
    
    /**
     * Default time style.
     */
    final int defaultTimeStyle;
    
    /**
     * Constructs a new <code>Validator</code> with the specified factory,
     * entity metadata, message resolver and error message builder.
     * 
     * @param factory Factory that was used to create this validator.
     * @param entityMeta Entity metadata.
     * @param messageResolver Message resolver to be used for resolving
     *        localized messages.
     * @param messageBuilder Message builder to be used for building error
     *        messages.
     * @param integerPattern Default pattern for integer numbers.
     * @param decimalPattern Default pattern for decimal numbers.
     * @param datePattern Default pattern for dates.
     * @param dateStyle Default date style.
     * @param timeStyle Default time style.
     */
    protected Validator(ValidatorFactory factory, EntityMetaData<T> entityMeta,
            MessageResolver messageResolver, MessageBuilder messageBuilder,
            String integerPattern, String decimalPattern,
            String datePattern, int dateStyle, int timeStyle) {
        this.factory = factory;
        this.entityMeta = entityMeta;
        this.messageResolver = messageResolver;
        this.messageBuilder = messageBuilder;
        this.defaultIntegerPattern = integerPattern;
        this.defaultDecimalPattern = decimalPattern;
        this.defaultDatePattern = datePattern;
        this.defaultDateStyle = dateStyle;
        this.defaultTimeStyle = timeStyle;
    }
    
    /**
     * Returns factory that was used to create this validator.
     * 
     * @return Factory that was used to create this validator.
     */
    public final ValidatorFactory getFactory() {
        return factory;
    }
    
    /**
     * Returns entity metadata.
     * 
     * @return Entity metadata.
     */
    public final EntityMetaData<T> getMetaData() {
        return entityMeta;
    }
    
    /**
     * Returns message resolver to be used for resolving localized messages.
     * 
     * @return Message resolver to be used for resolving localized messages.
     */
    public final MessageResolver getMessageResolver() {
        return messageResolver;
    }
    
    /**
     * Returns message builder to be used for building error messages.
     * 
     * @return Message builder to be used for building error messages.
     */
    public final MessageBuilder getMessageBuilder() {
        return messageBuilder;
    }
    
    // Data operations
    
    /**
     * Returns property value for the specified entity.
     * 
     * @param <V> The property value type.
     * @param entity Entity whose property value should be returned.
     * @param property Property name.
     * @return Property value for the specified entity.
     * @throws IllegalArgumentException if the specified entity is
     *         <code>null</code> or the specified property not exists.
     * @throws UnsupportedOperationException if the specified property is not
     *         readable.
     */
    public <V> V getValue(T entity, String property) {
        return entityMeta.<V>getPropertyMetaData(property).getValue(entity);
    }
    
    /**
     * Assigns property value for the specified entity.
     * 
     * @param entity Entity whose property value should be assigned.
     * @param property Property name.
     * @param value New property value.
     * @throws IllegalArgumentException if the specified entity is
     *         <code>null</code> or the specified property not exists.
     * @throws UnsupportedOperationException if the specified property is not
     *         writeable.
     */
    public void setValue(T entity, String property, Object value) {
        entityMeta.getPropertyMetaData(property).setValue(entity, value);
    }
    
    /**
     * Returns property values for the specified entity.
     * 
     * @param entity Entity whose property values should be returned.
     * @return Property values for the specified entity.
     * @throws IllegalArgumentException if the specified entity is
     *         <code>null</code>.
     * @see ContextBuilder#getValues(Object)
     */
    public Map<String, ?> getValues(T entity) {
        return newContext().getValues(entity);
    }
    
    /**
     * Assigns property values for the specified entity.
     * 
     * @param entity Entity whose property values should be assigned.
     * @param values New property values.
     * @throws IllegalArgumentException if the specified entity is
     *         <code>null</code>.
     * @see ContextBuilder#setValues(Object, Map)
     */
    public void setValues(T entity, Map<String, ?> values) {
        newContext().setValues(entity, values);
    }
    
    /**
     * Returns default string representation of property value for the
     * specified entity.
     * 
     * @param entity Entity whose string representation of property value
     *        should be returned.
     * @param property Property name.
     * @return Default string representation of property value for the
     *         specified entity.
     * @throws IllegalArgumentException if the specified entity is
     *         <code>null</code> or the specified property not exists.
     * @throws UnsupportedOperationException if the specified property is
     *         not readable.
     * @see ContextBuilder#getEncodedValue(Object, String)
     */
    public String getRawValue(T entity, String property) {
        return newContext().setLocalizedConvert(false).getEncodedValue(entity, property);
    }
    
    /**
     * Assigns property value for the specified entity.
     * 
     * @param entity Entity whose property value should be assigned.
     * @param property Property name.
     * @param value Default string representation of property value.
     * @throws IllegalArgumentException if the specified entity is
     *         <code>null</code> or the specified property not exists.
     * @throws UnsupportedOperationException if the specified property is
     *         not writeable.
     * @throws MalformedValueException if conversion fails.
     * @see ContextBuilder#setEncodedValue(Object, String, String)
     */
    public void setRawValue(T entity, String property, String value) {
        newContext().setLocalizedConvert(false).setEncodedValue(entity, property, value);
    }
    
    /**
     * Returns default string representations of property values for the
     * specified entity.
     * 
     * @param entity Entity whose string representations of property values
     *        should be returned.
     * @return Default string representations of property values for the
     *         specified entity.
     * @throws IllegalArgumentException if the specified entity is
     *         <code>null</code>.
     * @see ContextBuilder#getEncodedValues(Object)
     */
    public Map<String, String> getRawValues(T entity) {
        return newContext().setLocalizedConvert(false).getEncodedValues(entity);
    }
    
    /**
     * Assigns property values for the specified entity.
     * 
     * @param entity Entity whose property values should be assigned.
     * @param values Default string representations of property values.
     * @throws IllegalArgumentException if the specified entity is
     *         <code>null</code>.
     * @throws ValidationException if conversion fails.
     * @see ContextBuilder#setEncodedValues(Object, Map)
     */
    public void setRawValues(T entity, Map<String, String> values) {
        newContext().setLocalizedConvert(false).setEncodedValues(entity, values);
    }
    
    /**
     * Returns localized string representation of property value for the
     * specified entity using default locale.
     * 
     * @param entity Entity whose string representation of property value
     *        should be returned.
     * @param property Property name.
     * @return Localized string representation of property value for the
     *         specified entity.
     * @throws IllegalArgumentException if the specified entity is
     *         <code>null</code> or the specified property not exists.
     * @throws UnsupportedOperationException if the specified property is
     *         not readable.
     * @see ContextBuilder#getEncodedValue(Object, String)
     */
    public String getLocalizedValue(T entity, String property) {
        return newContext().setLocalizedConvert(true).getEncodedValue(entity, property);
    }
    
    /**
     * Assigns property value for the specified entity using default locale.
     * 
     * @param entity Entity whose property value should be assigned.
     * @param property Property name.
     * @param value Localized string representation of property value.
     * @throws IllegalArgumentException if the specified entity is
     *         <code>null</code> or the specified property not exists.
     * @throws UnsupportedOperationException if the specified property is
     *         not writeable.
     * @throws MalformedValueException if conversion fails.
     * @see ContextBuilder#setEncodedValue(Object, String, String)
     */
    public void setLocalizedValue(T entity, String property, String value) {
        newContext().setLocalizedConvert(true).setEncodedValue(entity, property, value);
    }
    
    /**
     * Returns localized string representations of property values for the
     * specified entity using default locale.
     * 
     * @param entity Entity whose string representations of property values
     *        should be returned.
     * @return Localized string representations of property values for the
     *         specified entity.
     * @throws IllegalArgumentException if the specified entity is
     *         <code>null</code>.
     * @see ContextBuilder#getEncodedValues(Object)
     */
    public Map<String, String> getLocalizedValues(T entity) {
        return newContext().setLocalizedConvert(true).getEncodedValues(entity);
    }
    
    /**
     * Assigns property values for the specified entity using default locale.
     * 
     * @param entity Entity whose property values should be assigned.
     * @param values Localized string representations of property values.
     * @throws IllegalArgumentException if the specified entity is
     *         <code>null</code>.
     * @throws ValidationException if conversion fails.
     * @see ContextBuilder#setEncodedValues(Object, Map)
     */
    public void setLocalizedValues(T entity, Map<String, String> values) {
        newContext().setLocalizedConvert(true).setEncodedValues(entity, values);
    }
    
    // Validation opeartions
    
    /**
     * Validates the specified entity and returns possibly modified entity.
     * 
     * @param entity Entity to be validated.
     * @param groups Array of constraint groups to be validated.
     * @return Possibly modified entity.
     * @throws IllegalArgumentException if the specified entity is
     *         <code>null</code>.
     * @throws ValidationException if validation fails.
     * @see ContextBuilder#validateEntity(Object)
     */
    public T validateEntity(T entity, String... groups) {
        return newContext().setValidatingGroups(groups).validateEntity(entity);
    }
    
    /**
     * Validates property value for the specified entity and assigns modified
     * value to that property.
     * 
     * @param <V> The property value type.
     * @param entity Entity whose property value should be validated.
     * @param property Property name.
     * @param groups Array of constraint groups to be validated.
     * @return Possibly modified value of the property.
     * @throws IllegalArgumentException if the specified entity is
     *         <code>null</code> or the specified property not exists.
     * @throws ConstraintViolationException if validation fails.
     * @see ContextBuilder#validateProperty(Object, String)
     */
    public <V> V validateProperty(T entity, String property, String... groups) {
        return newContext().setValidatingGroups(groups).validateProperty(entity, property);
    }
    
    /**
     * Validates the specified property value and returns possibly modified
     * value.
     * 
     * @param <V> The property value type.
     * @param property Property name.
     * @param value Property value to be validated.
     * @param groups Array of constraint groups to be validated.
     * @return Possibly modified value.
     * @throws IllegalArgumentException if the specified property not exists.
     * @throws ConstraintViolationException if validation fails.
     * @see ContextBuilder#validateValue(String, Object)
     */
    public <V> V validateValue(String property, Object value, String... groups) {
        return newContext().setValidatingGroups(groups).validateValue(property, value);
    }
    
    // ContextBuilder
    
    /**
     * Creates a new context builder.
     * 
     * @return A new context builder.
     */
    public ContextBuilder newContext() {
        return new ContextBuilder();
    }
    
    /**
     * Creates a new context builder for cascade validation.
     * 
     * @param parent Parent context.
     * @return A new context builder for cascade validation.
     * @throws IllegalStateException if the specified context state is invalid
     *         for cascade validation. 
     */
    public ContextBuilder newContext(ValidationContext<?> parent) {
        return new ContextBuilder(parent);
    }
    
    /**
     * This builder allows to perform validation operations with custom
     * parameters.
     * 
     * @author Fox Mulder
     */
    public class ContextBuilder {
        
        /**
         * Parent context in case of cascade validation.
         */
        private ValidationContext<?> parent = null;
        
        /**
         * Locale to be used for error messages and formatting numbers, dates, etc.
         */
        private Locale messageLocale = Locale.getDefault();
        
        /**
         * Filter to be used for property filtering.
         */
        private PropertyFilter propertyFilter = PropertyFilter.ALL;
        
        /**
         * Array of constraint groups we are validating.
         */
        private String[] validatingGroups = ALL_GROUPS;
        
        /**
         * Determines if converters should take locale into account.        
         */
        private boolean localizedConvert = false;
        
        /**
         * Determines if validation should fail on the first violation.
         */
        private boolean failFast = false;
        
        /**
         * Constructs default <code>ContextBuilder</code>.
         */
        protected ContextBuilder() {}
        
        /**
         * Constructs a new <code>ContextBuilder</code> for cascade validation.
         * 
         * @param parent Parent context.
         * @throws IllegalStateException if the specified context state is
         *         invalid for cascade validation. 
         */
        protected ContextBuilder(ValidationContext<?> parent) {
            if (parent.getElementMetaData() == null)
                throw new IllegalStateException();
            this.parent = parent;
            this.messageLocale = parent.getMessageLocale();
            this.validatingGroups = parent.getValidatingGroups();
            this.localizedConvert = parent.isLocalizedConvert();
        }
        
        /**
         * Returns a new context with predefined custom parameters.
         * 
         * @return A new context with predefined custom parameters.
         */
        protected Context build() {
            return new Context(this);
        }
        
        // Parameter setters
        
        /**
         * Sets locale to be used for error messages and formatting numbers,
         * dates, etc.
         * 
         * @param locale Locale to be used for error messages and formatting
         *        numbers, dates, etc.
         * @return Reference to this builder instance.
         */
        public final ContextBuilder setMessageLocale(Locale locale) {
            this.messageLocale = locale == null ? Locale.getDefault() : locale;
            return this;
        }
        
        /**
         * Sets filter to be used for property filtering.
         * 
         * @param filter Filter to be used for property filtering.
         * @return Reference to this builder instance.
         */
        public final ContextBuilder setPropertyFilter(PropertyFilter filter) {
            this.propertyFilter = filter == null ? PropertyFilter.ALL : filter;
            return this;
        }
        
        /**
         * Sets array of constraint groups to be validated.
         * 
         * @param groups Array of constraint groups to be validated.
         * @return Reference to this builder instance.
         */
        public final ContextBuilder setValidatingGroups(String... groups) {
            this.validatingGroups = groups == null ? ALL_GROUPS : groups;
            return this;
        }
        
        /**
         * Sets flag that determines if converters should take locale into
         * account.
         * 
         * @param localized Determines if converters should take locale into
         *        account.
         * @return Reference to this builder instance.
         */
        public final ContextBuilder setLocalizedConvert(boolean localized) {
            this.localizedConvert = localized;
            return this;
        }
        
        /**
         * Sets flag that determines if validation should fail on the first
         * violation.
         * 
         * @param fail Determines if validation should fail on the first
         *        violation.
         * @return Reference to this builder instance.
         */
        public final ContextBuilder setFailFast(boolean fail) {
            this.failFast = fail;
            return this;
        }
        
        // Data operations
        
        /**
         * Returns property values for the specified entity using predefined
         * custom parameters.
         * 
         * @param entity Entity whose property values should be returned.
         * @return Property values for the specified entity.
         * @throws IllegalArgumentException if the specified entity is
         *         <code>null</code>.
         */
        public final Map<String, ?> getValues(T entity) {
            return build().getValues(entity);
        }
        
        /**
         * Assigns property values for the specified entity using predefined
         * custom parameters.
         * 
         * @param entity Entity whose property values should be assigned.
         * @param values New property values.
         * @throws IllegalArgumentException if the specified entity is
         *         <code>null</code>.
         */
        public final void setValues(T entity, Map<String, ?> values) {
            build().setValues(entity, values);
        }
        
        /**
         * Returns string representation of property value for the specified
         * entity using predefined custom parameters.
         * 
         * @param entity Entity whose string representation of property value
         *        should be returned.
         * @param property Property name.
         * @return String representation of property value for the specified
         *         entity.
         * @throws IllegalArgumentException if the specified entity is
         *         <code>null</code> or the specified property not exists.
         * @throws UnsupportedOperationException if the specified property is
         *         not readable.
         */
        public final String getEncodedValue(T entity, String property) {
            return build().getEncodedValue(entity, property);
        }
        
        /**
         * Assigns property value for the specified entity using predefined
         * custom parameters.
         * 
         * @param entity Entity whose property value should be assigned.
         * @param property Property name.
         * @param value String representation of property value.
         * @throws IllegalArgumentException if the specified entity is
         *         <code>null</code> or the specified property not exists.
         * @throws UnsupportedOperationException if the specified property is
         *         not writeable.
         * @throws MalformedValueException if conversion fails.
         */
        public final void setEncodedValue(T entity, String property, String value) {
            build().setEncodedValue(entity, property, value);
        }
        
        /**
         * Returns string representations of property values for the specified
         * entity using predefined custom parameters.
         * 
         * @param entity Entity whose string representations of property values
         *        should be returned.
         * @return String representations of property values for the specified
         *         entity.
         * @throws IllegalArgumentException if the specified entity is
         *         <code>null</code>.
         */
        public final Map<String, String> getEncodedValues(T entity) {
            return build().getEncodedValues(entity);
        }
        
        /**
         * Assigns property values for the specified entity using predefined
         * custom parameters.
         * 
         * @param entity Entity whose property values should be assigned.
         * @param values String representations of property values.
         * @throws IllegalArgumentException if the specified entity is
         *         <code>null</code>.
         * @throws ValidationException if conversion fails.
         */
        public final void setEncodedValues(T entity, Map<String, String> values) {
            build().setEncodedValues(entity, values);
        }
        
        /**
         * Converts property value into string representation using predefined
         * custom parameters.
         * 
         * @param <V> The property value type.
         * @param property Property name.
         * @param value Property value to be encoded.
         * @return String representation of property value.
         * @throws IllegalArgumentException if the specified property not exists.
         */
        public final <V> String encodeValue(String property, V value) {
            return build().encodeValue(property, value);
        }
        
        /**
         * Converts string representation of property value into object using
         * predefined custom parameters.
         * 
         * @param <V> The property value type.
         * @param property Property name.
         * @param value String representation of property value.
         * @return Decoded property value.
         * @throws IllegalArgumentException if the specified property not exists.
         * @throws MalformedValueException if conversion fails.
         */
        public final <V> V decodeValue(String property, String value) {
            return build().decodeValue(property, value);
        }
        
        /**
         * Converts array of property values into array of their string
         * representations using predefined custom parameters.
         * 
         * @param <V> The property values type.
         * @param property Property name.
         * @param values Array of property values to be encoded.
         * @return Array of string representations of property values.
         * @throws IllegalArgumentException if the specified property not exists.
         */
        public final <V> String[] encodeValues(String property, V... values) {
            return build().encodeValues(property, values);
        }
        
        /**
         * Converts array of string representations of property values into
         * array of objects using predefined custom parameters.
         * 
         * @param <V> The property values type.
         * @param property Property name.
         * @param values Array of string representations of property values.
         * @return Array of decoded property values.
         * @throws IllegalArgumentException if the specified property not exists.
         * @throws MalformedValueException if conversion fails.
         */
        public final <V> V[] decodeValues(String property, String... values) {
            return build().decodeValues(property, values);
        }
        
        /**
         * Converts value into string representation using the specified
         * converter and predefined custom parameters.
         * 
         * @param <V> The value type.
         * @param converter Converter to be used for value conversion.
         * @param value Value to be encoded.
         * @return String representation of value.
         */
        public final <V> String encodeValue(Converter<V> converter, V value) {
            return build().encodeValue(converter, value);
        }
        
        /**
         * Converts string representation of value into object using the
         * specified converter and predefined custom parameters.
         * 
         * @param <V> The value type.
         * @param converter Converter to be used for value conversion.
         * @param value String representation of value.
         * @return Decoded value.
         * @throws MalformedValueException if conversion fails.
         */
        public final <V> V decodeValue(Converter<V> converter, String value) {
            return build().decodeValue(converter, value);
        }
        
        /**
         * Converts array of values into array of their string representations
         * using the specified converter and predefined custom parameters.
         * 
         * @param <V> The values type.
         * @param converter Converter to be used for values conversion.
         * @param values Array of values to be encoded.
         * @return Array of string representations of values.
         */
        public final <V> String[] encodeValues(Converter<V> converter, V... values) {
            return build().encodeValues(converter, values);
        }
        
        /**
         * Converts array of string representations of values into array of
         * objects using the specified converter and predefined custom
         * parameters. 
         * 
         * @param <V> The values type.
         * @param converter Converter to be used for values conversion.
         * @param values Array of string representations of values.
         * @return Array of decoded values.
         * @throws MalformedValueException if conversion fails.
         */
        public final <V> V[] decodeValues(Converter<V> converter, String... values) {
            return build().decodeValues(converter, values);
        }
        
        // Validation operations
        
        /**
         * Validates the specified entity and returns possibly modified entity
         * using predefined custom parameters.
         * 
         * @param entity Entity to be validated.
         * @return Possibly modified entity.
         * @throws IllegalArgumentException if the specified entity is
         *         <code>null</code>.
         * @throws ValidationException if validation fails.
         */
        public final T validateEntity(T entity) {
            return build().validateEntity(entity);
        }
        
        /**
         * Validates property value for the specified entity using predefined
         * custom parameters and assigns modified value to that property.
         * 
         * @param <V> The property value type.
         * @param entity Entity whose property value should be validated.
         * @param property Property name.
         * @return Possibly modified value of the property.
         * @throws IllegalArgumentException if the specified entity is
         *         <code>null</code> or the specified property not exists.
         * @throws ConstraintViolationException if validation fails.
         */
        public final <V> V validateProperty(T entity, String property) {
            return build().validateProperty(entity, property);
        }
        
        /**
         * Validates the specified property value using predefined custom
         * parameters and returns possibly modified value.
         * 
         * @param <V> The property value type.
         * @param property Property name.
         * @param value Property value to be validated.
         * @return Possibly modified value.
         * @throws IllegalArgumentException if the specified property not exists.
         * @throws ConstraintViolationException if validation fails.
         */
        public final <V> V validateValue(String property, Object value) {
            return build().validateValue(property, value);
        }
        
        // Message operations
        
        /**
         * Builds localized error message for the specified validation
         * component using predefined custom parameters.
         * 
         * @param component Validation component.
         * @return Localized error message for the specified validation
         *         component.
         */
        public final String buildMessage(Validation<?> component) {
            return build().buildMessage(component);
        }
        
    }
    
    // Context
    
    /**
     * This context holds current validation state.
     * 
     * @author Fox Mulder
     * @see ContextBuilder
     */
    protected class Context implements ValidationContext<T> {
        
        /**
         * Parent context in case of cascade validation.
         */
        protected final ValidationContext<?> parent;
        
        /**
         * Current entity we are validating.
         */
        protected T currentEntity = null;
        
        /**
         * Current element metadata we are validating.
         */
        protected ElementMetaData<T, ?> elementMeta = null;
        
        /**
         * Current validation target we are validating.
         */
        protected ValidationTarget currentTarget = ValidationTarget.VALUE;
        
        /**
         * Current item index we are validating.
         */
        protected Object currentIndex = null;
        
        /**
         * Locale to be used for error messages and formatting numbers, dates, etc.
         */
        protected final Locale messageLocale;
        
        /**
         * Filter to be used for property filtering.
         */
        protected final PropertyFilter propertyFilter;
        
        /**
         * Array of constraint groups we are validating.
         */
        protected final String[] validatingGroups;
        
        /**
         * Determines if converters should take locale into account.        
         */
        protected final boolean localizedConvert;
        
        /**
         * Determines if validation should fail on the first violation.
         */
        protected final boolean failFast;
        
        /**
         * Default format of integer numbers.
         */
        private NumberFormat integerFormat;
        
        /**
         * Default format of decimal numbers.
         */
        private NumberFormat decimalFormat;
        
        /**
         * Default format of dates.
         */
        private DateFormat dateFormat;
        
        /**
         * List of violations.
         */
        private List<ViolationException> violations;
        
        /**
         * Constructs a new <code>Context</code> using parameters from the
         * specified context builder.
         * 
         * @param builder Context builder.
         */
        protected Context(ContextBuilder builder) {
            this.parent = builder.parent;
            this.messageLocale = builder.messageLocale;
            this.propertyFilter = builder.propertyFilter;
            this.validatingGroups = builder.validatingGroups;
            this.localizedConvert = builder.localizedConvert;
            this.failFast = builder.failFast;
        }
        
        // State
        
        /**
         * {@inheritDoc}
         * @see ValidationContext#getValidator()
         */
        @Override
        public final Validator<T> getValidator() {
            return Validator.this;
        }
        
        /**
         * {@inheritDoc}
         * @see ValidationContext#getEntityType()
         */
        @Override
        public final Class<T> getEntityType() {
            return entityMeta.getType();
        }
        
        /**
         * {@inheritDoc}
         * @see ValidationContext#getElementMetaData()
         */
        @Override
        public final EntityMetaData<T> getEntityMetaData() {
            return entityMeta;
        }
        
        /**
         * {@inheritDoc}
         * @see ValidationContext#getElementType()
         */
        @Override
        public final Class<?> getElementType() {
            return elementMeta == null ? null : elementMeta.getType();
        }
        
        /**
         * {@inheritDoc}
         * @see ValidationContext#getElementName()
         */
        @Override
        public final String getElementName() {
            return elementMeta == null ? null : elementMeta.getName();
        }
        
        /**
         * {@inheritDoc}
         * @see ValidationContext#getElementMetaData()
         */
        @Override
        public final <V> ElementMetaData<T, V> getElementMetaData() {
            return Types.cast(elementMeta);
        }
        
        /**
         * {@inheritDoc}
         * @see ValidationContext#getRootEntity()
         */
        @Override
        public final Object getRootEntity() {
            return parent == null ? currentEntity : parent.getRootEntity();
        }
        
        /**
         * {@inheritDoc}
         * @see ValidationContext#getCurrentEntity()
         */
        @Override
        public final T getCurrentEntity() {
            return currentEntity;
        }
        
        /**
         * {@inheritDoc}
         * @see ValidationContext#getCurrentTarget()
         */
        @Override
        public ValidationTarget getCurrentTarget() {
            return currentTarget;
        }
        
        /**
         * {@inheritDoc}
         * @see ValidationContext#setCurrentTarget(ValidationTarget)
         */
        @Override
        public void setCurrentTarget(ValidationTarget target) {
            this.currentTarget = target == null ? ValidationTarget.VALUE : target;
        }
        
        /**
         * {@inheritDoc}
         * @see ValidationContext#getCurrentIndex()
         */
        @Override
        public final Object getCurrentIndex() {
            return currentIndex;
        }
        
        /**
         * {@inheritDoc}
         * @see ValidationContext#setCurrentIndex(Object)
         */
        @Override
        public final void setCurrentIndex(Object index) {
            this.currentIndex = index;
        }
        
        // Parameter getters
        
        /**
         * {@inheritDoc}
         * @see ValidationContext#getMessageLocale()
         */
        @Override
        public final Locale getMessageLocale() {
            return messageLocale;
        }
        
        /**
         * {@inheritDoc}
         * @see ValidationContext#getPropertyFilter()
         */
        @Override
        public final PropertyFilter getPropertyFilter() {
            return propertyFilter;
        }
        
        /**
         * {@inheritDoc}
         * @see ValidationContext#getValidatingGroups()
         */
        @Override
        public final String[] getValidatingGroups() {
            return validatingGroups.length == 0 ? validatingGroups : validatingGroups.clone();
        }
        
        /**
         * {@inheritDoc}
         * @see ValidationContext#isLocalizedConvert()
         */
        @Override
        public final boolean isLocalizedConvert() {
            return localizedConvert;
        }
        
        /**
         * {@inheritDoc}
         * @see ValidationContext#isFailFast()
         */
        @Override
        public final boolean isFailFast() {
            return failFast;
        }
        
        /**
         * {@inheritDoc}
         * @see ValidationContext#getIntegerFormat()
         */
        @Override
        public final NumberFormat getIntegerFormat() {
            if (integerFormat == null)
                integerFormat = defaultIntegerPattern == null
                    ? NumberFormat.getIntegerInstance(messageLocale)
                    : getIntegerFormat(defaultIntegerPattern);
            return integerFormat;
        }
        
        /**
         * {@inheritDoc}
         * @see ValidationContext#getIntegerFormat(String)
         */
        @Override
        public final NumberFormat getIntegerFormat(String pattern) {
            if (pattern == null)
                return getIntegerFormat();
            DecimalFormatSymbols symbols = DecimalFormatSymbols.getInstance(messageLocale);
            DecimalFormat format = new DecimalFormat(pattern, symbols);
            format.setMaximumFractionDigits(0);
            format.setParseIntegerOnly(true);
            return format;
        }
        
        /**
         * {@inheritDoc}
         * @see ValidationContext#getDecimalFormat()
         */
        @Override
        public final NumberFormat getDecimalFormat() {
            if (decimalFormat == null)
                decimalFormat = defaultDecimalPattern == null
                    ? NumberFormat.getInstance(messageLocale)
                    : getDecimalFormat(defaultDecimalPattern);
            return decimalFormat;
        }
        
        /**
         * {@inheritDoc}
         * @see ValidationContext#getDecimalFormat(String)
         */
        @Override
        public final NumberFormat getDecimalFormat(String pattern) {
            if (pattern == null)
                return getDecimalFormat();
            DecimalFormatSymbols symbols = DecimalFormatSymbols.getInstance(messageLocale);
            return new DecimalFormat(pattern, symbols);
        }
        
        /**
         * {@inheritDoc}
         * @see ValidationContext#getDateFormat()
         */
        @Override
        public final DateFormat getDateFormat() {
            if (dateFormat == null)
                dateFormat = defaultDatePattern == null
                    ? getDateFormat(defaultDateStyle, defaultTimeStyle)
                    : getDateFormat(defaultDatePattern);
            return dateFormat;
        }
        
        /**
         * {@inheritDoc}
         * @see ValidationContext#getDateFormat(String)
         */
        @Override
        public final DateFormat getDateFormat(String pattern) {
            if (pattern == null)
                return getDateFormat();
            return new SimpleDateFormat(pattern, messageLocale);
        }
        
        /**
         * {@inheritDoc}
         * @see ValidationContext#getDateFormat(int, int)
         */
        @Override
        public final DateFormat getDateFormat(int dateStyle, int timeStyle) {
            if (dateStyle < 0 && timeStyle < 0)
                return getDateFormat();
            return timeStyle < 0
                ? DateFormat.getDateInstance(dateStyle, messageLocale)
                : dateStyle < 0
                    ? DateFormat.getTimeInstance(timeStyle, messageLocale)
                    : DateFormat.getDateTimeInstance(dateStyle, timeStyle, messageLocale);
        }
        
        // Message operations
        
        /**
         * {@inheritDoc}
         * @see ValidationContext#resolveMessage(String)
         */
        @Override
        public final String resolveMessage(String key) {
            return messageResolver.resolveMessage(key, messageLocale);
        }
        
        /**
         * {@inheritDoc}
         * @see ValidationContext#buildMessage(Validation)
         */
        @Override
        public final String buildMessage(Validation<?> component) {
            return messageBuilder.buildMessage(component, this);
        }
        
        // Data operations
        
        // See ContextBuilder.getValues(Object)
        protected Map<String, ?> getValues(T entity) {
            currentEntity = checkEntity(entity);
            Map<String, Object> values = new LinkedHashMap<String, Object>();
            for (PropertyMetaData<T, Object> meta : entityMeta.getAllPropertyMetaData())
                if (meta.isReadable() && propertyFilter.accept(meta))
                    values.put(meta.getName(), meta.getValue(entity));
            return values;
        }
        
        // See ContextBuilder.setValues(Object, Map)
        protected void setValues(T entity, Map<String, ?> values) {
            currentEntity = checkEntity(entity);
            for (PropertyMetaData<T, Object> meta : entityMeta.getAllPropertyMetaData())
                if (meta.isWriteable() && propertyFilter.accept(meta))
                    if (values.containsKey(meta.getName()))
                        meta.setValue(entity, values.get(meta.getName()));
        }
        
        // See ContextBuilder.getEncodedValue(Object, String)
        protected String getEncodedValue(T entity, String property) {
            currentEntity = checkEntity(entity);
            PropertyMetaData<T, Object> meta = entityMeta.getPropertyMetaData(property);
            elementMeta = meta;
            return meta.getConverter().encode(meta.getValue(entity), this);
        }
        
        // See ContextBuilder.setEncodedValue(Object, String, String)
        protected void setEncodedValue(T entity, String property, String value) {
            currentEntity = checkEntity(entity);
            PropertyMetaData<T, Object> meta = entityMeta.getPropertyMetaData(property);
            elementMeta = meta;
            meta.setValue(entity, meta.getConverter().decode(value, this));
        }
        
        // See ContextBuilder.getEncodedValues(Object)
        protected Map<String, String> getEncodedValues(T entity) {
            currentEntity = checkEntity(entity);
            Map<String, String> values = new LinkedHashMap<String, String>();
            for (PropertyMetaData<T, Object> meta : entityMeta.getAllPropertyMetaData())
                if (meta.isReadable() && propertyFilter.accept(meta)) {
                    elementMeta = meta;
                    Object value = meta.getValue(entity);
                    values.put(meta.getName(), meta.getConverter().encode(value, this));
                }
            return values;
        }
        
        // See ContextBuilder.setEncodedValues(Object, Map)
        protected void setEncodedValues(T entity, Map<String, String> values) {
            currentEntity = checkEntity(entity);
            for (PropertyMetaData<T, Object> meta : entityMeta.getAllPropertyMetaData())
                if (values.containsKey(meta.getName()))
                    if (meta.isWriteable() && propertyFilter.accept(meta)) {
                        try {
                            elementMeta = meta;
                            String value = values.get(meta.getName());
                            meta.setValue(entity, meta.getConverter().decode(value, this));
                        } catch (MalformedValueException e) {
                            addViolation(e);
                        }
                    }
            throwIfViolated();
        }
        
        // See ContextBuilder.encodeValue(String, Object)
        protected <V> String encodeValue(String property, V value) {
            PropertyMetaData<T, Object> meta = entityMeta.getPropertyMetaData(property);
            elementMeta = meta;
            return meta.getConverter().encode(value, this);
        }
        
        // See ContextBuilder.decodeValue(String, String)
        protected <V> V decodeValue(String property, String value) {
            PropertyMetaData<T, V> meta = entityMeta.getPropertyMetaData(property);
            elementMeta = meta;
            return meta.getConverter().decode(value, this);
        }
        
        // See ContextBuilder.encodeValues(String, Object...)
        protected <V> String[] encodeValues(String property, V... values) {
            PropertyMetaData<T, V> meta = entityMeta.getPropertyMetaData(property);
            elementMeta = meta;
            return encodeValues(meta.getConverter(), values);
        }
        
         // See ContextBuilder.decodeValues(String, String...)
        protected <V> V[] decodeValues(String property, String... values) {
            PropertyMetaData<T, V> meta = entityMeta.getPropertyMetaData(property);
            elementMeta = meta;
            return decodeValues(meta.getConverter(), values);
        }
        
        // See ContextBuilder.encodeValue(Converter, Object)
        protected <V> String encodeValue(Converter<V> converter, V value) {
            return converter.encode(value, this);
        }
        
        // See ContextBuilder.decodeValue(Converter, String)
        protected <V> V decodeValue(Converter<V> converter, String value) {
            return converter.decode(value, this);
        }
        
        // See ContextBuilder.encodeValues(Converter, Object...)
        protected <V> String[] encodeValues(Converter<V> converter, V... values) {
            String[] array = new String[values.length];
            for (int i = 0; i < values.length; i++)
                array[i] = converter.encode(values[i], this);
            return array;
        }
        
        // See ContextBuilder.decodeValues(Converter, String...)
        protected <V> V[] decodeValues(Converter<V> converter, String... values) {
            V[] array = Types.newArray(converter.getType(), values.length);
            for (int i = 0; i < values.length; i++)
                array[i] = converter.decode(values[i], this);
            return array;
        }
        
        // Validation operations
        
        // See ContextBuilder.validateEntity(Object)
        protected T validateEntity(T entity) {
            currentEntity = entity;
            if (entity != null) {
                for (PropertyMetaData<T, Object> meta : entityMeta.getAllPropertyMetaData())
                    if (meta.isReadable() && propertyFilter.accept(meta)) {
                        try {
                            elementMeta = meta;
                            validateProperty(meta);
                        } catch (ConstraintViolationException e) {
                            addViolation(e);
                        }
                    }
            }
            
            try {
                elementMeta = null;
                validate(entityMeta, entity);
            } catch (ConstraintViolationException e) {
                addViolation(e);
            }
            
            throwIfViolated();
            return entity;
        }
        
        // See ContextBuilder.validateProperty(Object, String)
        protected <V> V validateProperty(T entity, String property) {
            currentEntity = checkEntity(entity);
            PropertyMetaData<T, V> meta = entityMeta.getPropertyMetaData(property);
            elementMeta = meta;
            return validateProperty(meta);
        }
        
        // Performs property validation for the specified property metatdata.
        protected <V> V validateProperty(PropertyMetaData<T, V> meta) {
            V oldValue = meta.getValue(getCurrentEntity());
            V newValue = validate(meta, oldValue);
            if (meta.isWriteable() && oldValue == null ? newValue != null : !oldValue.equals(newValue))
                meta.setValue(getCurrentEntity(), newValue);
            return newValue;
        }
        
        // See ContextBuilder.validateValue(String, Object)
        protected <V> V validateValue(String property, Object value) {
            PropertyMetaData<T, V> meta = entityMeta.getPropertyMetaData(property);
            elementMeta = meta;
            return validate(meta, meta.cast(value));
        }
        
        // Performs value validation using the specified metadata.
        protected <V> V validate(MetaData<V> meta, V value) {
            Constraint<? super V> constraint = meta.getConstraint();
            if (constraint != null)
                value = meta.cast(constraint.validate(value, this));
            return value;
        }
        
        // Checks if entity is not null.
        protected T checkEntity(T entity) {
            if (entity == null)
                throw new IllegalArgumentException("entity");
            return entity;
        }
        
        // Adds a new violation to the list of violations.
        protected final void addViolation(ViolationException violation) {
            if (violations == null)
                violations = new LinkedList<ViolationException>();
            violations.add(violation);
        }
        
         // Throws ValidationException if any violations present.
        protected final void throwIfViolated() {
            if (violations != null)
                throw new ValidationException(violations, parent != null);
        }
        
        // String
        
        public String toString() {
            StringBuilder buf = new StringBuilder();
            buf.append(entityMeta.getType().getName());
            if (elementMeta != null)
                buf.append('#').append(elementMeta.getName());
            return buf.toString();
        }
        
    }
    
}
