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

import java.lang.annotation.Annotation;

import java.lang.reflect.Type;

import java.math.BigDecimal;
import java.math.BigInteger;

import java.util.Iterator;
import java.util.Collection;
import java.util.Set;
import java.util.Map;
import java.util.Collections;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ConcurrentHashMap;

import org.foxlabs.validation.ValidationTarget;
import org.foxlabs.validation.ValidationTargetException;
import org.foxlabs.validation.ValidationTypeException;
import org.foxlabs.validation.Validator;
import org.foxlabs.validation.ValidatorFactory;
import org.foxlabs.validation.support.AnnotationSupport;

import org.foxlabs.util.reflect.Types;
import org.foxlabs.util.resource.Service;

/**
 * Defines a factory that allows to obtain <code>Converter</code> instances.
 * 
 * <p>This factory has default converters for all primitive types, their
 * wrappers and enums. Also the following types are supported by default:</p>
 * <ul>
 *   <li><code>java.lang.Class</code></li>
 *   <li><code>java.math.BigInteger</code></li>
 *   <li><code>java.math.BigDecimal</code></li>
 *   <li><code>java.util.Date</code></li>
 *   <li><code>java.util.Locale</code></li>
 *   <li><code>java.util.TimeZone</code></li>
 *   <li><code>java.util.logging.Level</code></li>
 *   <li><code>java.net.URL</code></li>
 *   <li><code>java.net.URI</code></li>
 *   <li><code>java.io.File</code></li>
 * </ul>
 * 
 * <p>One-dimensional arrays of any registered types are supported through
 * {@link ArrayConverter}. The <code>java.util.Collection</code> and
 * <code>java.util.Map</code> JDK types are supported through
 * {@link CollectionConverter} and {@link MapConverter} respectively.
 * Additional default converters would be automatically obtained through SPI
 * interface. The <code>META-INF/services/org.foxlabs.validation.converter.Converter</code>
 * file should be used to list such converter types. Any default converter can
 * be registered/overriden from {@link #addDefaultConverter(Converter)} method
 * at any time.</p>
 * 
 * <p><code>ConverterFactory</code> is thread-safe.</p>
 * 
 * @author Fox Mulder
 * @see Converter
 */
@SuppressWarnings({"unchecked", "rawtypes"})
public abstract class ConverterFactory extends AnnotationSupport {
    
    // Can't be inherited.
    private ConverterFactory() {}
    
    // Number
    
    /**
     * Creates a new <code>java.lang.Byte</code> converter with the specified
     * pattern.
     * 
     * @param pattern <code>java.text.DecimalFormat</code> pattern.
     * @return A new converter for <code>java.lang.Byte</code> type.
     * @see ByteConverter
     */
    public static Converter<Byte> forByte(String pattern) {
        return pattern == null
            ? ByteConverter.OBJECT
            : new ByteConverter(Byte.class, pattern);
    }
    
    /**
     * Creates a new <code>java.lang.Short</code> converter with the specified
     * pattern.
     * 
     * @param pattern <code>java.text.DecimalFormat</code> pattern.
     * @return A new converter for <code>java.lang.Short</code> type.
     * @see ShortConverter
     */
    public static Converter<Short> forShort(String pattern) {
        return pattern == null
            ? ShortConverter.OBJECT
            : new ShortConverter(Short.class, pattern);
    }
    
    /**
     * Creates a new <code>java.lang.Integer</code> converter with the
     * specified pattern.
     * 
     * @param pattern <code>java.text.DecimalFormat</code> pattern.
     * @return A new converter for <code>java.lang.Integer</code> type.
     * @see IntegerConverter
     */
    public static Converter<Integer> forInteger(String pattern) {
        return pattern == null
            ? IntegerConverter.OBJECT
            : new IntegerConverter(Integer.class, pattern);
    }
    
