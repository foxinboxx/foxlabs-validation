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

import java.util.Locale;

import java.text.DateFormat;
import java.text.NumberFormat;

import org.foxlabs.validation.metadata.ElementMetaData;
import org.foxlabs.validation.metadata.EntityMetaData;
import org.foxlabs.validation.metadata.PropertyFilter;

/**
 * This context holds current validation state.
 * 
 * @param <T> The type of validated entity
 * @author Fox Mulder
 */
public interface ValidationContext<T> {
    
    /**
     * Returns validator used for entity validation.
     * 
     * @return Validator used for entity validation.
     */
    Validator<T> getValidator();
    
    // State
    
    /**
     * Returns type of the entity we are validating.
     * 
     * @return Type of the entity we are validating.
     */
    Class<T> getEntityType();
    
    /**
     * Returns metadata of the current entity we are validating.
     * 
     * @return Metadata of the current entity we are validating.
     */
    EntityMetaData<T> getEntityMetaData();
    
    /**
     * Returns type of the element defined on the entity we are validating.
     * 
     * @return Type of the element defined on the entity we are validating.
     */
    Class<?> getElementType();
    
    /**
     * Returns name of the element defined on the entity we are validating.
     * 
     * @return Name of the element defined on the entity we are validating.
     */
    String getElementName();
    
    /**
     * Returns metadata of the current element we are validating.
     * 
     * @param <V> The element type.
     * @return Metadata of the current element we are validating.
     */
    <V> ElementMetaData<T, V> getElementMetaData();
    
    /**
     * Returns root entity being validated.
     * 
     * @return Root entity being validated.
     */
    Object getRootEntity();
    
    /**
     * Returns current entity we are validating.
     * 
     * @return Current entity we are validating.
     */
    T getCurrentEntity();
    
    /**
     * Returns current validation target we are validating.
     * 
     * @return Current validation target we are validating.
     */
    ValidationTarget getCurrentTarget();
    
    /**
     * Sets current validation target we are validating.
     * 
     * @param target Current validation target we are validating.
     */
    void setCurrentTarget(ValidationTarget target);
    
    /**
     * Returns current item index (or map key) we are validating.
     * 
     * @return Current item index (or map key) we are validating.
     */
    Object getCurrentIndex();
    
    /**
     * Sets current item index (or map key) we are validating.
     * 
     * @param index Current item index (or map key) we are validating.
     */
    void setCurrentIndex(Object index);
    
    // Parameters
    
    /**
     * Returns locale to be used for error messages and formatting numbers,
     * dates, etc.
     * 
     * @return Locale to be used for error messages and formatting numbers,
     *         dates, etc.
     */
    Locale getMessageLocale();
    
    /**
     * Returns filter to be used for property filtering.
     * 
     * @return Filter to be used for property filtering.
     */
    PropertyFilter getPropertyFilter();
    
    /**
     * Returns array of constraint groups to be validated.
     * 
     * @return Array of constraint groups to be validated.
     */
    String[] getValidatingGroups();
    
    /**
     * Determines if converters should take locale into account.
     * 
     * @return <code>true</code> if converters should take locale into
     *         account; <code>false</code> otherwise.
     */
    boolean isLocalizedConvert();
    
    /**
     * Determines if validation should fail on the first violation.
     * 
     * @return <code>true</code> if validation should fail on the first
     *         violation; <code>false</code> otherwise.
     */
    boolean isFailFast();
    
    /**
     * Returns default format of integer numbers for locale returned by the
     * method {@link #getMessageLocale()}. The format returned is based on the
     * pattern that was configured by the validator factory. If default pattern
     * is not configured then integer number format supported by JRE will be
     * returned.
     * 
     * @return Default format of integer numbers.
     */
    NumberFormat getIntegerFormat();
    
    /**
     * Returns format of integer numbers for the specified pattern and locale
     * returned by the method {@link #getMessageLocale()}. If the specified
     * pattern is <code>null</code> then default format will be returned.
     * 
     * @param pattern Integer number pattern.
     * @return Format of integer numbers for the specified pattern.
     * @see #getIntegerFormat()
     */
    NumberFormat getIntegerFormat(String pattern);
    
    /**
     * Returns default format of decimal numbers for locale returned by the
     * method {@link #getMessageLocale()}. The format returned is based on the
     * pattern that was configured by the validator factory. If default pattern
     * is not configured then decimal number format supported by JRE will be
     * returned.
     * 
     * @return Default format of decimal numbers.
     */
    NumberFormat getDecimalFormat();
    
    /**
     * Returns format of decimal numbers for the specified pattern and locale
     * returned by the method {@link #getMessageLocale()}. If the specified
     * pattern is <code>null</code> then default format will be returned.
     * 
     * @param pattern Decimal number pattern.
     * @return Format of decimal numbers for the specified pattern.
     * @see #getDecimalFormat()
     */
    NumberFormat getDecimalFormat(String pattern);
    
    /**
     * Returns default format of dates for locale returned by the method
     * {@link #getMessageLocale()}. The format returned is based on the pattern
     * if it was configured by the validator factory. If default pattern is not
     * configured then date format based on date and time styles will be
     * returned.
     * 
     * @return Default format of dates.
     */
    DateFormat getDateFormat();
    
    /**
     * Returns format of dates for the specified pattern and locale returned by
     * the method {@link #getMessageLocale()}. If the specified pattern is
     * <code>null</code> then default format will be returned.
     * 
     * @param pattern Date pattern.
     * @return Format of dates for the specified pattern.
     * @see #getDateFormat()
     */
    DateFormat getDateFormat(String pattern);
    
    /**
     * Returns format of dates for the specified date and time styles and
     * locale returned by the method {@link #getMessageLocale()}. If both of
     * the specified styles are negative values then default format will be
     * returned.
     * 
     * @param dateStyle Date style.
     * @param timeStyle Time style.
     * @return Format of dates for the specified date and time styles.
     * @see #getDateFormat()
     */
    DateFormat getDateFormat(int dateStyle, int timeStyle);
    
    // Message
    
    /**
     * Returns localized message for the specified key and locale returned by
     * the method {@link #getMessageLocale()}.
     * 
     * @param key Key of a message in a bundle.
     * @return Localized message for the specified key.
     */
    String resolveMessage(String key);
    
    /**
     * Builds localized error message for the specified validation component
     * and locale returned by the method {@link #getMessageLocale()}.
     * 
     * @param component Validation component.
     * @return Localized error message for the specified validation component.
     */
    String buildMessage(Validation<?> component);
    
}
