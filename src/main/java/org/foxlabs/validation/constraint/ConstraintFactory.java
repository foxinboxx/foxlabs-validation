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

import java.lang.annotation.Annotation;

import java.lang.reflect.Type;

import java.util.Comparator;
import java.util.Collection;
import java.util.List;
import java.util.LinkedList;
import java.util.Set;
import java.util.HashSet;
import java.util.Map;
import java.util.Collections;
import java.util.logging.Level;

import org.foxlabs.validation.ValidationDefaults;
import org.foxlabs.validation.ValidationTarget;
import org.foxlabs.validation.ValidationTargetException;
import org.foxlabs.validation.ValidationTypeException;
import org.foxlabs.validation.metadata.EntityMetaData;
import org.foxlabs.validation.support.AnnotationSupport;

import static org.foxlabs.validation.ValidationTarget.*;

import org.foxlabs.util.UnicodeSet;
import org.foxlabs.util.reflect.Types;

/**
 * Defines a factory that allows to obtain <code>Constraint</code> instances.
 *
 * <p><code>ConstraintFactory</code> is thread-safe.</p>
 *
 * @author Fox Mulder
 * @see Constraint
 */
@SuppressWarnings({"unchecked", "rawtypes"})
public abstract class ConstraintFactory extends AnnotationSupport {

    // Can't be inherited.
    private ConstraintFactory() {}

    // Fill annotation cache.
    static {
        addToCache(IsNull.class, IsNullConstraint.DEFAULT);
        addToCache(IsTrue.class, IsTrueConstraint.DEFAULT);
        addToCache(IsFalse.class, IsFalseConstraint.DEFAULT);

        addToCache(NotNull.class, NotNullConstraint.DEFAULT);
        addToCache(NotBlank.class, NotBlankConstraint.DEFAULT);

        addToCache(NotEmpty.class, NotEmptyConstraint.StringType.DEFAULT);
        addToCache(NotEmpty.class, NotEmptyConstraint.ArrayType.DEFAULT);
        addToCache(NotEmpty.class, NotEmptyConstraint.CollectionType.DEFAULT);
        addToCache(NotEmpty.class, NotEmptyConstraint.MapType.DEFAULT);

        addToCache(PastDate.class, PastDateConstraint.DEFAULT);
        addToCache(FutureDate.class, FutureDateConstraint.DEFAULT);
        addToCache(Identifier.class, IdentifierConstraint.DEFAULT);
        addToCache(MimeType.class, MimeTypeConstraint.DEFAULT);
        addToCache(Hostname.class, HostnameConstraint.DEFAULT);
        addToCache(Ip4Address.class, Ip4AddressConstraint.DEFAULT);
        addToCache(Ip6Address.class, Ip6AddressConstraint.DEFAULT);
        addToCache(IpAddress.class, IpAddressConstraint.DEFAULT);
        addToCache(InetAddress.class, InetAddressConstraint.DEFAULT);
        addToCache(EmailAddress.class, EmailAddressConstraint.DEFAULT);
        addToCache(SupportedEncoding.class, SupportedEncodingConstraint.DEFAULT);
        addToCache(SupportedImageMime.class, SupportedImageMimeConstraint.DEFAULT);
        addToCache(SupportedLocale.class, SupportedLocaleConstraint.DEFAULT);
        addToCache(SupportedTimeZone.class, SupportedTimeZoneConstraint.DEFAULT);

        addToCache(SetNull.class, SetNullConstraint.DEFAULT);
        addToCache(Nullify.class, NullifyConstraint.DEFAULT);
        addToCache(Trim.class, TrimConstraint.DEFAULT);
        addToCache(Despace.class, DespaceConstraint.DEFAULT);
        addToCache(UpperCase.class, UpperCaseConstraint.DEFAULT);
        addToCache(LowerCase.class, LowerCaseConstraint.DEFAULT);
        addToCache(Capitalize.class, CapitalizeConstraint.DEFAULT);
        addToCache(CapitalizeAll.class, CapitalizeAllConstraint.DEFAULT);
        addToCache(Sysdate.class, SysdateConstraint.DEFAULT);

        addToCache(RemoveNullElements.class, RemoveNullElementsConstraint.ArrayType.DEFAULT);
        addToCache(RemoveNullElements.class, RemoveNullElementsConstraint.CollectionType.DEFAULT);
    }

    // IsXXX

    /**
     * Returns constraint that checks whether a value is <code>null</code>.
     *
     * @param <V> The value type.
     * @return Constraint that checks whether a value is <code>null</code>.
     * @see IsNullConstraint
     */
    public static <V> Constraint<? super V> isNull() {
        return IsNullConstraint.DEFAULT;
    }

    /**
     * Returns constraint that checks whether a boolean value is
     * <code>true</code>.
     *
     * @return Constraint that checks whether a boolean value is
     *         <code>true</code>.
     * @see IsTrueConstraint
     */
    public static Constraint<Boolean> isTrue() {
        return IsTrueConstraint.DEFAULT;
    }

    /**
     * Returns constraint that checks whether a boolean value is
     * <code>false</code>.
     *
     * @return Constraint that checks whether a boolean value is
     *         <code>false</code>.
     * @see IsFalseConstraint
     */
    public static Constraint<Boolean> isFalse() {
        return IsFalseConstraint.DEFAULT;
    }

    // NotXXX

    /**
     * Returns constraint that checks whether a value is not <code>null</code>.
     *
     * @param <V> The value type.
     * @return Constraint that checks whether a value is not <code>null</code>.
     * @see NotNullConstraint
     */
    public static <V> Constraint<? super V> notNull() {
        return NotNullConstraint.DEFAULT;
    }

    /**
     * Returns constraint that checks whether a string is not <code>null</code>
     * or empty.
     *
     * @return Constraint that checks whether a string is not <code>null</code>
     *         or empty.
     * @see NotBlankConstraint
     */
    public static Constraint<String> notBlank() {
        return NotBlankConstraint.DEFAULT;
    }

    /**
     * Returns constraint that checks whether a string is not empty.
     *
     * @return Constraint that checks whether a string is not empty.
     * @see NotEmptyConstraint.StringType
     */
    public static Constraint<String> stringNotEmpty() {
        return NotEmptyConstraint.StringType.DEFAULT;
    }

    /**
     * Returns constraint that checks whether an array is not empty.
     *
     * @param <V> The array type.
     * @return Constraint that checks whether an array is not empty.
     * @see NotEmptyConstraint.ArrayType
     */
    public static <V> Constraint<V> arrayNotEmpty() {
        return (Constraint<V>) NotEmptyConstraint.ArrayType.DEFAULT;
    }

    /**
     * Returns constraint that checks whether a collection is not empty.
     *
     * @param <V> The collection type.
     * @return Constraint that checks whether a collection is not empty.
     * @see NotEmptyConstraint.CollectionType
     */
    public static <V extends Collection<?>> Constraint<V> collectionNotEmpty() {
        return (Constraint<V>) NotEmptyConstraint.CollectionType.DEFAULT;
    }

    /**
     * Returns constraint that checks whether a map is not empty.
     *
     * @param <V> The map type.
     * @return Constraint that checks whether a map is not empty.
     * @see NotEmptyConstraint.MapType
     */
    public static <V extends Map<?, ?>> Constraint<V> mapNotEmpty() {
        return (Constraint<V>) NotEmptyConstraint.MapType.DEFAULT;
    }

    // Size

    /**
     * Returns constraint that checks whether the length of a string is not
     * less than allowed minimum length.
     *
     * @param min Minimum length of a string.
     * @return Constraint that checks whether the length of a string is not
     *         less than allowed minimum length.
     * @see #stringSize(int, int)
     * @see SizeConstraint.StringType
     */
    public static Constraint<String> stringMinSize(int min) {
        return stringSize(min, Integer.MAX_VALUE);
    }

    /**
     * Returns constraint that checks whether the length of a string is not
     * greater than allowed maximum length.
     *
     * @param max Maximum length of a string.
     * @return Constraint that checks whether the length of a string is not
     *         greater than allowed maximum length.
     * @see #stringSize(int, int)
     * @see SizeConstraint.StringType
     */
    public static Constraint<String> stringMaxSize(int max) {
        return stringSize(0, max);
    }

    /**
     * Returns constraint that checks whether the length of a string is within
     * allowed minimum and maximum bounds.
     *
     * @param min Minimum length of a string.
     * @param max Maximum length of a string.
     * @return Constraint that checks whether the length of a string is within
     *         allowed minimum and maximum bounds.
     * @see SizeConstraint.StringType
     */
    public static Constraint<String> stringSize(int min, int max) {
        return new SizeConstraint.StringType(min, max);
    }

    /**
     * Returns constraint that checks whether the length of an array is not
     * less than allowed minimum length.
     *
     * @param <V> The array type.
     * @param min Minimum length of an array.
     * @return Constraint that checks whether the length of an array is not
     *         less than allowed minimum length.
     * @see #arraySize(int, int)
     * @see SizeConstraint.ArrayType
     */
    public static <V> Constraint<V> arrayMinSize(int min) {
        return arraySize(min, Integer.MAX_VALUE);
    }

