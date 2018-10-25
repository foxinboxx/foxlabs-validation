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

package org.foxlabs.validation.support;

import java.lang.annotation.Annotation;

import java.lang.reflect.Method;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import java.util.Set;
import java.util.HashSet;
import java.util.Collections;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ConcurrentHashMap;

import org.foxlabs.validation.Validation;
import org.foxlabs.validation.ValidationInstantiationException;
import org.foxlabs.validation.ValidationSignatureException;
import org.foxlabs.validation.ValidationTarget;
import org.foxlabs.validation.ValidationTypeException;
import org.foxlabs.validation.Validator;

import org.foxlabs.util.reflect.Types;

/**
 * This class provides annotation support methods and maintains validation
 * components cache.
 * 
 * <p>The support methods allow to extract annotation properties used for
 * building validation components.</p>
 * 
 * <p>The cache used to store <code>Validation</code> instances having
 * corresponding annotations. If <code>Validation</code> implementation class
 * has default constructor or constructor that accepts type of the value to be
 * validated then instances of this class can be cached. In other cases
 * <code>Validation</code> instance can't be cached because its constructor
 * takes arguments that can differ in each instance for the same annotation
 * type and value type. The cache stores such validation components as
 * key-value pairs where value is validation component instance and key is
 * combination of annotation type and type of the value to be validated.</p>
 * 
 * <p><code>AnnotationSupport</code> is thread-safe.</p>
 * 
 * @author Fox Mulder
 * @see org.foxlabs.validation.Validation
 */
public abstract class AnnotationSupport {
    
    // Annotation cache
    
    /**
     * The cache used to store <code>Validation</code> instances.
     */
    private static final ConcurrentMap<CacheKey, Validation<?>> cache =
            new ConcurrentHashMap<CacheKey, Validation<?>>();
    
    /**
     * Cache key of <code>Validation</code> instances.
     * 
     * @author Fox Mulder
     */
    private static final class CacheKey {
        
        /**
         * Annotation type.
         */
        private final Class<? extends Annotation> annotationType;
        
        /**
         * The type of a value to be validated.
         */
        private final Class<?> valueType;
        
        /**
         * Constructs a new <code>CacheKey</code> with the specified annotation
         * and value types.
         * 
         * @param annotationType Annotation type.
         * @param valueType The type of a value to be validated.
         */
        private CacheKey(Class<? extends Annotation> annotationType, Class<?> valueType) {
            this.annotationType = annotationType;
            this.valueType = valueType;
        }
        
        /**
         * Returns a hash code value for this key.
         * 
         * @return A hash code value for this key.
         */
        public int hashCode() {
            return annotationType.hashCode() ^ valueType.hashCode();
        }
        
        /**
         * Determines if this key equals to the specified one.
         * 
         * @param obj Another key.
         * @return <code>true</code> if this key equals to the specified one;
         *         <code>false</code> otherwise.
         */
        public boolean equals(Object obj) {
            return ((CacheKey) obj).annotationType == annotationType
                && ((CacheKey) obj).valueType == valueType;
        }
        
    }
    
    /**
     * Adds validation component to the cache.
     * 
     * @param annotationType Annotation type.
     * @param validation Validation component to be cached.
     */
    protected static void addToCache(Class<? extends Annotation> annotationType,
            Validation<?> validation) {
        addToCache(annotationType, validation, validation.getType());
    }
    
    /**
     * Adds validation component to the cache.
     * 
     * @param annotationType Annotation type.
     * @param validation Validation component to be cached.
     * @param valueType The type of a value to be validated.
     */
    protected static void addToCache(Class<? extends Annotation> annotationType,
            Validation<?> validation, Class<?> valueType) {
        cache.put(new CacheKey(annotationType, valueType), validation);
    }
    
    /**
     * Returns validation component from cache for the specified annotation and
     * value types.
     * 
     * @param annotationType Annotation type.
     * @param valueType The type of a value to be validated.
     * @return Validation component from cache for the specified annotation and
     *         value types or <code>null</code> if cache has no such entry.
     */
    protected static Validation<?> getFromCache(Class<? extends Annotation> annotationType,
            Class<?> valueType) {
        return cache.get(new CacheKey(annotationType, valueType));
    }
    
    // Factory methods
    
