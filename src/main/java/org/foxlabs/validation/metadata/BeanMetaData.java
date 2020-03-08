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

package org.foxlabs.validation.metadata;

import java.lang.annotation.Annotation;

import java.util.Map;
import java.util.HashMap;
import java.util.LinkedHashMap;

import org.foxlabs.validation.ValidationDeclarationException;
import org.foxlabs.validation.ValidationDefaults;
import org.foxlabs.validation.constraint.Constraint;
import org.foxlabs.validation.constraint.ConstraintFactory;
import org.foxlabs.validation.converter.Converter;
import org.foxlabs.validation.converter.ConverterFactory;

import org.foxlabs.common.Predicates;

import org.foxlabs.util.reflect.Types;
import org.foxlabs.util.reflect.PropertyGetter;
import org.foxlabs.util.reflect.PropertySetter;
import org.foxlabs.util.reflect.BeanIntrospector;

/**
 * This class provides <code>EntityMetaData</code> implementation for java
 * beans.
 *
 * @author Fox Mulder
 * @param <T> The type of bean
 */
public class BeanMetaData<T> extends AbstractEntityMetaData<T> {

    /**
     * The type of bean.
     */
    protected final Class<T> type;

    /**
     * Constructs a new <code>BeanMetaData</code> with the specified bean type,
     * constraint and properties metadata.
     *
     * @param type The type of bean.
     * @param constraint Constraint to be used for bean validation.
     * @param properties Metadata of all the properties defined on the bean.
     */
    protected BeanMetaData(Class<T> type, Constraint<? super T> constraint,
            Map<String, Property<T, Object>> properties) {
        super(constraint, properties);
        this.type = type;
    }

    /**
     * Returns bean type.
     *
     * @return Bean type.
     */
    @Override
    public final Class<T> getType() {
        return type;
    }

    // Property

    /**
     * This class provides <code>PropertyMetaData</code> implementation for
     * java beans.
     *
     * @author Fox Mulder
     * @param <V> The type of property
     */
    protected static class Property<T, V> extends AbstractPropertyMetaData<T, V> {

        /**
         * Property wrapped type.
         */
        protected final Class<V> wtype;

        /**
         * Property value getter.
         */
        protected final PropertyGetter getter;

        /**
         * Property value setter.
         */
        protected final PropertySetter setter;

        /**
         * Constructs a new <code>BeanPropertyMetaData</code> with the specified
         * name, converter, constraint, property getter and setter.
         *
         * @param name Property name.
         * @param converter Converter to be used for property value conversion
         *        into and from string representation.
         * @param constraint Constraint to be used for property value validation.
         * @param getter Property value getter.
         * @param setter Property value setter.
         * @param defaultValue Property default value.
         * @throws IllegalArgumentException if the specified name or converter
         *         is <code>null</code> or name is empty.
         */
        protected Property(String name, Converter<V> converter, Constraint<? super V> constraint,
                PropertyGetter getter, PropertySetter setter, V defaultValue) {
            super(name, converter, constraint, defaultValue);
            this.wtype = Types.wrapperTypeOf(getType());
            this.getter = getter;
            this.setter = setter;
        }

        /**
         * Determines if this property has getter.
         *
         * @return <code>true</code> if this property has getter;
         *         <code>false</code> otherwise.
         */
        @Override
        public final boolean isReadable() {
            return getter != null;
        }

        /**
         * Determines if this property has setter.
         *
         * @return <code>true</code> if this property has setter;
         *         <code>false</code> otherwise.
         */
        @Override
        public final boolean isWriteable() {
            return setter != null;
        }

        /**
         * Casts the specified value to this property type.
         *
         * @param value Value to be cast.
         * @return Value after casting.
         * @throws ClassCastException if the specified value is not assignable
         *         to this property type.
         */
        @Override
        public V cast(Object value) {
            return wtype.cast(value);
        }

        /**
         * Returns value of this property for the specified bean.
         *
         * @param bean Bean whose property value should be returned.
         * @return Value of this property for the specified bean.
         * @throws IllegalArgumentException if this specified bean is
         *         <code>null</code>.
         * @throws UnsupportedOperationException if this property is not
         *         readable.
         */
        @Override
        public final V getValue(T bean) {
            if (getter == null)
                throw new UnsupportedOperationException();
            if (bean == null)
                throw new IllegalArgumentException();
            return cast(getter.getValue(bean));
        }