    /**
     * Creates a new <code>java.lang.Long</code> converter with the specified
     * pattern.
     * 
     * @param pattern <code>java.text.DecimalFormat</code> pattern.
     * @return A new converter for <code>java.lang.Long</code> type.
     * @see LongConverter
     */
    public static Converter<Long> forLong(String pattern) {
        return pattern == null
            ? LongConverter.OBJECT
            : new LongConverter(Long.class, pattern);
    }
    
    /**
     * Creates a new <code>java.lang.Float</code> converter with the specified
     * pattern.
     * 
     * @param pattern <code>java.text.DecimalFormat</code> pattern.
     * @return A new converter for <code>java.lang.Float</code> type.
     * @see FloatConverter
     */
    public static Converter<Float> forFloat(String pattern) {
        return pattern == null
            ? FloatConverter.OBJECT
            : new FloatConverter(Float.class, pattern);
    }
    
    /**
     * Creates a new <code>java.lang.Double</code> converter with the specified
     * pattern.
     * 
     * @param pattern <code>java.text.DecimalFormat</code> pattern.
     * @return A new converter for <code>java.lang.Double</code> type.
     * @see DoubleConverter
     */
    public static Converter<Double> forDouble(String pattern) {
        return pattern == null
            ? DoubleConverter.OBJECT
            : new DoubleConverter(Double.class, pattern);
    }
    
    /**
     * Creates a new <code>java.math.BigInteger</code> converter with the
     * specified pattern.
     * 
     * @param pattern <code>java.text.DecimalFormat</code> pattern.
     * @return A new converter for <code>java.math.BigInteger</code> type.
     * @see BigIntegerConverter
     */
    public static Converter<BigInteger> forBigInteger(String pattern) {
        return pattern == null
            ? BigIntegerConverter.DEFAULT
            : new BigIntegerConverter(pattern);
    }
    
    /**
     * Creates a new <code>java.math.BigDecimal</code> converter with the
     * specified pattern.
     * 
     * @param pattern <code>java.text.DecimalFormat</code> pattern.
     * @return A new converter for <code>java.math.BigDecimal</code> type.
     * @see BigDecimalConverter
     */
    public static Converter<BigDecimal> forBigDecimal(String pattern) {
        return pattern == null
            ? BigDecimalConverter.DEFAULT
            : new BigDecimalConverter(pattern);
    }
    
    // Boolean
    
    /**
     * Creates a new converter for the specified boolean type and prefix.
     * 
     * @param type Boolean type (can be either primitive or object type).
     * @param prefix Prefix of message keys of boolean constants.
     * @return A new boolean converter.
     * @see BooleanConverter
     */
    public static Converter<Boolean> forBoolean(Class<Boolean> type, String prefix) {
        if (Boolean.class.getName().equals(prefix))
            return Boolean.class == type ? BooleanConverter.OBJECT : BooleanConverter.SIMPLE;
        return new BooleanConverter(type, prefix);
    }
    
    // Enum
    
    /**
     * Creates a new converter for the specified enumeration type.
     * 
     * @param <V> The enumeration type.
     * @param type Enumeration type.
     * @return A new enumeration converter.
     * @see #forEnum(Class, String)
     */
    public static <V extends Enum<V>> Converter<V> forEnum(Class<V> type) {
        return forEnum(type, type.getName());
    }
    
    /**
     * Creates a new converter for the specified enumeration type and prefix.
     * 
     * @param <V> The enumeration type.
     * @param type Enumeration type.
     * @param prefix Prefix of message keys of enumeration constants.
     * @return A new enumeration converter.
     * @see EnumConverter
     */
    public static <V extends Enum<V>> Converter<V> forEnum(Class<V> type, String prefix) {
        return new EnumConverter<V>(type, prefix);
    }
    
    // Date
    