    /**
     * Returns constraint that checks whether the length of an array is not
     * greater than allowed maximum length.
     *
     * @param <V> The array type.
     * @param max Maximum length of an array.
     * @return Constraint that checks whether the length of an array is not
     *         greater than allowed maximum length.
     * @see #arraySize(int, int)
     * @see SizeConstraint.ArrayType
     */
    public static <V> Constraint<V> arrayMaxSize(int max) {
        return arraySize(0, max);
    }

    /**
     * Returns constraint that checks whether the length of an array is within
     * allowed minimum and maximum bounds.
     *
     * @param <V> The array type.
     * @param min Minimum length of an array.
     * @param max Maximum length of an array.
     * @return Constraint that checks whether the length of an array is within
     *         allowed minimum and maximum bounds.
     * @see SizeConstraint.ArrayType
     */
    public static <V> Constraint<V> arraySize(int min, int max) {
        return (Constraint<V>) new SizeConstraint.ArrayType(min, max);
    }

    /**
     * Returns constraint that checks whether the size of a collection is not
     * less than allowed minimum size.
     *
     * @param <V> The collection type.
     * @param min Minimum size of a collection.
     * @return Constraint that checks whether the size of a collection is not
     *         less than allowed minimum size.
     * @see #collectionSize(int, int)
     * @see SizeConstraint.CollectionType
     */
    public static <V extends Collection<?>> Constraint<V> collectionMinSize(int min) {
        return collectionSize(min, Integer.MAX_VALUE);
    }

    /**
     * Returns constraint that checks whether the size of a collection is not
     * greater than allowed maximum size.
     *
     * @param <V> The collection type.
     * @param max Maximum size of a collection.
     * @return Constraint that checks whether the size of a collection is not
     *         greater than allowed maximum size.
     * @see #collectionSize(int, int)
     * @see SizeConstraint.CollectionType
     */
    public static <V extends Collection<?>> Constraint<V> collectionMaxSize(int max) {
        return collectionSize(0, max);
    }

    /**
     * Returns constraint that checks whether the size of a collection is
     * within allowed minimum and maximum bounds.
     *
     * @param <V> The collection type.
     * @param min Minimum size of a collection.
     * @param max Maximum size of a collection.
     * @return Constraint that checks whether the size of a collection is
     *         within allowed minimum and maximum bounds.
     * @see SizeConstraint.CollectionType
     */
    public static <V extends Collection<?>> Constraint<V> collectionSize(int min, int max) {
        return (Constraint<V>) new SizeConstraint.CollectionType(min, max);
    }

    /**
     * Returns constraint that checks whether the size of a map is not less
     * than allowed minimum size.
     *
     * @param <V> The map type.
     * @param min Minimum size of a map.
     * @return Constraint that checks whether the size of a map is not less
     *         than allowed minimum size.
     * @see #mapSize(int, int)
     * @see SizeConstraint.MapType
     */
    public static <V extends Map<?, ?>> Constraint<V> mapMinSize(int min) {
        return mapSize(min, Integer.MAX_VALUE);
    }

    /**
     * Returns constraint that checks whether the size of a map is not greater
     * than allowed maximum size.
     *
     * @param <V> The map type.
     * @param max Maximum size of a map.
     * @return Constraint that checks whether the size of a map is not greater
     *         than allowed maximum size.
     * @see #mapSize(int, int)
     * @see SizeConstraint.MapType
     */
    public static <V extends Map<?, ?>> Constraint<V> mapMaxSize(int max) {
        return mapSize(0, max);
    }

    /**
     * Returns constraint that checks whether the size of a map is within
     * allowed minimum and maximum bounds.
     *
     * @param <V> The map type.
     * @param min Minimum size of a map.
     * @param max Maximum size of a map.
     * @return Constraint that checks whether the size of a map is within
     *         allowed minimum and maximum bounds.
     * @see SizeConstraint.MapType
     */
    public static <V extends Map<?, ?>> Constraint<V> mapSize(int min, int max) {
        return (Constraint<V>) new SizeConstraint.MapType(min, max);
    }

    // Range

    /**
     * Returns constraint that checks whether a value is not less than
     * allowed minimum value.
     *
     * @param <V> The value type.
     * @param min Minimum value.
     * @return Constraint that checks whether a value is not less than
     *         allowed minimum value.
     * @see #range(Object, Object, Comparator)
     * @see RangeConstraint
     */
    public static <V extends Comparable<? super V>> Constraint<V> min(V min) {
        return range(min, null, (Comparator<V>) DefaultComparator.INSTANCE);
    }

    /**
     * Returns constraint that checks whether a value is not less than
     * allowed minimum value.
     *
     * @param <V> The value type.
     * @param min Minimum value.
     * @param comparator Comparator to be used for range checking.
     * @return Constraint that checks whether a value is not less than
     *         allowed minimum value.
     * @see #range(Object, Object, Comparator)
     * @see RangeConstraint
     */
    public static <V> Constraint<V> min(V min, Comparator<V> comparator) {
        return range(min, null, comparator);
    }

    /**
     * Returns constraint that checks whether a value is not greater than
     * allowed maximum value.
     *
     * @param <V> The value type.
     * @param max Maximum value.
     * @return Constraint that checks whether a value is not greater than
     *         allowed maximum value.
     * @see #range(Object, Object, Comparator)
     * @see RangeConstraint
     */
    public static <V extends Comparable<? super V>> Constraint<V> max(V max) {
        return range(null, max, (Comparator<V>) DefaultComparator.INSTANCE);
    }

    /**
     * Returns constraint that checks whether a value is not greater than
     * allowed maximum value.
     *
     * @param <V> The value type.
     * @param max Maximum value.
     * @param comparator Comparator to be used for range checking.
     * @return Constraint that checks whether a value is not greater than
     *         allowed maximum value.
     * @see #range(Object, Object, Comparator)
     * @see RangeConstraint
     */
    public static <V> Constraint<V> max(V max, Comparator<V> comparator) {
        return range(null, max, comparator);
    }

    /**
     * Returns constraint that checks whether a value is within allowed
     * minimum and maximum range.
     *
     * @param <V> The value type.
     * @param min Minimum value.
     * @param max Maximum value.
     * @return Constraint that checks whether a value is within allowed
     *         minimum and maximum range.
     * @see #range(Object, Object, Comparator)
     * @see RangeConstraint
     */
    public static <V extends Comparable<? super V>> Constraint<V> range(V min, V max) {
        return range(min, max, (Comparator<V>) DefaultComparator.INSTANCE);
    }

    /**
     * Returns constraint that checks whether a value is within allowed
     * minimum and maximum range.
     *
     * @param <V> The value type.
     * @param min Minimum value.
     * @param max Maximum value.
     * @param comparator Comparator to be used for range checking.
     * @return Constraint that checks whether a value is within allowed
     *         minimum and maximum range.
     * @see RangeConstraint
     */
    public static <V> Constraint<V> range(V min, V max, Comparator<V> comparator) {
        return new RangeConstraint<V>(min, max, comparator);
    }

    // Enumeration

    /**
     * Returns constraint that lists constants of the specified enumeration
     * type.
     *
     * @param <V> The enumeration type.
     * @param type Enumeration type.
     * @return Constraint that lists constants of the specified enumeration
     *         type.
     * @see #enumeration(Object...)
     * @see EnumerationConstraint.Default
     */
    public static <V extends Enum<V>> Constraint<V> enumeration(Class<V> type) {
        return enumeration(type.getEnumConstants());
    }

    /**
     * Returns constraint that checks whether a value is one of the allowed
     * constants.
     *
     * @param <V> The constants type.
     * @param constants Array of allowed constants.
     * @return Constraint that checks whether a value is one of the allowed
     *         constants.
     * @see EnumerationConstraint.Default
     */
    public static <V> Constraint<V> enumeration(V... constants) {
        return new EnumerationConstraint.Default<V>(constants);
    }

    /**
     * Returns constraint that checks whether a value is one of the allowed
     * constants.
     *
     * @param <V> The constants type.
     * @param type Type of enumeration constants.
     * @param constants Array of allowed constants.
     * @return Constraint that checks whether a value is one of the allowed
     *         constants.
     * @see EnumerationConstraint.Default
     */
    public static <V> Constraint<V> enumeration(Class<V> type, V... constants) {
        return new EnumerationConstraint.Default<V>(type, constants);
    }

    /**
     * Returns constraint that checks whether a value is one of the allowed
     * constants.
     *
     * @param <V> The constants type.
     * @param constants Collection of allowed constants.
     * @return Constraint that checks whether a value is one of the allowed
     *         constants.
     * @see EnumerationConstraint.Default
     */
    public static <V> Constraint<V> enumeration(Collection<V> constants) {
        return new EnumerationConstraint.Default<V>(constants);
    }