    /**
     * Creates a new instance of the specified validation component type.
     * 
     * <p>The validation component implementation class can define constructor
     * with any access modifier. The following are valid validation component
     * constructors in descending order of priority:</p>
     * <ul>
     *   <li><code>Validation(Annotation, Class)</code></li>
     *   <li><code>Validation(Class, Annotation)</code></li>
     *   <li><code>Validation(Annotation)</code></li>
     *   <li><code>Validation(Class)</code></li>
     *   <li><code>Validation()</code></li>
     * </ul>
     * For example:
     * <pre>
     *   DefaultValueConstraint(Class&lt;V&gt; type, DefaultValue annotation) {
     *       ...
     *   }
     * </pre>
     * 
     * <p>The returned validation component can be obtained from the cache if
     * it was cached previously. Also this method automatically adds validation
     * components to the cache if possible.</p>
     * 
     * @param <T> The validation type.
     * @param validationType Type of the validation component.
     * @param annotation Annotation of the validation component.
     * @param valueType Type of the value to be validated by the specified
     *        validation component type.
     * @return A new or cached validation component instance.
     * @throws ValidationTypeException if the specified value type is not
     *         supported by the specified validation component type.
     * @throws ValidationInstantiationException if validation component
     *         instantiation fails.
     * @throws ValidationSignatureException if validation component doesn't
     *         define valid constructor.
     */
    protected static <T extends Validation<?>> T createValidation(Class<T> validationType,
            Annotation annotation, Class<?> valueType) {
        Class<? extends Annotation> annotationType = annotation.annotationType();
        CacheKey key = new CacheKey(annotationType, valueType);
        T validation = validationType.cast(cache.get(key));
        if (validation != null)
            return validation;
        
        Constructor<T> constructor;
        Object[] arguments;
        boolean cacheable = false;
        try {
            constructor = validationType.getDeclaredConstructor(annotationType, Class.class);
            arguments = new Object[]{annotation, valueType};
        } catch (NoSuchMethodException e1) {
            try {
                constructor = validationType.getDeclaredConstructor(Class.class, annotationType);
                arguments = new Object[]{valueType, annotation};
            } catch (NoSuchMethodException e2) {
                try {
                    constructor = validationType.getDeclaredConstructor(annotationType);
                    arguments = new Object[]{annotation};
                } catch (NoSuchMethodException e3) {
                    try {
                        constructor = validationType.getDeclaredConstructor(Class.class);
                        arguments = new Object[]{valueType};
                        cacheable = true;
                    } catch (NoSuchMethodException e4) {
                        try {
                            constructor = validationType.getDeclaredConstructor();
                            arguments = new Object[]{};
                            cacheable = true;
                        } catch (NoSuchMethodException e5) {
                            throw new ValidationSignatureException(annotation, validationType);
                        }
                    }
                }
            }
        }
        
        try {
            constructor.setAccessible(true);
            validation = constructor.newInstance(arguments);
            if (cacheable)
                cache.put(key, validation);
            return validationType.cast(validation);
        } catch (InvocationTargetException e) {
            Throwable cause = e.getTargetException();
            if (cause instanceof UnsupportedOperationException)
                throw new ValidationTypeException(annotation, valueType);
            throw new ValidationInstantiationException(annotation, cause);
        } catch (IllegalAccessException e) {
            throw new ValidationInstantiationException(annotation, e);
        } catch (InstantiationException e) {
            throw new ValidationInstantiationException(annotation, e);
        }
    }
    
    // Annotation support
    
    /**
     * Default set of validation targets.
     */
    protected static final Set<ValidationTarget> DEFAULT_TARGET_SET =
            Collections.singleton(ValidationTarget.VALUE);
    
    /**
     * Default set of constraint groups.
     */
    protected static final Set<String> DEFAULT_GROUP_SET =
            Collections.singleton(Validator.DEFAULT_GROUP);
    
    /**
     * Returns array of annotations defined by the specified inner
     * <code>List</code> annotation or array with single element which
     * contains the specified annotation if it not conforms to the rules of
     * annotation list definition.
     * 
     * <p>Inner <code>List</code> annotation should define <code>value</code>
     * property of an outer annotation array type.</p>
     * 
     * @param annotation Annotation of the validation component.
     * @return Array of annotations defined by the specified inner
     *         <code>List</code> annotation or array with single element which
     *         contains the specified annotation if it not conforms to the
     *         rules of annotation list definition.
     */
    protected static Annotation[] getAnnotationList(Annotation annotation) {
        Annotation[] list = null;
        Class<? extends Annotation> annotationType = annotation.annotationType();
        if ("List".equals(annotationType.getSimpleName())) {
            Class<?> itemType = annotationType.getEnclosingClass();
            if (itemType != null && itemType.isAnnotation())
                list = (Annotation[]) getAnnotationProperty(annotation, "value",
                        Types.arrayTypeOf(itemType));
        }
        return list == null ? new Annotation[]{annotation} : list;
    }
    