    /**
     * Creates a new <code>java.util.Date</code> converter with the specified
     * date/time pattern.
     * 
     * @param pattern <code>java.text.SimpleDateFormat</code> date/time pattern.
     * @return A new <code>java.util.Date</code> converter.
     * @see DateConverter
     */
    public static Converter<java.util.Date> forDate(String pattern) {
        return new DateConverter(pattern);
    }
    
    /**
     * Creates a new <code>java.util.Date</code> converter with the specified
     * date and time style. If negative value will be specified as date or
     * time style then corresponding part would not be used.
     * 
     * @param dateStyle <code>java.text.DateFormat</code> date style.
     * @param timeStyle <code>java.text.DateFormat</code> time style.
     * @return A new <code>java.util.Date</code> converter.
     * @see DateConverter
     */
    public static Converter<java.util.Date> forDate(int dateStyle, int timeStyle) {
        return new DateConverter(dateStyle, timeStyle);
    }
    
    // Class
    
    /**
     * Creates a new <code>java.lang.Class</code> converter with the specified
     * class loader.
     * 
     * @param loader Class loader to be used for class loading.
     * @return A new <code>java.lang.Class</code> converter.
     * @see ClassConverter
     */
    public static Converter<Class<?>> forClass(ClassLoader loader) {
        return new ClassConverter(loader);
    }
    
    // File
    
    /**
     * Creates a new <code>java.io.File</code> converter with the specified
     * parent directory pathname.
     * 
     * @param parent Parent directory pathname.
     * @return A new <code>java.io.File</code> converter.
     * @see FileConverter
     */
    public static Converter<java.io.File> forFile(String parent) {
        return new FileConverter(parent);
    }
    
    // Array
    
    /**
     * Creates a new array converter with the specified converter of array
     * elements and default tokenizer.
     * 
     * @param <V> The array elements type.
     * @param converter Converter of array elements.
     * @return A new array converter.
     * @see #forArray(Converter, Tokenizer)
     * @see ArrayConverter
     * @see SimpleTokenizer
     */
    public static <V> Converter<V[]> forArray(Converter<V> converter) {
        return forArray(converter, SimpleTokenizer.DEFAULT);
    }
    
    /**
     * Creates a new array converter with the specified converter of array
     * elements and allowed elements delimiters.
     * 
     * @param <V> The array elements type.
     * @param converter Converter of array elements.
     * @param delims Delimiters of array elements.
     * @return A new array converter.
     * @see #forArray(Converter, Tokenizer)
     * @see ArrayConverter
     * @see SimpleTokenizer
     */
    public static <V> Converter<V[]> forArray(Converter<V> converter, String delims) {
        return forArray(converter, tokenizer(delims));
    }
    
    /**
     * Creates a new array converter with the specified converter of array
     * elements and tokenzier of array elements.
     * 
     * @param <V> The array elements type.
     * @param converter Converter of array elements.
     * @param tokenizer Tokenizer of array elements.
     * @return A new array converter.
     * @see ArrayConverter
     * @see Tokenizer
     */
    public static <V> Converter<V[]> forArray(Converter<V> converter, Tokenizer tokenizer) {
        return (Converter) new ArrayConverter<V>(converter, tokenizer);
    }
    
    // Collection
    
    /**
     * Creates a new collection converter with the specified collection type,
     * converter of collection elements and default tokenzier.
     * 
     * @param <T> The collection type.
     * @param <V> The collection elements type.
     * @param type Collection type.
     * @param converter Converter of collection elements.
     * @return A new collection converter.
     * @see #forCollection(Class, Converter, Tokenizer)
     * @see CollectionConverter
     * @see SimpleTokenizer
     */
    public static <T extends Collection<V>, V> Converter<T> forCollection(
            Class<? extends Collection> type, Converter<V> converter) {
        return forCollection(type, converter, SimpleTokenizer.DEFAULT);
    }
    