    /**
     * Returns constraint that checks whether a value is one of the allowed
     * constants.
     *
     * @param <V> The constants type.
     * @param type Type of enumeration constants.
     * @param constants Collection of allowed constants.
     * @return Constraint that checks whether a value is one of the allowed
     *         constants.
     * @see EnumerationConstraint.Default
     */
    public static <V> Constraint<V> enumeration(Class<V> type, Collection<V> constants) {
        return new EnumerationConstraint.Default<V>(type, constants);
    }

    /**
     * Returns constraint that checks whether a string is one of the allowed
     * strings ignoring case considerations.
     *
     * @param constants Array of allowed strings.
     * @return Constraint that checks whether a string is one of the allowed
     *         strings ignoring case considerations.
     * @see IgnoreCaseEnumerationConstraint
     */
    public static Constraint<String> ignoreCaseEnumeration(String... constants) {
        return new IgnoreCaseEnumerationConstraint(constants);
    }

    /**
     * Returns constraint that checks whether a string is one of the allowed
     * strings ignoring case considerations.
     *
     * @param constants Collection of allowed strings.
     * @return Constraint that checks whether a string is one of the allowed
     *         strings ignoring case considerations.
     * @see IgnoreCaseEnumerationConstraint
     */
    public static Constraint<String> ignoreCaseEnumeration(Collection<String> constants) {
        return new IgnoreCaseEnumerationConstraint(constants);
    }

    /**
     * Returns constraint that checks whether a locale is in list of available
     * locales supported by JRE.
     *
     * @return Constraint that checks whether a locale is in list of available
     *         locales supported by JRE.
     * @see SupportedLocaleConstraint
     */
    public static Constraint<java.util.Locale> supportedLocale() {
        return SupportedLocaleConstraint.DEFAULT;
    }

    /**
     * Returns constraint that checks whether a time zone is in list of
     * available time zones supported by JRE.
     *
     * @return Constraint that checks whether a time zone is in list of
     *         available time zones supported by JRE.
     * @see SupportedTimeZoneConstraint
     */
    public static Constraint<java.util.TimeZone> supportedTimeZone() {
        return SupportedTimeZoneConstraint.DEFAULT;
    }

    /**
     * Returns constraint that checks whether a string is in list of available
     * charset names supported by JRE.
     *
     * @return Constraint that checks whether a string is in list of available
     *         charset names supported by JRE.
     * @see SupportedEncodingConstraint
     */
    public static Constraint<String> supportedEncoding() {
        return SupportedEncodingConstraint.DEFAULT;
    }

    /**
     * Returns constraint that checks whether a string is in list of available
     * image MIME types supported by <code>javax.imageio.ImageIO</code>.
     *
     * @return Constraint that checks whether a string is in list of available
     *         image MIME types supported by <code>javax.imageio.ImageIO</code>.
     * @see SupportedImageMimeConstraint
     */
    public static Constraint<String> supportedImageMime() {
        return SupportedImageMimeConstraint.DEFAULT;
    }

    /**
     * Returns constraint that checks whether a log level is in list of known
     * log levels.
     *
     * @return Constraint that checks whether a log level is in list of known
     *         log levels.
     * @see LogLevelConstraint
     */
    public static Constraint<Level> logLevel() {
        return LogLevelConstraint.DEFAULT;
    }

    // Regex

    /**
     * Returns constraint that checks whether a string matches the regular
     * expression.
     *
     * @param pattern Regular expression pattern.
     * @return Constraint that checks whether a string matches the regular
     *         expression.
     * @see #regex(String, int)
     * @see RegexConstraint
     */
    public static Constraint<String> regex(String pattern) {
        return regex(pattern, 0);
    }

    /**
     * Returns constraint that checks whether a string matches the regular
     * expression.
     *
     * @param pattern Regular expression pattern.
     * @param flags Match flags.
     * @return Constraint that checks whether a string matches the regular
     *         expression.
     * @see RegexConstraint
     */
    public static Constraint<String> regex(String pattern, int flags) {
        return new RegexConstraint(pattern, flags);
    }

    /**
     * Returns constraint that checks whether a string is valid mime type
     * representation.
     *
     * @return Constraint that checks whether a string is valid mime type
     *         representation.
     * @see MimeTypeConstraint
     */
    public static Constraint<String> mimetype() {
        return MimeTypeConstraint.DEFAULT;
    }

    // Misc

    /**
     * Returns constraint that just returns value unchanged.
     *
     * @param <V> The value type.
     * @return Constraint that just returns value unchanged.
     * @see IdentityConstraint
     */
    public static <V> Constraint<? super V> identity() {
        return IdentityConstraint.DEFAULT;
    }

    /**
     * Returns constraint that checks whether a value is instance of one of the
     * specified types.
     *
     * @param <V> The value type.
     * @param types The types to check.
     * @return Constraint that checks whether a value is instance of one of the
     *         specified types.
     * @see InstanceOfConstraint
     */
    public static <V> Constraint<? super V> instanceOf(Class<?>... types) {
        return new InstanceOfConstraint(types);
    }

    /**
     * Returns constraint that checks whether a date is date in the past.
     *
     * @return Constraint that checks whether a date is date in the past.
     * @see PastDateConstraint
     */
    public static Constraint<java.util.Date> pastDate() {
        return PastDateConstraint.DEFAULT;
    }

    /**
     * Returns constraint that checks whether a date is date in the future.
     *
     * @return Constraint that checks whether a date is date in the future.
     * @see FutureDateConstraint
     */
    public static Constraint<java.util.Date> futureDate() {
        return FutureDateConstraint.DEFAULT;
    }

    /**
     * Returns constraint that checks whether a string contains allowed
     * characters only.
     *
     * @param pattern Allowed characters pattern.
     * @return Constraint that checks whether a string contains allowed
     *         characters only.
     * @see #legalCharset(UnicodeSet)
     * @see LegalCharsetConstraint
     * @see UnicodeSet#fromPattern(String)
     */
    public static Constraint<String> legalCharset(String pattern) {
        return legalCharset(UnicodeSet.fromPattern(pattern));
    }

    /**
     * Returns constraint that checks whether a string contains allowed
     * characters only.
     *
     * @param charset Allowed character set.
     * @return Constraint that checks whether a string contains allowed
     *         characters only.
     * @see LegalCharsetConstraint
     */
    public static Constraint<String> legalCharset(UnicodeSet charset) {
        return new LegalCharsetConstraint(charset);
    }

    /**
     * Returns constraint that checks whether a string not contains disallowed
     * characters.
     *
     * @param pattern Disallowed characters pattern.
     * @return Constraint that checks whether a string not contains disallowed
     *         characters.
     * @see #illegalCharset(UnicodeSet)
     * @see IllegalCharsetConstraint
     * @see UnicodeSet#fromPattern(String)
     */
    public static Constraint<String> illegalCharset(String pattern) {
        return illegalCharset(UnicodeSet.fromPattern(pattern));
    }

    /**
     * Returns constraint that checks whether a string not contains disallowed
     * characters.
     *
     * @param charset Disallowed character set.
     * @return Constraint that checks whether a string not contains disallowed
     *         characters.
     * @see IllegalCharsetConstraint
     */
    public static Constraint<String> illegalCharset(UnicodeSet charset) {
        return new IllegalCharsetConstraint(charset);
    }

    /**
     * Returns constraint that checks whether a string is valid identifier.
     *
     * @return Constraint that checks whether a string is valid identifier.
     * @see IdentifierConstraint
     */
    public static Constraint<String> identifier() {
        return IdentifierConstraint.DEFAULT;
    }

    /**
     * Returns constraint that checks whether a string is valid internet
     * hostname.
     *
     * @return Constraint that checks whether a string is valid internet
     *         hostname.
     * @see HostnameConstraint
     */
    public static Constraint<String> hostname() {
        return HostnameConstraint.DEFAULT;
    }

    /**
     * Returns constraint that checks whether a string is valid IPv4 address.
     *
     * @return Constraint that checks whether a string is valid IPv4 address.
     * @see Ip4AddressConstraint
     */
    public static Constraint<String> ip4Address() {
        return Ip4AddressConstraint.DEFAULT;
    }

    /**
     * Returns constraint that checks whether a string is valid IPv6 address.
     *
     * @return Constraint that checks whether a string is valid IPv6 address.
     * @see Ip6AddressConstraint
     */
    public static Constraint<String> ip6Address() {
        return Ip6AddressConstraint.DEFAULT;
    }

    /**
     * Returns constraint that checks whether a string is valid IPv4 or IPv6
     * address.
     *
     * @return Constraint that checks whether a string is valid IPv4 or IPv6
     *         address.
     * @see IpAddressConstraint
     */
    public static Constraint<String> ipAddress() {
        return IpAddressConstraint.DEFAULT;
    }

