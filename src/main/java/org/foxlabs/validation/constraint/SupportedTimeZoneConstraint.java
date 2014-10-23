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
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.Collections;
import java.util.Locale;
import java.util.TimeZone;

import org.foxlabs.validation.converter.LocaleConverter;
import org.foxlabs.validation.converter.TimeZoneConverter;
import org.foxlabs.validation.resource.ResourceManager;

/**
 * This class provides <code>EnumerationConstraint</code> implementation that
 * checks whether a time zone is in list of available time zones supported by
 * JRE.
 * 
 * <p>Note that subset of supported time zones are checked by this constraint.</p>
 * 
 * @author Fox Mulder
 * @see SupportedTimeZone
 * @see ConstraintFactory#supportedTimeZone()
 */
public final class SupportedTimeZoneConstraint extends EnumerationConstraint.Default<TimeZone> {
    
    /**
     * Time zone comparator that compares time zones by raw offset.
     */
    static final Comparator<TimeZone> TIME_ZONE_COMPARATOR = new Comparator<TimeZone>() {
        public int compare(TimeZone zone1, TimeZone zone2) {
            return zone1.getRawOffset() - zone2.getRawOffset();
        }
    };
    
    /**
     * Returns set of available time zones.
     * 
     * @return Set of available time zones.
     */
    static LinkedHashSet<TimeZone> getAvailableTimeZones() {
        ArrayList<TimeZone> timeZoneList = new ArrayList<TimeZone>();
        for (String ID : TimeZone.getAvailableIDs())
            if (ResourceManager.TIME_ZONE_BUNDLE.contains(ID))
                timeZoneList.add(TimeZone.getTimeZone(ID));
        Collections.sort(timeZoneList, TIME_ZONE_COMPARATOR);
        return new LinkedHashSet<TimeZone>(timeZoneList);
    }
    
    /**
     * <code>SupportedTimeZoneConstraint</code> single instance.
     */
    public static final SupportedTimeZoneConstraint DEFAULT = new SupportedTimeZoneConstraint();
    
    /**
     * Constructs default <code>SupportedTimeZoneConstraint</code>.
     */
    private SupportedTimeZoneConstraint() {
        super(TimeZone.class, getAvailableTimeZones());
    }
    
    /**
     * Prints time zones for the locale specified as command line argument.
     * If locale is not specified in command line then default locale will be
     * used.
     * 
     * @param args Command line arguments.
     */
    public static void main(String[] args) {
        Locale locale = args.length > 0
            ? LocaleConverter.parseLocale(args[0])
            : Locale.getDefault();
        ArrayList<TimeZone> timeZoneList = new ArrayList<TimeZone>();
        for (String ID : TimeZone.getAvailableIDs())
            timeZoneList.add(TimeZone.getTimeZone(ID));
        Collections.sort(timeZoneList, TIME_ZONE_COMPARATOR);
        for (TimeZone zone : timeZoneList)
            System.out.println(zone.getID() + " = (" + TimeZoneConverter.toGMTString(zone.getRawOffset()) + ") " +
                    zone.getDisplayName(locale));
    }
    
}