    /**
     * Creates a new collection converter with the specified collection type,
     * converter of collection elements and allowed elements delimiters.
     * 
     * @param <T> The collection type.
     * @param <V> The collection elements type.
     * @param type Collection type.
     * @param converter Converter of collection elements.
     * @param delims Delimiters of collection elements.
     * @return A new collection converter.
     * @see #forCollection(Class, Converter, Tokenizer)
     * @see CollectionConverter
     * @see SimpleTokenizer
     */
    public static <T extends Collection<V>, V> Converter<T> forCollection(
            Class<? extends Collection> type, Converter<V> converter, String delims) {
        return forCollection(type, converter, tokenizer(delims));
    }
    
    /**
     * Creates a new collection converter with the specified collection type,
     * converter of collection elements and tokenzier of collection elements.
     * 
     * @param <T> The collection type.
     * @param <V> The collection elements type.
     * @param type Collection type.
     * @param converter Converter of collection elements.
     * @param tokenizer Tokenizer of collection elements.
     * @return A new collection converter.
     * @see CollectionConverter
     * @see Tokenizer
     */
    public static <T extends Collection<V>, V> Converter<T> forCollection(
            Class<? extends Collection> type, Converter<V> converter, Tokenizer tokenizer) {
        return Types.cast(new CollectionConverter<V>((Class<? extends Collection<?>>) type,
                converter, tokenizer));
    }
    
    // Map
    
    /**
     * Creates a new map converter with the specified map type, key and value
     * converters and default tokenzier.
     * 
     * @param <T> The map type.
     * @param <K> The map keys type.
     * @param <V> The map values type.
     * @param type Map type.
     * @param kconverter Converter of map keys.
     * @param vconverter Converter of map values.
     * @return A new map converter.
     * @see #forMap(Class, Converter, Converter, Tokenizer)
     * @see MapConverter
     * @see SimpleTokenizer
     */
    public static <T extends Map<K, V>, K, V> Converter<T> forMap(Class<? extends Map> type,
            Converter<K> kconverter, Converter<V> vconverter) {
        return forMap(type, kconverter, vconverter, SimpleTokenizer.DEFAULT);
    }
    
    /**
     * Creates a new map converter with the specified map type, key and value
     * converters, allowed key-value pairs delimiters.
     * 
     * @param <T> The map type.
     * @param <K> The map keys type.
     * @param <V> The map values type.
     * @param type Map type.
     * @param kconverter Converter of map keys.
     * @param vconverter Converter of map values.
     * @param delims Delimiters of key-value pairs.
     * @return A new map converter.
     * @see #forMap(Class, Converter, Converter, Tokenizer)
     * @see MapConverter
     * @see SimpleTokenizer
     */
    public static <T extends Map<K, V>, K, V> Converter<T> forMap(Class<? extends Map> type,
            Converter<K> kconverter, Converter<V> vconverter, String delims) {
        return forMap(type, kconverter, vconverter, tokenizer(delims));
    }
    
    /**
     * Creates a new map converter with the specified map type, key and value
     * converters, key-value pairs tokenzier.
     * 
     * @param <T> The map type.
     * @param <K> The map keys type.
     * @param <V> The map values type.
     * @param type Map type.
     * @param kconverter Converter of map keys.
     * @param vconverter Converter of map values.
     * @param tokenizer Tokenizer of key-value pairs.
     * @return A new map converter.
     * @see MapConverter
     * @see Tokenizer
     */
    public static <T extends Map<K, V>, K, V> Converter<T> forMap(Class<? extends Map> type,
            Converter<K> kconverter, Converter<V> vconverter, Tokenizer tokenizer) {
        return Types.cast(new MapConverter<K, V>((Class<? extends Map<?, ?>>) type,
                kconverter, vconverter, tokenizer));
    }
    
    // Tokenizer
    
    /**
     * Creates a new tokenizer with the specified elements delimiters.
     * 
     * @param delims Allowed elements delimiter characters.
     * @return A new tokenizer.
     * @see SimpleTokenizer
     */
    public static Tokenizer tokenizer(String delims) {
        return new SimpleTokenizer(delims);
    }
    
