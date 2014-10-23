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

import java.util.Comparator;
import java.util.TreeMap;
import java.util.Set;
import java.util.TreeSet;
import java.util.LinkedHashSet;
import java.util.Locale;
import java.text.Collator;

/**
 * This class provides <code>EnumerationConstraint</code> implementation that
 * checks whether a locale is in list of available locales supported by JRE.
 * 
 * @author Fox Mulder
 * @see SupportedLocale
 * @see ConstraintFactory#supportedLocale()
 */
public class SupportedLocaleConstraint extends EnumerationConstraint.Default<Locale> {
    
    /**
     * <code>SupportedLocaleConstraint</code> single instance.
     */
    public static final SupportedLocaleConstraint DEFAULT = new SupportedLocaleConstraint();
    
    /**
     * Constructs default <code>SupportedLocaleConstraint</code>.
     */
    private SupportedLocaleConstraint() {
        super(Locale.class, getAvailableLocales());
    }
    
    /**
     * Returns set of constants in order according to the specified locale.
     * 
     * @param locale Locale to be used for constants sorting.
     * @return Set of constants in order according to the specified locale.
     */
    @Override
    public Set<Locale> getSortedConstants(Locale locale) {
        Set<Locale> constants = new TreeSet<Locale>(new LocaleComparator(locale));
        constants.addAll(getConstants());
        return constants;
    }
    
    /**
     * Returns set of available locales.
     * 
     * @return Set of available locales.
     */
    static LinkedHashSet<Locale> getAvailableLocales() {
        TreeMap<String, Locale> localeMap = new TreeMap<String, Locale>();
        for (Locale locale : Locale.getAvailableLocales())
            localeMap.put(locale.toString(), locale);
        return new LinkedHashSet<Locale>(localeMap.values());
    }
    
    /**
     * Locale comparator.
     * 
     * @author Fox Mulder
     */
    static final class LocaleComparator implements Comparator<Locale> {
        final Locale locale;
        final Collator collator;
        LocaleComparator(Locale locale) {
            this.locale = locale;
            this.collator = Collator.getInstance(locale);
        }
        public int compare(Locale l1, Locale l2) {
            return collator.compare(l1.getDisplayName(locale), l2.getDisplayName(locale));
        }
    }
    
}