    /**
     * Returns constraint that checks whether a string is valid internet
     * address (can be either hostname or IP address).
     *
     * @return Constraint that checks whether a string is valid internet
     *         address (can be either hostname or IP address).
     * @see InetAddressConstraint
     */
    public static Constraint<String> inetAddress() {
        return InetAddressConstraint.DEFAULT;
    }

    /**
     * Returns constraint that checks whether a string is valid email address.
     *
     * @return Constraint that checks whether a string is valid email address.
     * @see EmailAddressConstraint
     */
    public static Constraint<String> emailAddress() {
        return EmailAddressConstraint.DEFAULT;
    }

    /**
     * Returns constraint that checks whether a string is valid URL reference.
     *
     * @return Constraint that checks whether a string is valid URL reference.
     * @see UrlAddressConstraint
     */
    public static Constraint<String> urlAddress() {
        return UrlAddressConstraint.DEFAULT;
    }

    /**
     * Returns constraint that checks whether a string is valid URL reference
     * with allowed prefix and protocol.
     *
     * @param prefix URL prefix pattern if any.
     * @param protocols Array of allowed protocols.
     * @return Constraint that checks whether a string is valid URL reference
     *         with allowed prefix and protocol.
     * @see UrlAddressConstraint
     */
    public static Constraint<String> urlAddress(String prefix, String... protocols) {
        return (prefix == null || prefix.isEmpty()) && (protocols == null || protocols.length == 0)
            ? UrlAddressConstraint.DEFAULT
            : new UrlAddressConstraint(prefix, protocols);
    }

    /**
     * Returns constraint that checks whether a string is valid URI reference.
     *
     * @return Constraint that checks whether a string is valid URI reference.
     * @see UriAddressConstraint
     */
    public static Constraint<String> uriAddress() {
        return UriAddressConstraint.DEFAULT;
    }

    /**
     * Returns constraint that checks whether a string is valid URI reference
     * with allowed scheme.
     *
     * @param schemes Array of allowed schemes.
     * @return Constraint that checks whether a string is valid URI reference
     *         with allowed scheme.
     * @see UriAddressConstraint
     */
    public static Constraint<String> uriAddress(String... schemes) {
        return schemes == null || schemes.length == 0
            ? UriAddressConstraint.DEFAULT
            : new UriAddressConstraint(schemes);
    }

    /**
     * Returns constraint that checks existence of a file.
     *
     * @return Constraint that checks existence of a file.
     * @see FileExistsConstraint
     */
    public static Constraint<java.io.File> fileExists() {
        return FileExistsConstraint.FILE;
    }

    /**
     * Returns constraint that checks existence of a directory.
     *
     * @return Constraint that checks existence of a directory.
     * @see FileExistsConstraint
     */
    public static Constraint<java.io.File> directoryExists() {
        return FileExistsConstraint.DIRECTORY;
    }

    /**
     * Returns constraint that checks whether size of a file is within allowed
     * minimum and maximum bounds.
     *
     * @param min Minimum allowed file size.
     * @param max Maximum allowed file size.
     * @return Constraint that checks whether size of a file is within allowed
     *         minimum and maximum bounds.
     * @see FileSizeConstraint
     */
    public static Constraint<java.io.File> fileSize(long min, long max) {
        return new FileSizeConstraint(min, max);
    }

    /**
     * Returns constraint that checks whether the size of an image is within
     * maximum bounds.
     *
     * @param maxWidth Maximum image width.
     * @param maxHeight Maximum image height.
     * @param adjust Image size adjust mode.
     * @return Constraint that checks whether the size of an image is within
     *         maximum bounds.
     * @see ImageSizeConstraint.AwtCodec
     */
    public static Constraint<java.awt.Image> awtImageSize(int maxWidth, int maxHeight,
            ImageAdjust adjust) {
        return new ImageSizeConstraint.AwtCodec(maxWidth, maxHeight, adjust);
    }

    /**
     * Returns constraint that checks whether the size of a raw image is within
     * maximum bounds.
     *
     * @param maxWidth Maximum image width.
     * @param maxHeight Maximum image height.
     * @param adjust Image size adjust mode.
     * @return Constraint that checks whether the size of a raw image is within
     *         maximum bounds.
     * @see ImageSizeConstraint.RawCodec
     */
    public static Constraint<byte[]> rawImageSize(int maxWidth, int maxHeight,
            ImageAdjust adjust) {
        return new ImageSizeConstraint.RawCodec(maxWidth, maxHeight, adjust);
    }

    // Corrector

    /**
     * Returns constraint that replaces a value with <code>null</code>.
     *
     * @param <V> The value type.
     * @return Constraint that replaces a value with <code>null</code>.
     * @see SetNullConstraint
     */
    public static <V> Constraint<V> setNull() {
        return (Constraint<V>) SetNullConstraint.DEFAULT;
    }

    /**
     * Returns constraint that replaces an empty string with <code>null</code>.
     *
     * @return Constraint that replaces an empty string with <code>null</code>.
     * @see NullifyConstraint
     */
    public static Constraint<String> nullify() {
        return NullifyConstraint.DEFAULT;
    }

    /**
     * Returns constraint that removes leading and trailing whitespaces from a
     * string.
     *
     * @return Constraint that removes leading and trailing whitespaces from a
     *         string.
     * @see TrimConstraint
     */
    public static Constraint<String> trim() {
        return TrimConstraint.DEFAULT;
    }

    /**
     * Returns constraint that removes all whitespaces from a string.
     *
     * @return Constraint that removes all whitespaces from a string.
     * @see DespaceConstraint
     */
    public static Constraint<String> despace() {
        return DespaceConstraint.DEFAULT;
    }

    /**
     * Returns constraint that replaces all whitespaces with <code>' '</code>
     * character in a string.
     *
     * @return Constraint that replaces all whitespaces with <code>' '</code>
     *         character in a string.
     * @see CoalesceConstraint
     */
    public static Constraint<String> coalesce() {
        return CoalesceConstraint.DEFAULT;
    }

    /**
     * Returns constraint that replaces all whitespaces with single character
     * in a string.
     *
     * @param replacement Whitespaces replacement character.
     * @param multiline Determines if new line characters should be preserved.
     * @return Constraint that replaces all whitespaces with single character
     *         in a string.
     * @see CoalesceConstraint
     */
    public static Constraint<String> coalesce(char replacement, boolean multiline) {
        return !multiline && replacement == '\u0020'
            ? CoalesceConstraint.DEFAULT
            : new CoalesceConstraint(replacement, multiline);
    }

    /**
     * Returns constraint that converts a string to upper case.
     *
     * @return Constraint that converts a string to upper case.
     * @see UpperCaseConstraint
     */
    public static Constraint<String> upperCase() {
        return UpperCaseConstraint.DEFAULT;
    }

    /**
     * Returns constraint that converts a string to lower case.
     *
     * @return Constraint that converts a string to lower case.
     * @see LowerCaseConstraint
     */
    public static Constraint<String> lowerCase() {
        return LowerCaseConstraint.DEFAULT;
    }

    /**
     * Returns constraint that converts to upper case first letter of first
     * word in a string.
     *
     * @return Constraint that converts to upper case first letter of first
     *         word in a string.
     * @see CapitalizeConstraint
     */
    public static Constraint<String> capitalize() {
        return CapitalizeConstraint.DEFAULT;
    }

    /**
     * Returns constraint that converts to upper case first letters of all
     * words in a string.
     *
     * @return Constraint that converts to upper case first letters of all
     *         words in a string.
     * @see CapitalizeAllConstraint
     */
    public static Constraint<String> capitalizeAll() {
        return CapitalizeAllConstraint.DEFAULT;
    }

    /**
     * Returns constraint that replaces each substring of a string that matches
     * the given regular expression with the given replacement.
     *
     * @param regex Regular expression pattern.
     * @param replacement String to be substituted for each match.
     * @return Constraint that replaces each substring of a string that matches
     *         the given regular expression with the given replacement.
     * @see #replace(String, String, int)
     * @see ReplaceConstraint
     */
    public static Constraint<String> replace(String regex, String replacement) {
        return replace(regex, replacement, 0);
    }

    /**
     * Returns constraint that replaces each substring of a string that matches
     * the given regular expression with the given replacement.
     *
     * @param regex Regular expression pattern.
     * @param replacement String to be substituted for each match.
     * @param flags Match flags.
     * @return Constraint that replaces each substring of a string that matches
     *         the given regular expression with the given replacement.
     * @see ReplaceConstraint
     */
    public static Constraint<String> replace(String regex, String replacement, int flags) {
        return new ReplaceConstraint(regex, replacement, flags);
    }

    /**
     * Returns constraint that allows to cut length of a string.
     *
     * @param limit Maximum string length.
     * @return Constraint that allows to cut length of a string.
     * @see MaxLengthConstraint
     */
    public static Constraint<String> maxLength(int limit) {
        return new MaxLengthConstraint(limit);
    }