    // Wrapper
    
    /**
     * Wraps error message of the specified converter.
     * 
     * <p>Note that this method has no effect if the specified constraint is
     * instance of the <code>UnsupportedConverter</code>.</p>
     * 
     * @param <V> The value type.
     * @param converter Converter to be wrapped.
     * @param message Error message template key.
     * @return A new converter with wrapped error message.
     * @see ConverterMessageWrapper
     */
    public static <V> Converter<V> wrapMessage(Converter<V> converter, String message) {
        if (unwrapConverter(converter) instanceof UnsupportedConverter)
            return converter;
        return new ConverterMessageWrapper<V>(converter, message);
    }
    
    /**
     * Determines if the specified converter has wrapped error message.
     * 
     * @param converter Converter to be tested.
     * @return <code>true</code> if the specified converter has wrapped error
     *         message; <code>false</code> otherwise.
     * @see ConverterMessageWrapper
     */
    public static boolean hasWrappedMessage(Converter<?> converter) {
        if (converter instanceof ConverterWrapper) {
            if (converter instanceof ConverterMessageWrapper)
                return true;
            return hasWrappedMessage(((ConverterWrapper<?>) converter).getConverter());
        }
        return false;
    }
    
    /**
     * Returns unwrapped converter for the specified one.
     * 
     * @param <V> The value type.
     * @param converter Converter to be unwrapped.
     * @return Unwrapped converter for the specified one or the specified
     *         converter if it was not wrapped.
     * @see ConverterWrapper
     */
    public static <V> Converter<V> unwrapConverter(Converter<V> converter) {
        return converter instanceof ConverterWrapper
            ? unwrapConverter(((ConverterWrapper<V>) converter).getConverter())
            : converter;
    }
    
    // Factory methods
    
    /**
     * Default converters cache.
     */
    private final static ConcurrentMap<Class<?>, Converter<?>> defaultConverters;
    
    // Register default and SPI converters.
    static {
        defaultConverters = new ConcurrentHashMap<Class<?>, Converter<?>>();
        registerDefaultConverters();
        registerSpiConverters();
    }
    
    /**
     * Registers all default converters.
     */
    private static void registerDefaultConverters() {
        addDefaultConverter(BooleanConverter.SIMPLE);
        addDefaultConverter(BooleanConverter.OBJECT);
        addDefaultConverter(CharacterConverter.SIMPLE);
        addDefaultConverter(CharacterConverter.OBJECT);
        addDefaultConverter(ByteConverter.SIMPLE);
        addDefaultConverter(ByteConverter.OBJECT);
        addDefaultConverter(ShortConverter.SIMPLE);
        addDefaultConverter(ShortConverter.OBJECT);
        addDefaultConverter(IntegerConverter.SIMPLE);
        addDefaultConverter(IntegerConverter.OBJECT);
        addDefaultConverter(LongConverter.SIMPLE);
        addDefaultConverter(LongConverter.OBJECT);
        addDefaultConverter(FloatConverter.SIMPLE);
        addDefaultConverter(FloatConverter.OBJECT);
        addDefaultConverter(DoubleConverter.SIMPLE);
        addDefaultConverter(DoubleConverter.OBJECT);
        addDefaultConverter(StringConverter.DEFAULT);
        addDefaultConverter(BigIntegerConverter.DEFAULT);
        addDefaultConverter(BigDecimalConverter.DEFAULT);
        addDefaultConverter(DateConverter.DEFAULT);
        addDefaultConverter(ClassConverter.DEFAULT);
        addDefaultConverter(TimeZoneConverter.DEFAULT);
        addDefaultConverter(LocaleConverter.DEFAULT);
        addDefaultConverter(LogLevelConverter.DEFAULT);
        addDefaultConverter(URLConverter.DEFAULT);
        addDefaultConverter(URIConverter.DEFAULT);
        addDefaultConverter(FileConverter.DEFAULT);
    }
    
