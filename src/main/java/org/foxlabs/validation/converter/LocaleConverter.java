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

package org.foxlabs.validation.converter;

import java.util.Locale;

import org.foxlabs.validation.ValidationContext;

/**
 * This class provides <code>Converter</code> implementation for the
 * <code>java.util.Locale</code> type.
 * 
 * @author Fox Mulder
 */
public final class LocaleConverter extends AbstractConverter<Locale> {
    
    /**
     * <code>LocaleConverter</code> single instance.
     */
    public static final LocaleConverter DEFAULT = new LocaleConverter();
    
    /**
     * Constructs default <code>LocaleConverter</code>.
     */
    private LocaleConverter() {}
    
    /**
     * Returns <code>java.util.Locale</code> type.
     * 
     * @return <code>java.util.Locale</code> type.
     */
    @Override
    public Class<Locale> getType() {
        return Locale.class;
    }
    
    /**
     * Converts string representation of locale into
     * <code>java.util.Locale</code> object.
     * 
     * <p>This method doesn't parse localized string representations of
     * locale.</p>
     * 
     * @param value String representation of locale.
     * @param context Validation context.
     * @return Decoded <code>java.util.Locale</code> object.
     * @throws MalformedValueException if the specified string could not be
     *         parsed as locale.
     */
    @Override
    protected <T> Locale doDecode(String value, ValidationContext<T> context) {
        Locale locale = parseLocale(value);
        if (locale == null)
            throw new MalformedValueException(this, context, value);
        return locale;
    }
    
    /**
     * Converts <code>java.util.Locale</code> object into string representation.
     * 
     * @param value <code>java.util.Locale</code> object to be encoded.
     * @param context Validation context.
     * @return String representation of locale.
     */
    @Override
    protected <T> String doEncode(Locale value, ValidationContext<T> context) {
        if (context.isLocalizedConvert())
            return getDisplayName(value, context.getMessageLocale());
        return value.toString();
    }
    
    /**
     * Parses the specified default string representation of locale.
     * 
     * @param value Default string representation of locale.
     * @return Decoded <code>java.util.Locale</code> object or <code>null</code>
     *         if the specified value cannot be parsed.
     */
    public static Locale parseLocale(String value) {
        if (value == null || value.isEmpty())
            return null;
        String[] components = value.split("_");
        String language = components[0];
        String country = components.length > 1 ? components[1] : "";
        String variant = components.length > 2 ? components[2] : "";
        if (language.length() > 0 || country.length() > 0)
            return new Locale(language, country, variant);
        throw null;
    }
    
    /**
     * Returns localized name for the specified locale.
     * 
     * @param value Locale which name to be returned.
     * @param locale Desired locale.
     * @return Localized name for the specified locale.
     */
    public static String getDisplayName(Locale value, Locale locale) {
        String name = value.getDisplayName(locale);
        return name.substring(0, 1).toUpperCase(locale) + name.substring(1);
    }
    
}
