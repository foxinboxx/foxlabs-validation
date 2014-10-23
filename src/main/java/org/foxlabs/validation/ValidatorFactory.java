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

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.text.DecimalFormat;

import org.foxlabs.validation.message.MessageBuilder;
import org.foxlabs.validation.message.MessageResolver;
import org.foxlabs.validation.message.ResourceMessageResolver;
import org.foxlabs.validation.metadata.BeanMetaData;
import org.foxlabs.validation.metadata.EntityMetaData;

/**
 * Defines a factory that allows to create <code>Validator</code> instances.
 * 
 * <p>The main purpose of this factory is maintaining validators configuration.
 * You can configure factory once and then create validators with different
 * metadata.</p>
 * 
 * @author Fox Mulder
 * @see Validator
 */
public class ValidatorFactory {
    
    /**
     * Default pattern for integer numbers.
     */
    protected String integerPattern = null;
    
    /**
     * Default pattern for decimal numbers.
     */
    protected String decimalPattern = null;
    
    /**
     * Default pattern for dates.
     */
    protected String datePattern = null;
    
    /**
     * Default date style.
     */
    protected int dateStyle = DateFormat.DEFAULT;
    
    /**
     * Default time style.
     */
    protected int timeStyle = DateFormat.DEFAULT;
    
    /**
     * Message resolver to be used for resolving localized messages.
     */
    protected MessageResolver messageResolver = MessageResolver.DEFAULT;
    
    /**
     * Message builder to be used for building error messages.
     */
    protected MessageBuilder messageBuilder = MessageBuilder.DEFAULT;
    
    /**
     * Constructs default <code>ValidatorFactory</code>.
     */
    public ValidatorFactory() {}
    
    /**
     * Constructs a new <code>ValidatorFactory</code> with the specified
     * message bundle name.
     * 
     * @param bundleName Message bundle name.
     */
    public ValidatorFactory(String bundleName) {
        this.messageResolver = new ResourceMessageResolver(bundleName);
    }
    
    /**
     * Sets default format of integer numbers according to the specified
     * pattern.
     * 
     * @param pattern Default pattern for integer numbers.
     * @return Reference to this factory instance.
     */
    public ValidatorFactory setIntegerFormat(String pattern) {
        this.integerPattern = pattern == null ? null : new DecimalFormat(pattern).toPattern();
        return this;
    }
    
    /**
     * Sets default format of decimal numbers according to the specified
     * pattern.
     * 
     * @param pattern Default pattern for decimal numbers.
     * @return Reference to this factory instance.
     */
    public ValidatorFactory setDecimalFormat(String pattern) {
        this.decimalPattern = pattern == null ? null : new DecimalFormat(pattern).toPattern();
        return this;
    }
    
    /**
     * Sets default format of dates according to the specified pattern.
     * 
     * @param pattern Default pattern for dates.
     * @return Reference to this factory instance.
     */
    public ValidatorFactory setDateFormat(String pattern) {
        this.datePattern = pattern == null ? null : new SimpleDateFormat(pattern).toPattern();
        return this;
    }
    
    /**
     * Sets default format of dates according to the specified date and time
     * styles. If negative value will be specified as date or time style (but
     * not both) then corresponding part would not be used.
     * 
     * @param dateStyle Default date style.
     * @param timeStyle Default time style.
     * @return Reference to this factory instance.
     */
    public ValidatorFactory setDateFormat(int dateStyle, int timeStyle) {
        if (timeStyle < 0) {
            DateFormat.getDateInstance(dateStyle);
        } else if (dateStyle < 0) {
            DateFormat.getTimeInstance(timeStyle);
        } else {
            DateFormat.getDateTimeInstance(dateStyle, timeStyle);
        }
        this.dateStyle = dateStyle;
        this.timeStyle = timeStyle;
        this.datePattern = null;
        return this;
    }
    
    /**
     * Sets message resolver to be used for resolving localized messages.
     * 
     * @param resolver Message resolver to be used for resolving localized
     *        messages.
     * @return Reference to this factory instance.
     */
    public ValidatorFactory setMessageResolver(MessageResolver resolver) {
        this.messageResolver = resolver == null ? MessageResolver.DEFAULT : resolver;
        return this;
    }
    
    /**
     * Sets message builder to be used for building error messages.
     * 
     * @param builder Message builder to be used for building error messages.
     * @return Reference to this factory instance.
     */
    public ValidatorFactory setMessageBuilder(MessageBuilder builder) {
        this.messageBuilder = builder == null ? MessageBuilder.DEFAULT : builder;
        return this;
    }
    
    /**
     * Creates a new validator for the specified bean type using factory
     * configuration.
     * 
     * @param type Bean type for which validator should be created.
     * @return Validator for the specified bean type.
     */
    public <T> Validator<T> newValidator(Class<T> type) {
        return newValidator(BeanMetaData.<T>getMetaData(type));
    }
    
    /**
     * Creates a new validator for the specified bean type using configuration
     * from another validator.
     * 
     * @param type Bean type for which validator should be created.
     * @param config Validator whose configuration should be taken.
     * @return Validator for the specified bean type.
     */
    public <T> Validator<T> newValidator(Class<T> type, Validator<?> config) {
        return newValidator(BeanMetaData.<T>getMetaData(type), config);
    }
    
    /**
     * Creates a new validator for the specified entity metadata using factory
     * configuration.
     * 
     * @param metadata Entity metadata for which validator should be created.
     * @return Validator for the specified entity metadata.
     */
    public <T> Validator<T> newValidator(EntityMetaData<T> metadata) {
        return new Validator<T>(this, metadata, messageResolver, messageBuilder,
                integerPattern, decimalPattern, datePattern, dateStyle, timeStyle);
    }
    
    /**
     * Creates a new validator for the specified entity metadata using
     * configuration from another validator.
     * 
     * @param metadata Entity metadata.
     * @param config Validator whose configuration should be taken.
     * @return Validator for the specified entity metadata.
     */
    public <T> Validator<T> newValidator(EntityMetaData<T> metadata, Validator<?> config) {
        return new Validator<T>(this, metadata,
                config.messageResolver, config.messageBuilder,
                config.defaultIntegerPattern, config.defaultDecimalPattern,
                config.defaultDatePattern, config.defaultDateStyle, config.defaultTimeStyle);
    }
    
    /**
     * Returns immutable validator factory configured by default.
     * 
     * @return Immutable validator factory configured by default.
     */
    public static ValidatorFactory getDefault() {
        return DEFAULT_FACTORY;
    }
    
    /**
     * Validator factory configured by default.
     */
    private static final ValidatorFactory DEFAULT_FACTORY = new ValidatorFactory() {
        @Override public ValidatorFactory setIntegerFormat(String pattern) {
            throw new UnsupportedOperationException();
        }
        @Override public ValidatorFactory setDecimalFormat(String pattern) {
            throw new UnsupportedOperationException();
        }
        @Override public ValidatorFactory setDateFormat(String pattern) {
            throw new UnsupportedOperationException();
        }
        @Override public ValidatorFactory setDateFormat(int dateStyle, int timeStyle) {
            throw new UnsupportedOperationException();
        }
        @Override public ValidatorFactory setMessageResolver(MessageResolver resolver) {
            throw new UnsupportedOperationException();
        }
        @Override public ValidatorFactory setMessageBuilder(MessageBuilder builder) {
            throw new UnsupportedOperationException();
        }
    };
    
}