    /**
     * Returns constraint that replaces a <code>null</code> date with current
     * system date.
     *
     * @return Constraint that replaces a <code>null</code> date with current
     *         system date.
     * @see SysdateConstraint
     */
    public static Constraint<java.util.Date> sysdate() {
        return SysdateConstraint.DEFAULT;
    }

    /**
     * Returns constraint that replaces a <code>null</code> value with default
     * value.
     *
     * @param <V> The value type.
     * @param defaultValue Default value.
     * @return Constraint that replaces a <code>null</code> value with default
     *         value.
     * @see DefaultValueConstraint
     */
    public static <V> Constraint<V> defaultValue(V defaultValue) {
        return new DefaultValueConstraint<V>(defaultValue);
    }

    /**
     * Returns constraint that removes all <code>null</code> elements from an
     * array.
     *
     * @param <V> The array type.
     * @return Constraint that removes all <code>null</code> elements from an
     *         array.
     */
    public static <V> Constraint<V> arrayRemoveNullElements() {
        return (Constraint<V>) RemoveNullElementsConstraint.ArrayType.DEFAULT;
    }

    /**
     * Returns constraint that removes all <code>null</code> elements from a
     * collection.
     *
     * @param <V> The collection type.
     * @return Constraint that removes all <code>null</code> elements from a
     *         collection.
     */
    public static <V extends Collection<?>> Constraint<V> collectionRemoveNullElements() {
        return (Constraint<V>) RemoveNullElementsConstraint.CollectionType.DEFAULT;
    }

    // Property

    /**
     * Returns constraint that applies <code>==</code> operator to a value
     * and the specified property.
     *
     * @param <V> The property value type.
     * @param type Type of the operands.
     * @param property Property name of the second operand.
     * @return Constraint that applies <code>==</code> operator to a value
     *         and the specified property.
     * @see #eq(Class, String, Comparator)
     * @see PropertyComparisonConstraint.EqualToOp
     */
    public static <V> Constraint<V> eq(Class<V> type, String property) {
        return eq(type, property, (Comparator<V>) DefaultComparator.INSTANCE);
    }

    /**
     * Returns constraint that applies <code>==</code> operator to a value
     * and the specified property.
     *
     * @param <V> The property value type.
     * @param type Type of the operands.
     * @param property Property name of the second operand.
     * @param comparator Comparator to be used for operands comparison.
     * @return Constraint that applies <code>==</code> operator to a value
     *         and the specified property.
     * @see PropertyComparisonConstraint.EqualToOp
     */
    public static <V> Constraint<V> eq(Class<V> type, String property, Comparator<V> comparator) {
        return new PropertyComparisonConstraint.EqualToOp<V>(type, property, comparator);
    }

    /**
     * Returns constraint that applies <code>!=</code> operator to a value
     * and the specified property.
     *
     * @param <V> The property value type.
     * @param type Type of the operands.
     * @param property Property name of the second operand.
     * @return Constraint that applies <code>!=</code> operator to a value
     *         and the specified property.
     * @see #ne(Class, String, Comparator)
     * @see PropertyComparisonConstraint.NotEqualToOp
     */
    public static <V> Constraint<V> ne(Class<V> type, String property) {
        return ne(type, property, (Comparator<V>) DefaultComparator.INSTANCE);
    }

    /**
     * Returns constraint that applies <code>!=</code> operator to a value
     * and the specified property.
     *
     * @param <V> The property value type.
     * @param type Type of the operands.
     * @param property Property name of the second operand.
     * @param comparator Comparator to be used for operands comparison.
     * @return Constraint that applies <code>!=</code> operator to a value
     *         and the specified property.
     * @see PropertyComparisonConstraint.NotEqualToOp
     */
    public static <V> Constraint<V> ne(Class<V> type, String property, Comparator<V> comparator) {
        return new PropertyComparisonConstraint.NotEqualToOp<V>(type, property, comparator);
    }

    /**
     * Returns constraint that applies <code>&lt;</code> operator to a value
     * and the specified property.
     *
     * @param <V> The property value type.
     * @param type Type of the operands.
     * @param property Property name of the second operand.
     * @return Constraint that applies <code>&lt;</code> operator to a value
     *         and the specified property.
     * @see #lt(Class, String, Comparator)
     * @see PropertyComparisonConstraint.LessThanOp
     */
    public static <V extends Comparable<? super V>> Constraint<V> lt(Class<V> type, String property) {
        return lt(type, property, (Comparator<V>) DefaultComparator.INSTANCE);
    }

    /**
     * Returns constraint that applies <code>&lt;</code> operator to a value
     * and the specified property.
     *
     * @param <V> The property value type.
     * @param type Type of the operands.
     * @param property Property name of the second operand.
     * @param comparator Comparator to be used for operands comparison.
     * @return Constraint that applies <code>&lt;</code> operator to a value
     *         and the specified property.
     * @see PropertyComparisonConstraint.LessThanOp
     */
    public static <V> Constraint<V> lt(Class<V> type, String property, Comparator<V> comparator) {
        return new PropertyComparisonConstraint.LessThanOp<V>(type, property, comparator);
    }

    /**
     * Returns constraint that applies <code>&lt;=</code> operator to a value
     * and the specified property.
     *
     * @param <V> The property value type.
     * @param type Type of the operands.
     * @param property Property name of the second operand.
     * @return Constraint that applies <code>&lt;=</code> operator to a value
     *         and the specified property.
     * @see #lte(Class, String, Comparator)
     * @see PropertyComparisonConstraint.LessThanEqualOp
     */
    public static <V extends Comparable<? super V>> Constraint<V> lte(Class<V> type, String property) {
        return lte(type, property, (Comparator<V>) DefaultComparator.INSTANCE);
    }

    /**
     * Returns constraint that applies <code>&lt;=</code> operator to a value
     * and the specified property.
     *
     * @param <V> The property value type.
     * @param type Type of the operands.
     * @param property Property name of the second operand.
     * @param comparator Comparator to be used for operands comparison.
     * @return Constraint that applies <code>&lt;=</code> operator to a value
     *         and the specified property.
     * @see PropertyComparisonConstraint.LessThanEqualOp
     */
    public static <V> Constraint<V> lte(Class<V> type, String property, Comparator<V> comparator) {
        return new PropertyComparisonConstraint.LessThanEqualOp<V>(type, property, comparator);
    }

    /**
     * Returns constraint that applies <code>&gt;</code> operator to a value
     * and the specified property.
     *
     * @param <V> The property value type.
     * @param type Type of the operands.
     * @param property Property name of the second operand.
     * @return Constraint that applies <code>&gt;</code> operator to a value
     *         and the specified property.
     * @see #gt(Class, String, Comparator)
     * @see PropertyComparisonConstraint.GreaterThanOp
     */
    public static <V extends Comparable<? super V>> Constraint<V> gt(Class<V> type, String property) {
        return gt(type, property, (Comparator<V>) DefaultComparator.INSTANCE);
    }

    /**
     * Returns constraint that applies <code>&gt;</code> operator to a value
     * and the specified property.
     *
     * @param <V> The property value type.
     * @param type Type of the operands.
     * @param property Property name of the second operand.
     * @param comparator Comparator to be used for operands comparison.
     * @return Constraint that applies <code>&gt;</code> operator to a value
     *         and the specified property.
     * @see PropertyComparisonConstraint.GreaterThanOp
     */
    public static <V> Constraint<V> gt(Class<V> type, String property, Comparator<V> comparator) {
        return new PropertyComparisonConstraint.GreaterThanOp<V>(type, property, comparator);
    }

    /**
     * Returns constraint that applies <code>&gt;=</code> operator to a value
     * and the specified property.
     *
     * @param <V> The property value type.
     * @param type Type of the operands.
     * @param property Property name of the second operand.
     * @return Constraint that applies <code>&gt;=</code> operator to a value
     *         and the specified property.
     * @see #gte(Class, String, Comparator)
     * @see PropertyComparisonConstraint.GreaterThanEqualOp
     */
    public static <V extends Comparable<? super V>> Constraint<V> gte(Class<V> type, String property) {
        return gte(type, property, (Comparator<V>) DefaultComparator.INSTANCE);
    }

    /**
     * Returns constraint that applies <code>&gt;=</code> operator to a value
     * and the specified property.
     *
     * @param <V> The property value type.
     * @param type Type of the operands.
     * @param property Property name of the second operand.
     * @param comparator Comparator to be used for operands comparison.
     * @return Constraint that applies <code>&gt;=</code> operator to a value
     *         and the specified property.
     * @see PropertyComparisonConstraint.GreaterThanEqualOp
     */
    public static <V> Constraint<V> gte(Class<V> type, String property, Comparator<V> comparator) {
        return new PropertyComparisonConstraint.GreaterThanEqualOp<V>(type, property, comparator);
    }

    // Composite