    /**
     * Registers default SPI converters.
     */
    private static void registerSpiConverters() {
        Iterator<Converter> itr = Service.lookup(Converter.class);
        while (itr.hasNext())
            addDefaultConverter(itr.next());
    }
    
    /**
     * Returns all supported value types by default.
     * 
     * @return <b>Immutable</b> set of the supported value types.
     */
    public static Set<Class<?>> getSupportedTypes() {
        return Collections.unmodifiableSet(defaultConverters.keySet());
    }
    
    /**
     * Registers a new default converter or overrides existing one.
     * 
     * @param <V> The value type.
     * @param converter A new converter.
     * @return A new converter.
     */
    public static <V> Converter<V> addDefaultConverter(Converter<V> converter) {
        defaultConverters.put(converter.getType(), converter);
        return converter;
    }
    
    /**
     * Returns default converter for the specified value type.
     * If there is no converter found for the specified value type then
     * {@link UnsupportedConverter} will be returned.
     * 
     * @param <V> The value type.
     * @param type The type of value to be converted.
     * @return Converter for the specified value type.
     */
    public static <V> Converter<V> getDefaultConverter(Class<V> type) {
        Converter<?> converter = defaultConverters.get(type);
        if (converter != null)
            return (Converter<V>) converter;
        
        if (type.isEnum()) {
            converter = forEnum(type.asSubclass(Enum.class));
        } else if (type.isArray()) {
            converter = forArray(getDefaultConverter(type.getComponentType()));
        } else {
            return new UnsupportedConverter<V>(type);
        }
        
        return (Converter<V>) addDefaultConverter(converter);
    }
    
    /**
     * Returns converter for the specified value type.
     * 
     * <p>This method differs from {@link #getDefaultConverter(Class)} in that
     * it allows to override prefix of message keys for boolean and enumeration
     * constants.</p>
     * 
     * @param <V> The value type.
     * @param type The type of value to be converted.
     * @param prefix Prefix of message keys of boolean and enumeration constants.
     * @return Converter for the specified value type.
     * @see #getDefaultConverter(Class)
     * @see AnnotationSupport#createValidation(Class, Annotation, Class)
     */
    public static <V> Converter<V> createConverter(Class<V> type, String prefix) {
        if (Types.isBoolean(type)) {
            return Types.<Converter<V>>cast(forBoolean((Class<Boolean>) type, prefix));
        } else if (type.isEnum()) {
            return Types.<Converter<V>>cast(forEnum(type.asSubclass(Enum.class), prefix));
        } else if (type.isArray()) {
            return Types.<Converter<V>>cast(forArray(createConverter(type.getComponentType(), prefix)));
        } else {
            return getDefaultConverter(type);
        }
    }
    
    // Annotation
    
    /**
     * Determines if the specified annotation is either converter annotation
     * or custom converter annotation.
     * 
     * @param annotation Annotation to be tested.
     * @return <code>true</code> if the specified annotation is either
     *         converter annotation or custom converter annotation;
     *         <code>false</code> otherwise.
     */
    public static boolean isConverterAnnotation(Annotation annotation) {
        return isAnnotationPresent(annotation, ConvertedBy.class);
    }
    