    /**
     * Returns <code>message</code> property value from the specified
     * annotation or default annotation message if annotation has no such
     * property.
     * 
     * <p>Default annotation message should be defined as follows:</p>
     * <pre>namespace + "." + annotation.annotationType().getSimpleName()</pre>
     * For example, default message for the
     * {@link org.foxlabs.validation.constraint.NotNull} annotation and
     * <code>value</code> namespace should be <code>value.NotNull</code>.
     * 
     * @param annotation Annotation of the validation component.
     * @param namespace Namespace of error message template key.
     * @return <code>message</code> property value from the specified
     *         annotation or default annotation message if annotation has no
     *         such property.
     */
    protected static String getAnnotationMessage(Annotation annotation, String namespace) {
        String message = getAnnotationProperty(annotation, "message", String.class);
        if (!(message == null || message.isEmpty()))
            return message;
        if (namespace == null || namespace.isEmpty())
            return null;
        return namespace + "." + annotation.annotationType().getSimpleName();
    }
    
    /**
     * Returns <code>targets</code> property value from the specified annotation
     * or default target set if annotation has no such property.
     * 
     * @param annotation Annotation of the validation component.
     * @return <code>targets</code> property value from the specified annotation
     *         or default target set if annotation has no such property.
     */
    protected static Set<ValidationTarget> getAnnotationTargets(Annotation annotation) {
        ValidationTarget[] targets = getAnnotationProperty(annotation, "targets",
                ValidationTarget[].class);
        if (targets == null || targets.length == 0)
            return DEFAULT_TARGET_SET;
        Set<ValidationTarget> targetSet = new HashSet<ValidationTarget>();
        for (ValidationTarget target : targets)
            targetSet.add(target);
        return targetSet;
    }
    
    /**
     * Returns <code>groups</code> property value from the specified annotation
     * or the specified default groups if annotation has no such property and
     * array of default groups is not empty; returns <code>null</code>
     * otherwise.
     * 
     * @param annotation Annotation of the validation component.
     * @param defaults Array of default groups.
     * @return <code>groups</code> property value from the specified annotation
     *         or the specified default groups if annotation has no such
     *         property and array of default groups is not empty; returns
     *         <code>null</code> otherwise.
     */
    protected static String[] getAnnotationGroups(Annotation annotation, String... defaults) {
        String[] groups = getAnnotationProperty(annotation, "groups", String[].class);
        if (groups == null || groups.length == 0) {
            if (defaults == null || defaults.length == 0)
                return null;
            return defaults;
        }
        return groups;
    }
    
    /**
     * Returns annotation property value for the specified property name and
     * type or <code>null</code> if the specified annotation has no property
     * with such name and type.
     * 
     * @param <T> The expected annotation property type.
     * @param annotation Annotation.
     * @param property Property name.
     * @param type Expected property type.
     * @return Annotation property value for the specified property name and
     *         type or <code>null</code> if the specified annotation has no
     *         property with such name and type.
     */
    protected static <T> T getAnnotationProperty(Annotation annotation, String property, Class<T> type) {
        try {
            Method method = annotation.annotationType().getMethod(property);
            Object value = method.invoke(annotation);
            return type == value.getClass() ? type.cast(value) : null;
        } catch (InvocationTargetException e) {
            throw new RuntimeException(e.getTargetException());
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        } catch (ClassCastException e) {
            return null;
        } catch (NoSuchMethodException e) {
            return null;
        }
    }
    
    /**
     * Determines recursively if the specified annotation has an annotation of
     * required type.
     * 
     * @param annotation Annotation to be tested.
     * @param requiredType Required annotation type.
     * @return <code>true</code> if the specified annotation has an annotation
     *         of required type; <code>false</code> otherwise.
     */
    protected static boolean isAnnotationPresent(Annotation annotation,
            Class<? extends Annotation> requiredType) {
        return isAnnotationPresent(annotation, requiredType, new HashSet<Class<?>>());
    }
    
    /**
     * Determines recursively if the specified annotation has an annotation of
     * required type.
     * 
     * @param annotation Annotation to be tested.
     * @param requiredType Required annotation type.
     * @param checkedTypes Set of already checked types.
     * @return <code>true</code> if the specified annotation has an annotation
     *         of required type; <code>false</code> otherwise.
     */
    private static boolean isAnnotationPresent(Annotation annotation,
            Class<? extends Annotation> requiredType, Set<Class<?>> checkedTypes) {
        Class<? extends Annotation> annotationType = annotation.annotationType();
        if (annotationType.isAnnotationPresent(requiredType))
            return true;
        if (checkedTypes.contains(annotationType))
            return false;
        checkedTypes.add(annotationType);
        Annotation[] annotations = annotationType.getAnnotations();
        for (int i = 0; i < annotations.length; i++) {
            Annotation[] list = getAnnotationList(annotations[i]);
            for (int j = 0; j < list.length; j++)
                if (isAnnotationPresent(list[j], requiredType, checkedTypes))
                    return true;
        }
        return false;
    }
    
}