    /**
     * Returns constraint that is composition of the specified constraints.
     *
     * @param <V> The value type.
     * @param constraints Collection of constraints to be used for validation
     *        of a value.
     * @return Constraint that is composition of the specified constraints.
     * @see #join(Class, Constraint...)
     * @see ConstraintComposition
     */
    public static <V> Constraint<V> join(Collection<Constraint<? super V>> constraints) {
        return join(constraints.toArray((Constraint<V>[]) new Constraint<?>[constraints.size()]));
    }

    /**
     * Returns constraint that is composition of the specified constraints.
     *
     * @param <V> The value type.
     * @param type The type of value to be validated.
     * @param constraints Collection of constraints to be used for validation
     *        of a value.
     * @return Constraint that is composition of the specified constraints.
     * @see #join(Class, Constraint...)
     * @see ConstraintComposition
     */
    public static <V> Constraint<V> join(Class<V> type, Collection<Constraint<? super V>> constraints) {
        return join(type, constraints.toArray((Constraint<V>[]) new Constraint<?>[constraints.size()]));
    }

    /**
     * Returns constraint that is composition of the specified constraints.
     *
     * @param <V> The value type.
     * @param constraints Array of constraints to be used for validation of a
     *        value.
     * @return Constraint that is composition of the specified constraints.
     * @see #join(Class, Constraint...)
     * @see ConstraintComposition
     */
    public static <V> Constraint<V> join(Constraint<? super V>... constraints) {
        return join(ConstraintFactory.<V>typeOf(constraints), constraints);
    }

    /**
     * Returns constraint that is composition of the specified constraints.
     *
     * @param <V> The value type.
     * @param type The type of value to be validated.
     * @param constraints Array of constraints to be used for validation of a
     *        value.
     * @return Constraint that is composition of the specified constraints.
     * @see ConstraintComposition
     */
    public static <V> Constraint<V> join(Class<V> type, Constraint<? super V>... constraints) {
        if (constraints.length == 1)
            return (Constraint<V>) constraints[0];
        return new ConstraintComposition<V>(type, constraints);
    }

    /**
     * Returns constraint that is negation of the specified constraints.
     *
     * @param <V> The value type.
     * @param constraints Collection of constraints to be used for validation
     *        of a value.
     * @return Constraint that is negation of the specified constraints.
     * @see #not(Class, Constraint...)
     * @see ConstraintNegation
     */
    public static <V> Constraint<V> not(Collection<Constraint<? super V>> constraints) {
        return not(constraints.toArray((Constraint<V>[]) new Constraint<?>[constraints.size()]));
    }

    /**
     * Returns constraint that is negation of the specified constraints.
     *
     * @param <V> The value type.
     * @param type The type of value to be validated.
     * @param constraints Collection of constraints to be used for validation
     *        of a value.
     * @return Constraint that is negation of the specified constraints.
     * @see #not(Class, Constraint...)
     * @see ConstraintNegation
     */
    public static <V> Constraint<V> not(Class<V> type, Collection<Constraint<? super V>> constraints) {
        return not(type, constraints.toArray((Constraint<V>[]) new Constraint<?>[constraints.size()]));
    }

    /**
     * Returns constraint that is negation of the specified constraints.
     *
     * @param <V> The value type.
     * @param constraints Array of constraints to be used for validation of a
     *        value.
     * @return Constraint that is negation of the specified constraints.
     * @see #not(Class, Constraint...)
     * @see ConstraintNegation
     */
    public static <V> Constraint<V> not(Constraint<? super V>... constraints) {
        return not(ConstraintFactory.<V>typeOf(constraints), constraints);
    }

    /**
     * Returns constraint that is negation of the specified constraints.
     *
     * @param <V> The value type.
     * @param type The type of value to be validated.
     * @param constraints Array of constraints to be used for validation of a
     *        value.
     * @return Constraint that is negation of the specified constraints.
     * @see ConstraintNegation
     */
    public static <V> Constraint<V> not(Class<V> type, Constraint<? super V>... constraints) {
        return new ConstraintNegation<V>(type, constraints);
    }

    /**
     * Returns constraint that is conjunction of the specified constraints.
     *
     * @param <V> The value type.
     * @param constraints Collection of constraints to be used for validation
     *        of a value.
     * @return Constraint that is conjunction of the specified constraints.
     * @see #and(Class, Constraint...)
     * @see ConstraintConjunction
     */
    public static <V> Constraint<V> and(Collection<Constraint<? super V>> constraints) {
        return and(constraints.toArray((Constraint<V>[]) new Constraint<?>[constraints.size()]));
    }

    /**
     * Returns constraint that is conjunction of the specified constraints.
     *
     * @param <V> The value type.
     * @param type The type of value to be validated.
     * @param constraints Collection of constraints to be used for validation
     *        of a value.
     * @return Constraint that is conjunction of the specified constraints.
     * @see #and(Class, Constraint...)
     * @see ConstraintConjunction
     */
    public static <V> Constraint<V> and(Class<V> type, Collection<Constraint<? super V>> constraints) {
        return and(type, constraints.toArray((Constraint<V>[]) new Constraint<?>[constraints.size()]));
    }

    /**
     * Returns constraint that is conjunction of the specified constraints.
     *
     * @param <V> The value type.
     * @param constraints Array of constraints to be used for validation of a
     *        value.
     * @return Constraint that is conjunction of the specified constraints.
     * @see #and(Class, Constraint...)
     * @see ConstraintConjunction
     */
    public static <V> Constraint<V> and(Constraint<? super V>... constraints) {
        return and(ConstraintFactory.<V>typeOf(constraints), constraints);
    }

    /**
     * Returns constraint that is conjunction of the specified constraints.
     *
     * @param <V> The value type.
     * @param type The type of value to be validated.
     * @param constraints Array of constraints to be used for validation of a
     *        value.
     * @return Constraint that is conjunction of the specified constraints.
     * @see ConstraintConjunction
     */
    public static <V> Constraint<V> and(Class<V> type, Constraint<? super V>... constraints) {
        if (constraints.length == 1)
            return (Constraint<V>) constraints[0];
        return new ConstraintConjunction<V>(type, constraints);
    }

    /**
     * Returns constraint that is disjunction of the specified constraints.
     *
     * @param <V> The value type.
     * @param constraints Collection of constraints to be used for validation
     *        of a value.
     * @return Constraint that is disjunction of the specified constraints.
     * @see #or(Class, Constraint...)
     * @see ConstraintDisjunction
     */
    public static <V> Constraint<V> or(Collection<Constraint<? super V>> constraints) {
        return or(constraints.toArray((Constraint<V>[]) new Constraint<?>[constraints.size()]));
    }

    /**
     * Returns constraint that is disjunction of the specified constraints.
     *
     * @param <V> The value type.
     * @param type The type of value to be validated.
     * @param constraints Collection of constraints to be used for validation
     *        of a value.
     * @return Constraint that is disjunction of the specified constraints.
     * @see #or(Class, Constraint...)
     * @see ConstraintDisjunction
     */
    public static <V> Constraint<V> or(Class<V> type, Collection<Constraint<? super V>> constraints) {
        return or(type, constraints.toArray((Constraint<V>[]) new Constraint<?>[constraints.size()]));
    }

    /**
     * Returns constraint that is disjunction of the specified constraints.
     *
     * @param <V> The value type.
     * @param constraints Array of constraints to be used for validation of a
     *        value.
     * @return Constraint that is disjunction of the specified constraints.
     * @see #or(Class, Constraint...)
     * @see ConstraintDisjunction
     */
    public static <V> Constraint<V> or(Constraint<? super V>... constraints) {
        return or(ConstraintFactory.<V>typeOf(constraints), constraints);
    }

    /**
     * Returns constraint that is disjunction of the specified constraints.
     *
     * @param <V> The value type.
     * @param type The type of value to be validated.
     * @param constraints Array of constraints to be used for validation of a
     *        value.
     * @return Constraint that is disjunction of the specified constraints.
     * @see ConstraintDisjunction
     */
    public static <V> Constraint<V> or(Class<V> type, Constraint<? super V>... constraints) {
        if (constraints.length == 1)
            return (Constraint<V>) constraints[0];
        return new ConstraintDisjunction<V>(type, constraints);
    }

    /**
     * Returns constraint that performs validation of array elements.
     *
     * @param <V> The array elements type.
     * @param constraint Constraint to be used for validation of array elements.
     * @return Constraint that performs validation of array elements.
     * @see ArrayElementConstraint
     */
    public static <V> Constraint<? super V[]> arrayElement(Constraint<V> constraint) {
        return new ArrayElementConstraint<V>(constraint);
    }

    /**
     * Returns constraint that performs validation of collection elements.
     *
     * @param <V> The collection elements type.
     * @param constraint Constraint to be used for validation of collection
     *        elements.
     * @return Constraint that performs validation of collection elements.
     * @see CollectionElementConstraint
     */
    public static <V> Constraint<? super Collection<V>> collectionElement(Constraint<V> constraint) {
        return new CollectionElementConstraint<V>(constraint);
    }