    /**
     * Creates a new converter for the specified generic value type using
     * configuration from the specified annotations.
     * 
     * <p>Annotations that are not converter annotations will be skipped.
     * Simple converters require one or none annotations. In case
     * array/collection/map value type additional tokenizer annotation
     * can be specified. If there are no annotations specified (empty array)
     * then default converter will be returned.</p>
     * 
     * <p>The converter returned may be instance of <code>ConverterMessageWrapper</code>
     * if annotation contains overriding message template.</p>
     * 
     * @param <V> The value type.
     * @param type The type of value to be converted.
     * @param annotations Array of annotations.
     * @param namespace Namespace (for example, qualified property name).
     * @return A new converter.
     * @throws ValidationTargetException if there is an error in converter declaration.
     * @see #createConverter(Annotation, Class, String)
     * @see #createConverter(Class, String)
     */
    public static <V> Converter<V> createConverter(Type type, Annotation[] annotations,
            String namespace) {
        Class<V> valueType = Types.rawTypeOf(type);
        Class<Object> elementType = Types.elementTypeOf(type);
        Class<Object> keyType = Types.keyTypeOf(type);
        
        Tokenizer tokenizer = null;
        Converter<V> valueConverter = null;
        Converter<Object> keyConverter = null;
        Converter<Object> elementConverter = null;
        
        for (Annotation list : annotations)
            for (Annotation annotation : getAnnotationList(list)) {
                Converter<?> converter = null;
                for (ValidationTarget target : getAnnotationTargets(annotation)) {
                    if (target == ValidationTarget.KEYS) {
                        if (keyType == null)
                            throw new ValidationTargetException(annotation, target);
                        converter = createConverter(annotation, keyType, namespace);
                        if (converter != null) {
                            if (keyConverter != null)
                                throw new ValidationTargetException(annotation, target);
                            keyConverter = (Converter<Object>) converter;
                        }
                    } else if (target == ValidationTarget.ELEMENTS) {
                        if (elementType == null)
                            throw new ValidationTargetException(annotation, target);
                        converter = createConverter(annotation, elementType, namespace);
                        if (converter != null) {
                            if (elementConverter != null)
                                throw new ValidationTargetException(annotation, target);
                            elementConverter = (Converter<Object>) converter;
                        }
                    } else {
                        try {
                            converter = createConverter(annotation, valueType, namespace);
                            if (converter != null) {
                                if (tokenizer != null)
                                    throw new ValidationTargetException(annotation, target);
                                if (valueConverter != null || keyConverter != null || elementConverter != null)
                                    throw new ValidationTargetException(annotation, target);
                                valueConverter = (Converter<V>) converter;
                            }
                        } catch (ValidationTypeException e1) {
                            if (elementType != null) {
                                try {
                                    converter = createConverter(annotation, String[].class, namespace);
                                    if (converter instanceof Tokenizer) {
                                        if (tokenizer != null)
                                            throw new ValidationTargetException(annotation, target);
                                        tokenizer = (Tokenizer) converter;
                                    }
                                } catch (ValidationTypeException e2) {}
                            }
                            throw e1;
                        }
                    }
                }
            }
        
        if (valueConverter == null) {
            if (elementType == null) {
                valueConverter = createConverter(valueType, namespace);
            } else {
                if (tokenizer == null)
                    tokenizer = SimpleTokenizer.DEFAULT;
                if (elementConverter == null)
                    elementConverter = createConverter(elementType, namespace);
                if (keyType == null) {
                    valueConverter = valueType.isArray()
                        ? (Converter<V>) forArray(elementConverter, tokenizer)
                        : (Converter<V>) forCollection((Class<? extends Collection>) valueType,
                                elementConverter, tokenizer);
                } else {
                    if (keyConverter == null)
                        keyConverter = createConverter(keyType, namespace);
                    valueConverter = (Converter<V>) forMap((Class<? extends Map>) valueType,
                            keyConverter, elementConverter, tokenizer);
                }
            }
        }
        
        return valueConverter;
    }
    