        /**
         * Assigns value of this property for the specified bean.
         *
         * @param bean Bean whose property value should be assigned.
         * @param value New property value.
         * @throws IllegalArgumentException if this specified bean is
         *         <code>null</code>.
         * @throws UnsupportedOperationException if this property is not
         *         writeable.
         */
        @Override
        public final void setValue(T bean, Object value) {
            if (setter == null)
                throw new UnsupportedOperationException();
            if (bean == null)
                throw new IllegalArgumentException();
            setter.setValue(bean, value == null ? defaultValue : cast(value));
        }

    }

    // Factory methods

    /**
     * Bean metadata cache.
     */
    private static final Map<Class<?>, BeanMetaData<?>> metaCache =
            new HashMap<Class<?>, BeanMetaData<?>>();

    /**
     * Returns bean metadata for the specified type.
     *
     * @param <T> The bean type.
     * @param type Type for which bean metadata should be returned.
     * @return Bean metadata for the specified type.
     * @throws BeanDefinitionException if bean has illegal validation definition.
     */
    public synchronized static <T> BeanMetaData<T> getMetaData(Class<?> type) {
        Predicates.require(type, Types::isObject, "type");
        BeanMetaData<T> meta = Types.cast(metaCache.get(type));
        if (meta == null) {
            boolean failure = true;
            try {
                BeanIntrospector introspector = BeanIntrospector.getInstance(type);
                ValidationDefaults defaults = introspector.getAnnotation(ValidationDefaults.class);
                Constraint<? super T> constraint = createConstraint(introspector, defaults);
                Map<String, Property<T, Object>> properties = new LinkedHashMap<String, Property<T, Object>>();
                meta = new BeanMetaData<T>(Types.<Class<T>>cast(type), constraint, properties);
                metaCache.put(type, meta); // to avoid recursion in CascadeConstraint
                findProperties(meta, properties, introspector, defaults);
                failure = false;
            } finally {
                if (failure)
                    metaCache.remove(type);
            }
        }
        return meta;
    }

    /**
     * Creates bean constraint.
     *
     * @param introspector Bean introspector.
     * @param defaults Validation default settings.
     * @return Bean constraint.
     * @throws BeanDefinitionException if bean has illegal constraint definition.
     */
    private static <T> Constraint<? super T> createConstraint(BeanIntrospector introspector,
            ValidationDefaults defaults) {
        try {
            Class<T> type = Types.cast(introspector.getType());
            return ConstraintFactory.createConstraint(type, introspector.getAnnotations(),
                    type.getName(), defaults);
        } catch (ValidationDeclarationException e) {
            throw new BeanDefinitionException(introspector.getType(), e);
        }
    }

    /**
     * Searches for bean properties.
     *
     * @param meta Bean metadata.
     * @param properties Property map to be filled.
     * @param introspector Bean introspector.
     * @param defaults Validation default settings.
     * @throws BeanDefinitionException if bean has illegal property definition.
     */
    private static <T> void findProperties(BeanMetaData<T> meta,
            Map<String, Property<T, Object>> properties, BeanIntrospector introspector,
            ValidationDefaults defaults) {
        Class<T> type = meta.getType();
        for (BeanIntrospector.Property property : introspector.getProperties()) {
            try {
                String name = property.getName();
                String namespace = type.getName() + "." + name;
                Annotation[] annotations = property.getAnnotations();
                Converter<Object> converter = ConverterFactory.createConverter(
                        property.getGenericType(), annotations, namespace);
                Constraint<? super Object> constraint = ConstraintFactory.createConstraint(
                        property.getGenericType(), annotations, namespace, defaults);
                Object defaultValue = constraint == null ? null : ConstraintFactory.findDefaultValue(constraint);
                properties.put(name, new Property<T, Object>(name, converter, constraint,
                        property.getGetter(), property.getSetter(), defaultValue));
            } catch (ValidationDeclarationException e) {
                throw new BeanDefinitionException(type, property.getName(), e);
            }
        }
    }

}