    /**
     * Returns constraint that performs validation of map keys.
     *
     * @param <V> The map keys type.
     * @param constraint Constraint to be used for validation of map keys.
     * @return Constraint that performs validation of map keys.
     * @see MapKeyConstraint
     */
    public static <V> Constraint<? super Map<V, Object>> mapKey(Constraint<V> constraint) {
        return new MapKeyConstraint<V>(constraint);
    }

    /**
     * Returns constraint that performs validation of map values.
     *
     * @param <V> The map values type.
     * @param constraint Constraint to be used for validation of map values.
     * @return Constraint that performs validation of map values.
     * @see MapValueConstraint
     */
    public static <V> Constraint<? super Map<Object, V>> mapValue(Constraint<V> constraint) {
        return new MapValueConstraint<V>(constraint);
    }

    /**
     * Returns constraint that performs cascade validation on an entity.
     *
     * @param <V> The entity type.
     * @param type The type of entity to be validated.
     * @return Constraint that performs cascade validation on an entity.
     * @see CascadeConstraint
     */
    public static <V> Constraint<V> cascade(Class<V> type) {
        return new CascadeConstraint<V>(type);
    }

    /**
     * Returns constraint that performs cascade validation on an entity.
     *
     * @param <V> The entity type.
     * @param metadata Entity metadata.
     * @return Constraint that performs cascade validation on an entity.
     * @see CascadeConstraint
     */
    public static <V> Constraint<V> cascade(EntityMetaData<V> metadata) {
        return new CascadeConstraint<V>(metadata);
    }

    /**
     * Returns supertype for the array of constraints.
     *
     * @param <V> The value type.
     * @param constraints Array of constraints.
     * @return Supertype for the array of constraints.
     */
    private static <V> Class<V> typeOf(Constraint<? super V>[] constraints) {
        Class<?>[] types = new Class<?>[constraints.length];
        for (int i = 0; i < constraints.length; i++)
            types[i] = constraints[i].getType();
        return (Class<V>) Types.superTypeOf(types);
    }

    // Wrapper

    /**
     * Wraps error message of the specified constraint.
     *
     * <p>Note that this method has no effect if the specified constraint is
     * instance of the <code>CorrectConstraint</code>.</p>
     *
     * @param <V> The value type.
     * @param constraint Constraint to be wrapped.
     * @param message Error message template key.
     * @return A new constraint with wrapped error message.
     * @see ConstraintMessageWrapper
     */
    public static <V> Constraint<V> wrapMessage(Constraint<V> constraint, String message) {
        if (unwrapConstraint(constraint) instanceof CorrectConstraint)
            return constraint;
        return new ConstraintMessageWrapper<V>(constraint, message);
    }

    /**
     * Determines if the specified constraint has wrapped error message.
     *
     * @param constraint Constraint to be tested.
     * @return <code>true</code> if the specified constraint has wrapped
     *         error message; <code>false</code> otherwise.
     * @see ConstraintMessageWrapper
     */
    public static boolean hasWrappedMessage(Constraint<?> constraint) {
        if (constraint instanceof ConstraintWrapper) {
            if (constraint instanceof ConstraintMessageWrapper)
                return true;
            return hasWrappedMessage(((ConstraintWrapper<?>) constraint).constraint);
        }
        return false;
    }

    /**
     * Returns set of groups the specified constraint is applied on.
     *
     * @param constraint Constraint whose groups to be returned.
     * @return Set of groups the specified constraint is applied on.
     */
    public static Set<String> getGroups(Constraint<?> constraint) {
        if (constraint instanceof ConstraintWrapper) {
            if (constraint instanceof ConstraintGroupWrapper)
                return ((ConstraintGroupWrapper<?>) constraint).getGroups();
            return getGroups(((ConstraintWrapper<?>) constraint).constraint);
        }

        if (constraint instanceof ConstraintAggregation) {
            Set<String> groups = new HashSet<String>();
            ConstraintAggregation<?> aggregation = (ConstraintAggregation<?>) constraint;
            for (int i = 0; i < aggregation.constraints.length; i++)
                groups.addAll(getGroups(aggregation.constraints[i]));
            return groups;
        }

        if (constraint instanceof SequenceElementConstraint)
            return getGroups(((SequenceElementConstraint<?, ?>) constraint).constraint);

        return DEFAULT_GROUP_SET;
    }

    /**
     * Wraps groups of the specified constraint.
     *
     * @param <V> The value type.
     * @param constraint Constraint to be wrapped.
     * @param groups Array of constraint groups.
     * @return A new constraint with wrapped groups.
     * @see ConstraintGroupWrapper
     */
    public static <V> Constraint<V> wrapGroups(Constraint<V> constraint, String... groups) {
        if (groups == null || groups.length == 0)
            return wrapGroups(constraint);
        if (constraint instanceof ConstraintGroupWrapper)
            return wrapGroups(((ConstraintGroupWrapper<V>) constraint).constraint, groups);
        return new ConstraintGroupWrapper<V>(constraint, groups);
    }

    /**
     * Wraps groups of the specified constraint by default.
     *
     * @param <V> The value type.
     * @param constraint Constraint to be wrapped.
     * @return A new constraint with wrapped groups.
     * @see ConstraintGroupWrapper
     */
    public static <V> Constraint<V> wrapGroups(Constraint<V> constraint) {
        if (constraint instanceof ConstraintGroupWrapper)
            return constraint;
        return new ConstraintGroupWrapper<V>(constraint, getGroups(constraint));
    }

    /**
     * Returns unwrapped constraint for the specified one.
     *
     * @param <V> The value type.
     * @param wrapper Wrapper constraint.
     * @return Unwrapped constraint for the specified one or the specified
     *         constraint if it was not wrapped.
     * @see ConstraintWrapper
     */
    public static <V> Constraint<V> unwrapConstraint(Constraint<V> wrapper) {
        return wrapper instanceof ConstraintWrapper
            ? unwrapConstraint(((ConstraintWrapper<V>) wrapper).constraint)
            : wrapper;
    }

    /**
     * Searches constraint by its type in the specified one.
     *
     * @param <T> The constraint type.
     * @param wrapper Wrapper or aggregation constraint.
     * @param requiredType Type of the required constraint.
     * @return Constraint extracted from the specified one or <code>null</code>
     *         if there is no required constraint found.
     */
    public static <T extends Constraint<?>> T findConstraint(Constraint<?> wrapper, Class<T> requiredType) {
        if (requiredType.isAssignableFrom(wrapper.getClass()))
            return (T) wrapper;
        if (wrapper instanceof ConstraintWrapper)
            return findConstraint(((ConstraintWrapper<?>) wrapper).constraint, requiredType);
        if (wrapper instanceof ConstraintAggregation)
            for (Constraint<?> constraint : ((ConstraintAggregation<?>) wrapper).constraints) {
                T required = findConstraint(constraint, requiredType);
                if (required != null)
                    return required;
            }
        return null;
    }

    /**
     * Searches enumeration constants for the specified constraint.
     *
     * @param <V> The constants type.
     * @param wrapper Wrapper or aggregation constraint.
     * @return Enumeration constants or empty set if there is no enumeration
     *         constraint found.
     */
    public static <V> Set<V> findConstants(Constraint<?> wrapper) {
        EnumerationConstraint<V> constraint = findConstraint(wrapper, EnumerationConstraint.class);
        if (constraint == null)
            return Collections.emptySet();
        return constraint.getConstants();
    }

    /**
     * Searches default value for the specified constraint.
     *
     * @param <V> The value type.
     * @param wrapper Wrapper or aggregation constraint.
     * @return Default value or <code>null</code> if there is no default value
     *         constraint found.
     */
    public static <V> V findDefaultValue(Constraint<?> wrapper) {
        DefaultValueConstraint<V> constraint = findConstraint(wrapper, DefaultValueConstraint.class);
        return constraint == null ? null : constraint.getDefaultValue();
    }

    // Annotation

    /**
     * Determines if the specified annotation is either constraint annotation
     * or custom constraint annotation.
     *
     * @param annotation Annotation to be tested.
     * @return <code>true</code> if the specified annotation is either
     *         constraint annotation or custom constraint annotation;
     *         <code>false</code> otherwise.
     */
    public static boolean isConstraintAnnotation(Annotation annotation) {
        return isAnnotationPresent(annotation, ConstrainedBy.class);
    }