    /**
     * Creates a new converter for the specified raw value type using
     * configuration from the specified annotation.
     * 
     * <p>This method may return <code>null</code> if the specified annotation
     * is not a converter annotation. This method is responsible for creation
     * converters of raw types only.</p>
     * 
     * <p>The converter returned may be instance of <code>ConverterMessageWrapper</code>
     * if annotation contains overriding message template.</p>
     * 
     * @param <V> The value type.
     * @param annotation Converter annotation.
     * @param type The type of value to be converted.
     * @param namespace Namespace (for example, qualified property name).
     * @return A new converter or <code>null</code> if the specified annotation
     *         is not a converter annotation.
     * @throws ValidationDeclarationException if there is an error in converter
     *         declaration.
     */
    private static <V> Converter<V> createConverter(Annotation annotation, Class<V> type, String namespace) {
        Class<? extends Annotation> annotationType = annotation.annotationType();
        ConvertedBy convertedBy = annotationType.getAnnotation(ConvertedBy.class);
        
        if (convertedBy == null) {
            if (isConverterAnnotation(annotation)) {
                Converter<V> converter = (Converter<V>) getFromCache(annotationType, type);
                if (converter == null) {
                    converter = createConverter(type, annotationType.getAnnotations(), null);
                    converter = new CustomConverter<V>(converter);
                    addToCache(annotationType, converter);
                }
                return wrapConverter(converter, annotation, namespace);
            }
            return null;
        }
        
        for (Class<? extends Converter> converterType : convertedBy.value())
            if (Types.parameterTypeOf(converterType, Converter.class, 0) == Types.wrapperTypeOf(type)) {
                Converter<V> converter = (Converter<V>) createValidation(
                        (Class<Converter<?>>) converterType, annotation, type);
                if (converter.getType() == type)
                    return wrapConverter(converter, annotation, namespace);
            }
        
        throw new ValidationTypeException(annotation, annotationType);
    }
    
    /**
     * Wraps the specified converter using information from the specified
     * annotatation.
     * 
     * @param <V> The value type.
     * @param converter Converter to be wrapped.
     * @param annotation Converter annotation.
     * @param namespace Namespace (for example, qualified property name).
     * @return A new wrapped converter or the specified one if there are
     *         nothing to wrap.
     */
    private static <V> Converter<V> wrapConverter(Converter<V> converter, Annotation annotation,
            String namespace) {
        String message = getAnnotationMessage(annotation, namespace);
        return message == null ? converter : wrapMessage(converter, message);
    }
    
    // Helper methods
    
    /**
     * Default <code>Validator.ContextBuilder</code> to be used for
     * <code>encode()</code> and <code>decode()</code> methods.
     */
    private static final Validator<?>.ContextBuilder defaultContext =
            ValidatorFactory.getDefault().newValidator(Object.class).newContext();
    
    /**
     * Converts default string representation of value into object of the
     * specified type using registered converter.
     * 
     * @param <V> The value type.
     * @param type The type of value to be converted.
     * @param value Default string representation of value.
     * @return Decoded value.
     * @throws MalformedValueException if conversion fails.
     */
    public static <V> V decode(Class<V> type, String value) {
        return defaultContext.decodeValue(getDefaultConverter(type), value);
    }
    
    /**
     * Converts array of default string representations of values into array of
     * objects of the specified type using registered converter.
     * 
     * @param <V> The array elements type.
     * @param type The type of value to be converted.
     * @param values Array of default string representations of values.
     * @return Decoded array of values.
     * @throws MalformedValueException if conversion fails.
     */
    public static <V> V[] decode(Class<V> type, String... values) {
        return defaultContext.decodeValues(getDefaultConverter(type), values);
    }
    
    /**
     * Converts value into default string representation using registered
     * converter.
     * 
     * @param <V> The value type.
     * @param type The type of value to be converted.
     * @param value Value to be encoded.
     * @return Default string representation of value.
     */
    public static <V> String encode(Class<V> type, V value) {
        return defaultContext.encodeValue(getDefaultConverter(type), value);
    }
    
    /**
     * Converts array of values into array of their raw string representations
     * using registered converter.
     * 
     * @param <V> The array elements type.
     * @param type The type of value to be converted.
     * @param values Array of values to be encoded.
     * @return Array of default string representations of values.
     */
    public static <V> String[] encode(Class<V> type, V... values) {
        return defaultContext.encodeValues(getDefaultConverter(type), values);
    }
    
}
