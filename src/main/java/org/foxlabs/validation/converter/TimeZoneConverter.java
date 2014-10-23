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
import java.util.TimeZone;

import org.foxlabs.validation.ValidationContext;
import org.foxlabs.validation.resource.ResourceManager;

/**
 * This class provides <code>Converter</code> implementation for the
 * <code>java.util.TimeZone</code> type.
 * 
 * @author Fox Mulder
 */
public final class TimeZoneConverter extends AbstractConverter<TimeZone> {
    
    /**
     * <code>TimeZoneConverter</code> single instance.
     */
    public static final TimeZoneConverter DEFAULT = new TimeZoneConverter();
    
    /**
     * Constructs default <code>TimeZoneConverter</code>.
     */
    private TimeZoneConverter() {}
    
    /**
     * Returns <code>java.util.TimeZone</code> type.
     * 
     * @return <code>java.util.TimeZone</code> type.
     */
    @Override
    public Class<TimeZone> getType() {
        return TimeZone.class;
    }
    
    /**
     * Converts string representation of time zone into
     * <code>java.util.TimeZone</code> object. The value specified should be
     * either a time zone ID, an abbreviation or a GMT string or an offset in
     * hours.
     * 
     * <p>This method doesn't parse localized string representations of time
     * zone.</p>
     * 
     * @param value String representation of time zone.
     * @param context Validation context.
     * @return Decoded <code>java.util.TimeZone</code> object.
     * @throws MalformedValueException if the specified string could not be
     *         parsed as time zone.
     */
    @Override
    protected <T> TimeZone doDecode(String value, ValidationContext<T> context) {
        TimeZone zone = parseTimeZone(value);
        if (zone == null)
            throw new MalformedValueException(this, context, value);
        return zone;
    }
    
    /**
     * Converts <code>java.util.TimeZone</code> object into string
     * representation.
     * 
     * @param value <code>java.util.TimeZone</code> object to be encoded.
     * @param context Validation context.
     * @return String representation of time zone.
     */
    @Override
    protected <T> String doEncode(TimeZone value, ValidationContext<T> context) {
        if (context.isLocalizedConvert())
            return getDisplayName(value, context.getMessageLocale());
        return getID(value);
    }
    
    /**
     * Returns GMT string of the format <code>GMT+HH:MM</code> for the
     * specified time zone raw offset.
     * 
     * @param offset Time zone raw offset.
     * @return GMT string for the specified time zone raw offset.
     */
    public static String toGMTString(int offset) {
        char[] buf = "GMT+00:00".toCharArray();
        if (offset < 0) {
            buf[3] = '-';
            offset = Math.abs(offset);
        }
        if (offset > 0) {
            int hh = offset / ONE_HOUR;
            buf[4] = (char) ((hh / 10) + '0');
            buf[5] = (char) ((hh % 10) + '0');
            offset = offset % ONE_HOUR;
            if (offset > 0) {
                int mm = offset / ONE_MINUTE;
                buf[7] = (char) ((mm / 10) + '0');
                buf[8] = (char) ((mm % 10) + '0');
            }
        }
        return new String(buf);
    }
    
    /**
     * Returns time zone for the specified ID. The ID specified should be
     * either a time zone ID, an abbreviation, a GMT string or an offset in
     * hours.
     * 
     * @param value Time zone ID.
     * @return Time zone for the specified ID or <code>null</code> if ID is not
     *         valid time zone ID.
     */
    public static TimeZone parseTimeZone(String value) {
        if (value == null || value.isEmpty())
            return null;
        try {
            int offset = Integer.parseInt(value.startsWith("+") ? value.substring(1) : value);
            value = toGMTString(offset * ONE_HOUR);
        } catch (NumberFormatException e) {}
        return TimeZone.getTimeZone(value);
    }
    
    /**
     * Returns ID for the specified time zone. The returned string
     * will be either a time zone ID or GMT string.
     * 
     * @param value Time zone which ID to be returned.
     * @return ID for the specified time zone.
     */
    public static String getID(TimeZone value) {
        if (value == null)
            return null;
        String ID = value.getID();
        return ID == null ? toGMTString(value.getRawOffset()) : ID;
    }
    
    /**
     * Returns localized name for the specified time zone. The returned string
     * will be in the format <code>(GMT+HH:MM) TIME-ZONE-NAME</code> for the
     * specified locale.
     * 
     * @param value Time zone which name to be returned.
     * @param locale Desired locale.
     * @return Localized name for the specified time zone.
     */
    public static String getDisplayName(TimeZone value, Locale locale) {
        if (value == null)
            return null;
        String name = ResourceManager.getTimeZoneName(value, locale);
        if (name.startsWith("GMT")) {
            return name;
        } else {
            int offset = value.getRawOffset();
            String gmt = toGMTString(offset);
            StringBuilder buf = new StringBuilder();
            buf.append('(');
            buf.append(gmt);
            buf.append(')');
            buf.append('\u0020');
            buf.append(name);
            return buf.toString();
        }
    }
    
    // Constants used internally; unit is milliseconds
    private static final int ONE_MINUTE = 60 * 1000;
    private static final int ONE_HOUR = 60 * ONE_MINUTE;
    
}