    /**
     * Creates a new constraint for the specified generic value type using
     * configuration from the specified annotations.
     *
     * <p>This method may return <code>null</code> if there are no constraint
     * annotations specified. Annotations that are not constraint annotations
     * would be skipped.</p>
     *
     * @param <V> The value type.
     * @param type The type of value to be validated.
     * @param annotations Array of annotations.
     * @param namespace Namespace (for example, qualified property name).
     * @param defaults Validation defaults.
     * @return A new constraint or <code>null</code> if there are no constraint
     *         annotations specified.
     * @throws ValidationTargetException if there is an error in constraint
     *         declaration.
     * @see #createConstraint(Class, Annotation, String, ValidationDefaults)
     */
    public static <V> Constraint<? super V> createConstraint(Type type, Annotation[] annotations,
            String namespace, ValidationDefaults defaults) {
        String[] groups = defaults == null ? null : defaults.groups();

        Class<Object> valueType = Types.rawTypeOf(type);
        Class<Object> keyType = Types.keyTypeOf(type);
        Class<Object> elementType = Types.elementTypeOf(type);

        LinkedList<Constraint<? super Object>> valueConstraints = new LinkedList<Constraint<? super Object>>();
        LinkedList<Constraint<? super Object>> keyConstraints = new LinkedList<Constraint<? super Object>>();
        LinkedList<Constraint<? super Object>> elementConstraints = new LinkedList<Constraint<? super Object>>();

        Annotation[] aggregations = new Annotation[3];
        for (Annotation list : annotations)
            for (Annotation annotation : getAnnotationList(list)) {
                Class<? extends Annotation> annotationType = annotation.annotationType();
                Set<ValidationTarget> targets = getAnnotationTargets(annotation);
                if (annotationType == Composition.class ||
                    annotationType == Conjunction.class ||
                    annotationType == Disjunction.class ||
                    annotationType == Negation.class) {
                    for (ValidationTarget target : targets) {
                        if (aggregations[target.ordinal()] != null)
                            throw new ValidationTargetException(annotation, target);
                        aggregations[target.ordinal()] = annotation;
                    }
                } else {
                    Constraint<? super Object> constraint = null;
                    for (ValidationTarget target : targets) {
                        if (target == KEYS) {
                            if (keyType == null)
                                throw new ValidationTargetException(annotation, target);
                            constraint = createConstraint(keyType, annotation, namespace, defaults);
                            if (constraint != null)
                                keyConstraints.add(constraint);
                        } else if (target == ELEMENTS) {
                            if (elementType == null)
                                throw new ValidationTargetException(annotation, target);
                            constraint = createConstraint(elementType, annotation, namespace, defaults);
                            if (constraint != null)
                                elementConstraints.add(constraint);
                        } else {
                            constraint = createConstraint(valueType, annotation, namespace, defaults);
                            if (constraint != null)
                                valueConstraints.add(constraint);
                        }
                    }
                }
            }

        if (keyType == null && aggregations[KEYS.ordinal()] != null)
            throw new ValidationTargetException(aggregations[KEYS.ordinal()], KEYS);
        if (elementType == null && aggregations[ELEMENTS.ordinal()] != null)
            throw new ValidationTargetException(aggregations[ELEMENTS.ordinal()], ELEMENTS);

        if (keyConstraints.size() > 0) {
            Constraint<Object> keyConstraint = createAggregation(keyType, keyConstraints,
                    aggregations[KEYS.ordinal()], namespace, groups);
            if (Map.class.isAssignableFrom(valueType))
                valueConstraints.addFirst(wrapComponent((Constraint<Object>) mapKey(keyConstraint),
                        KEYS, namespace));
        }

        if (elementConstraints.size() > 0) {
            Constraint<Object> elementConstraint = createAggregation(elementType, elementConstraints,
                    aggregations[ELEMENTS.ordinal()], namespace, groups);
            if (Collection.class.isAssignableFrom(valueType)) {
                valueConstraints.addFirst(wrapComponent((Constraint<Object>) collectionElement(elementConstraint),
                        ELEMENTS, namespace));
            } else if (Map.class.isAssignableFrom(valueType)) {
                valueConstraints.addFirst(wrapComponent((Constraint<Object>) mapValue(elementConstraint),
                        ELEMENTS, namespace));
            } else if (valueType.isArray()) {
                valueConstraints.addFirst(wrapComponent((Constraint<Object>) arrayElement(elementConstraint),
                        ELEMENTS, namespace));
            }
        }

        if (valueConstraints.size() > 0)
            return createAggregation(valueType, valueConstraints,
                    aggregations[VALUE.ordinal()], namespace, groups);

        return null;
    }

    /**
     * Creates a new constraint for the specified raw value type using
     * configuration from the specified annotation.
     *
     * <p>This method may return <code>null</code> if the specified annotation
     * is not a constraint annotation. This method is responsible for creation
     * constraints of raw types only.</p>
     *
     * @param <V> The value type.
     * @param type The type of value to be validated.
     * @param annotation Constraint annotation.
     * @param namespace Namespace (for example, qualified property name).
     * @param defaults Validation defaults.
     * @return A new constraint or <code>null</code> if the specified annotation
     *         is not a constraint annotation.
     * @throws ValidationDeclarationException if there is an error in constraint
     *         declaration.
     * @see AnnotationSupport#createValidation(Class, Annotation, Class)
     */
    private static <V> Constraint<? super V> createConstraint(Class<V> type, Annotation annotation,
            String namespace, ValidationDefaults defaults) {
        String[] groups = defaults == null ? null : defaults.groups();
        Class<? extends Annotation> annotationType = annotation.annotationType();
        ConstrainedBy constrainedBy = annotationType.getAnnotation(ConstrainedBy.class);

        if (constrainedBy == null) {
            if (isConstraintAnnotation(annotation)) {
                Constraint<? super V> constraint = (Constraint<? super V>) getFromCache(annotationType, type);
                if (constraint == null) {
                    constraint = createConstraint(type, annotationType.getAnnotations(), null, null);
                    constraint = new CustomConstraint<V>((Constraint<V>) constraint);
                    addToCache(annotationType, constraint);
                }
                return wrapConstraint(constraint, annotation, namespace, groups);
            }
            return null;
        }

        Class<?> wrapperType = Types.wrapperTypeOf(type);
        for (Class<? extends Constraint> constraintType : constrainedBy.value())
            if (Types.parameterTypeOf(constraintType, Constraint.class, 0).isAssignableFrom(wrapperType)) {
                Constraint<V> constraint = (Constraint<V>) createValidation(
                        (Class<Constraint<?>>) constraintType, annotation, type);
                if (constraint.getType().isAssignableFrom(wrapperType))
                    return wrapConstraint(constraint, annotation, namespace, groups);
            }

        throw new ValidationTypeException(annotation, annotationType);
    }

    /**
     * Creates aggregation of the specified constraints.
     *
     * <p>Note that if there is no constraint aggregation specified then
     * constraint composition will be used by default.</p>
     *
     * @param <V> The value type.
     * @param type The type of value to be validated.
     * @param constraints List of constraints to be aggregated.
     * @param aggregation Constraint aggregation annotation.
     * @param namespace Namespace (for example, qualified property name).
     * @param groups Array of default constraint groups.
     * @return Aggregation of the specified constraints.
     */
    private static <V> Constraint<V> createAggregation(Class<V> type, List<Constraint<? super V>> constraints,
            Annotation aggregation, String namespace, String[] groups) {
        Class<? extends Annotation> aggregationType = aggregation == null ? null : aggregation.annotationType();
        if (aggregationType == Conjunction.class)
            return wrapConstraint(and(type, constraints), aggregation, namespace, groups);
        if (aggregationType == Disjunction.class)
            return wrapConstraint(or(type, constraints), aggregation, namespace, groups);
        if (aggregationType == Negation.class)
            return wrapConstraint(not(type, constraints), aggregation, namespace, groups);
        return wrapConstraint(join(type, constraints), aggregation, namespace, groups);
    }

    /**
     * Wraps the specified constraint using information from the specified
     * annotatation.
     *
     * @param <V> The value type.
     * @param constraint Constraint to be wrapped.
     * @param annotation Constraint annotation.
     * @param namespace Namespace (for example, qualified property name).
     * @param groups Array of default constraint groups.
     * @return A new wrapped constraint.
     */
    private static <V> Constraint<V> wrapConstraint(Constraint<V> constraint, Annotation annotation,
            String namespace, String[] groups) {
        String message = annotation == null ? null : getAnnotationMessage(annotation, namespace);
        if (message != null)
            constraint = wrapMessage(constraint, message);
        if (namespace == null) {
            groups = annotation == null ? null : getAnnotationGroups(annotation);
            return groups == null ? constraint : wrapGroups(constraint, groups);
        }
        if (annotation != null)
            groups = getAnnotationGroups(annotation, groups);
        return wrapGroups(constraint, groups);
    }

    /**
     * Wraps the specified component constraint.
     *
     * @param <V> The value type.
     * @param constraint Constraint to be wrapped.
     * @param target Validation target.
     * @param namespace Namespace (for example, qualified property name).
     * @return A new wrapped constraint.
     */
    private static <V> Constraint<V> wrapComponent(Constraint<V> constraint, ValidationTarget target,
            String namespace) {
        if (!(namespace == null || namespace.isEmpty()))
            constraint = wrapMessage(constraint, namespace + "." + target.name());
        return namespace == null ? constraint : wrapGroups(constraint);
    }

}
