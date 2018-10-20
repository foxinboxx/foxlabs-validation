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

import java.util.*;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock.*;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

import java.io.File;
import java.io.Reader;
import java.io.FileReader;
import java.io.BufferedReader;
import java.io.Writer;
import java.io.FileWriter;
import java.io.BufferedWriter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.IOException;

import org.foxlabs.validation.constraint.Constraint;
import org.foxlabs.validation.converter.Converter;
import org.foxlabs.validation.metadata.PropertyFilter;
import org.foxlabs.validation.metadata.PropertyMetaData;

/**
 * This class represents map with constrained entries. The number of entries is
 * constant and depends on the provided metadata. Metadata for this map can be
 * builded by the {@link org.foxlabs.validation.metadata.MapMetaData.Builder}
 * class.
 * 
 * <p>Example of usage:
 * <pre>
 *   MapMetaData.Builder builder = new MapMetaData.Builder();
 *   builder.property(...)
 *          .property(...)
 *          .property(...);
 *   MapMetaData metadata = builder.build();
 *   new ConstrainedMap(ValidatorFactory.getDefault().newValidator(metadata));
 * </pre></p>
 * 
 * <p><code>ConstrainedMap</code> is thread-safe. It makes this map useful for
 * maintaining configurations in the multi-threaded environment. The method
 * {@link #newTransaction()} allows to change map values in transactional way
 * and keep integrity.</p>
 * 
 * @author Fox Mulder
 * @see Validator
 */
public class ConstrainedMap implements Map<String, Object>, Iterable<ConstrainedMap.Entry<?>> {
    
    /**
     * Validator of map entries.
     */
    private final Validator<Map<String, Object>> validator;
    
    /**
     * Map entries.
     */
    private final Map<String, Entry<?>> dataset;
    
    /**
     * Read access lock.
     */
    private final ReadLock readLock;
    
    /**
     * Write access lock.
     */
    private final WriteLock writeLock;
    
    /**
     * Constructs a new <code>ConstrainedMap</code> with the specified
     * validator of map entries.
     * 
     * @param validator Validator of map entries.
     */
    public ConstrainedMap(Validator<Map<String, Object>> validator) {
        this.validator = validator;
        
        // create entries according to the metadata properties
        this.dataset = new LinkedHashMap<String, Entry<?>>();
        for (PropertyMetaData<Map<String, Object>, Object> meta : validator.getMetaData().getAllPropertyMetaData())
            dataset.put(meta.getName(), new Entry<Object>(meta));
        
        // initialize locks
        ReentrantReadWriteLock lock = new ReentrantReadWriteLock();
        this.readLock = lock.readLock();
        this.writeLock = lock.writeLock();
    }
    
    /**
     * Returns validator of this map.
     * 
     * @return Validator of this map.
     */
    public final Validator<Map<String, Object>> getValidator() {
        return validator;
    }
    
    // Query operations
    
    /**
     * Returns the number of entries in this map.
     * 
     * @return The number of entries in this map.
     * @see Map#size()
     */
    public final int size() {
        readLock.lock();
        try {
            return dataset.size();
        } finally {
            readLock.unlock();
        }
    }
    
    /**
     * Determines if this map contains no entries.
     * 
     * @return <code>true</code> if this map contains no entries;
     *         <code>false</code> otherwise.
     * @see Map#isEmpty()
     */
    public final boolean isEmpty() {
        return size() == 0;
    }
    
    /**
     * Determines if this map contains an entry for the specified key.
     * 
     * @param key Key whose presence in this map to be tested.
     * @return <code>true</code> if this map contains an entry for the
     *         specified key; <code>false</code> otherwise.
     * @see Map#containsKey(Object)
     */
    public final boolean containsKey(Object key) {
        readLock.lock();
        try {
            return dataset.containsKey(key);
        } finally {
            readLock.unlock();
        }
    }
    
    /**
     * Determines if this map contains one or more keys with the specified
     * value.
     * 
     * @param value Value whose presence in this map to be tested.
     * @return <code>true</code> if this map contains one or more keys with the
     *         specified value; <code>false</code> otherwise.
     * @see Map#containsValue(Object)
     */
    public final boolean containsValue(Object value) {
        readLock.lock();
        try {
            for (Entry<?> entry : dataset.values())
                if (value == null ? entry.value == null : value.equals(entry.value))
                    return true;
            return false;
        } finally {
            readLock.unlock();
        }
    }
    
    /**
     * Returns value to which the specified key is mapped.
     * 
     * @param key Key whose associated value to be returned.
     * @return Value to which the specified key is mapped.
     * @throws IllegalArgumentException if this map contains no entry for the
     *         specified key.
     * @see Map#get(Object)
     * @see #getValue(Object)
     */
    public final Object get(Object key) {
        return getValue(key);
    }
    
    // Modification operations
    
    /**
     * Assigns the specified value for the specified key in this map.
     * 
     * @param key Key for which the specified value to be assigned.
     * @param value Value to be assigned for the specified key.
     * @return Previous value assigned for the specified key.
     * @throws IllegalArgumentException if this map contains no entry for the
     *         specified key.
     * @throws ValidationException if violations occured.
     * @see Map#put(Object, Object)
     * @see #setValue(Object, Object)
     */
    public final Object put(String key, Object value) {
        return setValue(key, value);
    }
    
    /**
     * Resets value of the specified key to its default value.
     * 
     * @param key Key for which the specified value to be reseted.
     * @return Previous value assigned for the specified key.
     * @throws IllegalArgumentException if this map contains no entry for the
     *         specified key.
     * @throws ValidationException if violations occured.
     * @see Map#remove(Object)
     * @see #resetValue(Object)
     */
    public final Object remove(Object key) {
        return resetValue(key);
    }
    
    // Bulk operations
    
    /**
     * Assigns the specified map values to this map values.
     * 
     * @param values Values to be stored in this map.
     * @throws ValidationException if violations occured.
     * @see Map#putAll(Map)
     * @see #setValues(Map)
     */
    @SuppressWarnings("unchecked")
    public final void putAll(Map<? extends String, ? extends Object> values) {
        setValues((Map<String, Object>) values);
    }
    
    /**
     * Resets all the values of this map to its default values.
     * 
     * @throws ValidationException if violations occured.
     * @see Map#clear()
     * @see #resetValues()
     */
    public final void clear() {
        resetValues();
    }
    
    // Advanced operations
    
    /**
     * Returns map entry for the specified key.
     * 
     * @param key Key for which entry to be returned.
     * @return Map entry for the specified key.
     * @throws IllegalArgumentException if this map contains no entry for the
     *         specified key.
     */
    public final <V> Entry<V> getEntry(String key) {
        readLock.lock();
        try {
            return entryFor(key);
        } finally {
            readLock.unlock();
        }
    }
    
    /**
     * Returns value to which the specified key is mapped.
     * 
     * @param key Key whose associated value to be returned.
     * @return Value to which the specified key is mapped.
     * @throws IllegalArgumentException if this map contains no entry for the
     *         specified key.
     */
    public final <V> V getValue(Object key) {
        readLock.lock();
        try {
            Entry<V> entry = entryFor(key);
            return entry.value;
        } finally {
            readLock.unlock();
        }
    }
    
    /**
     * Assigns the specified value for the specified key in this map.
     * 
     * @param key Key for which the specified value to be assigned.
     * @param value Value to be assigned for the specified key.
     * @return Previous value assigned for the specified key.
     * @throws IllegalArgumentException if this map contains no entry for the
     *         specified key.
     * @throws ValidationException if violations occured.
     * @see #setValue(Object, Object, Locale)
     */
    public final <V> V setValue(Object key, Object value) {
        return setValue(key, value, Locale.getDefault());
    }
    
    /**
     * Assigns the specified value for the specified key in this map.
     * 
     * @param key Key for which the specified value to be assigned.
     * @param value Value to be assigned for the specified key.
     * @param locale Locale to be used for error messages.
     * @return Previous value assigned for the specified key.
     * @throws IllegalArgumentException if this map contains no entry for the
     *         specified key.
     * @throws ValidationException if violations occured.
     * @see Entry#setValue(Object, Locale)
     */
    public final <V> V setValue(Object key, Object value, Locale locale) {
        writeLock.lock();
        try {
            Entry<V> entry = entryFor(key);
            return entry.setValue(value, locale);
        } finally {
            writeLock.unlock();
        }
    }
    
    /**
     * Returns default string representation of value to which the specified
     * key is mapped.
     * 
     * @param key Key whose associated default string representation of value
     *        to be returned.
     * @return Default string representation of value to which the specified
     *         key is mapped.
     * @throws IllegalArgumentException if this map contains no entry for the
     *         specified key.
     * @see Entry#getRawValue()
     */
    public final String getRawValue(Object key) {
        readLock.lock();
        try {
            Entry<?> entry = entryFor(key);
            return entry.getRawValue();
        } finally {
            readLock.unlock();
        }
    }
    
    /**
     * Assigns the specified value for the specified key in this map.
     * 
     * @param key Key for which the specified value to be assigned.
     * @param value Default string representation of value to be assigned for
     *        the specified key.
     * @return Previous value assigned for the specified key.
     * @throws IllegalArgumentException if this map contains no entry for the
     *         specified key.
     * @throws ValidationException if violations occured.
     * @see #setRawValue(Object, String, Locale)
     */
    public final Object setRawValue(Object key, String value) {
        return setRawValue(key, value, Locale.getDefault());
    }
    
    /**
     * Assigns the specified value for the specified key in this map.
     * 
     * @param key Key for which the specified value to be assigned.
     * @param value Default string representation of value to be assigned for
     *        the specified key.
     * @param locale Locale to be used for error messages.
     * @return Previous value assigned for the specified key.
     * @throws IllegalArgumentException if this map contains no entry for the
     *         specified key.
     * @throws ValidationException if violations occured.
     * @see Entry#setRawValue(String, Locale)
     */
    public final Object setRawValue(Object key, String value, Locale locale) {
        writeLock.lock();
        try {
            Entry<?> entry = entryFor(key);
            return entry.setRawValue(value, locale);
        } finally {
            writeLock.unlock();
        }
    }
    
    /**
     * Returns localized string representation of value to which the specified
     * key is mapped.
     * 
     * @param key Key whose associated localized string representation of value
     *        to be returned.
     * @return Localized string representation of value to which the specified
     *         key is mapped.
     * @throws IllegalArgumentException if this map contains no entry for the
     *         specified key.
     * @see #getLocalizedValue(Object, Locale)
     */
    public final String getLocalizedValue(Object key) {
        return getLocalizedValue(key, Locale.getDefault());
    }
    
    /**
     * Returns localized string representation of value to which the specified
     * key is mapped.
     * 
     * @param key Key whose associated localized string representation of value
     *        to be returned.
     * @param locale Desired locale.
     * @return Localized string representation of value to which the specified
     *         key is mapped.
     * @throws IllegalArgumentException if this map contains no entry for the
     *         specified key.
     * @see Entry#getLocalizedValue(Locale)
     */
    public final String getLocalizedValue(Object key, Locale locale) {
        readLock.lock();
        try {
            Entry<?> entry = entryFor(key);
            return entry.getLocalizedValue(locale);
        } finally {
            readLock.unlock();
        }
    }
    
    /**
     * Assigns the specified value for the specified key in this map.
     * 
     * @param key Key for which the specified value to be assigned.
     * @param value Localized string representation of value to be assigned for
     *        the specified key.
     * @return Previous value assigned for the specified key.
     * @throws IllegalArgumentException if this map contains no entry for the
     *         specified key.
     * @throws ValidationException if violations occured.
     * @see #setLocalizedValue(Object, String, Locale)
     */
    public final Object setLocalizedValue(Object key, String value) {
        return setLocalizedValue(key, value, Locale.getDefault());
    }
    
    /**
     * Assigns the specified value for the specified key in this map.
     * 
     * @param key Key for which the specified value to be assigned.
     * @param value Localized string representation of value to be assigned for
     *        the specified key.
     * @param locale Desired locale.
     * @return Previous value assigned for the specified key.
     * @throws IllegalArgumentException if this map contains no entry for the
     *         specified key.
     * @throws ValidationException if violations occured.
     * @see Entry#setLocalizedValue(String, Locale)
     */
    public final Object setLocalizedValue(Object key, String value, Locale locale) {
        writeLock.lock();
        try {
            Entry<?> entry = entryFor(key);
            return entry.setLocalizedValue(value, locale);
        } finally {
            writeLock.unlock();
        }
    }
    
    /**
     * Resets value of the specified key to its default value.
     * 
     * @param key Key for which the specified value to be reseted.
     * @return Previous value assigned for the specified key.
     * @throws IllegalArgumentException if this map contains no entry for the
     *         specified key.
     * @throws ValidationException if violations occured.
     * @see #resetValue(Object, Locale)
     */
    public final Object resetValue(Object key) {
        return resetValue(key, Locale.getDefault());
    }
    
    /**
     * Resets value of the specified key to its default value.
     * 
     * @param key Key for which the specified value to be reseted.
     * @param locale Locale to be used for error messages.
     * @return Previous value assigned for the specified key.
     * @throws IllegalArgumentException if this map contains no entry for the
     *         specified key.
     * @throws ValidationException if violations occured.
     * @see Entry#resetValue(Locale)
     */
    public final Object resetValue(Object key, Locale locale) {
        writeLock.lock();
        try {
            Entry<?> entry = entryFor(key);
            return entry.resetValue(locale);
        } finally {
            writeLock.unlock();
        }
    }
    
    /**
     * Returns a snapshot of this map.
     * 
     * @return A snapshot of this map.
     */
    public final Map<String, Object> getValues() {
        readLock.lock();
        try {
            return makeSnapshot();
        } finally {
            readLock.unlock();
        }
    }
    
    /**
     * Assigns the specified map values to this map values.
     * 
     * @param values Values to be stored in this map.
     * @throws ValidationException if violations occured.
     * @see #setValues(Map, Locale)
     */
    public final void setValues(Map<String, Object> values) {
        setValues(values, Locale.getDefault());
    }
    
    /**
     * Assigns the specified map values to this map values.
     * 
     * @param values Values to be stored in this map.
     * @param locale Locale to be used for error messages.
     * @throws ValidationException if violations occured.
     * @see Transaction#setValues(Map)
     */
    public final void setValues(Map<String, Object> values, Locale locale) {
        writeLock.lock();
        try {
            Transaction tx = new Transaction(locale);
            tx.setValues(values);
            tx.commit(false);
        } finally {
            writeLock.unlock();
        }
    }
    
    /**
     * Returns default string representations of this map values.
     * 
     * @return Default string representations of this map values.
     */
    public final Map<String, String> getRawValues() {
        readLock.lock();
        try {
            return validator.getRawValues(this);
        } finally {
            readLock.unlock();
        }
    }
    
    /**
     * Assigns the specified map values to this map values.
     * 
     * @param values Default string representations of values to be stored in
     *        this map.
     * @throws ValidationException if violations occured.
     * @see #setRawValues(Map, Locale)
     */
    public final void setRawValues(Map<String, String> values) {
        setRawValues(values, Locale.getDefault());
    }
    
    /**
     * Assigns the specified map values to this map values.
     * 
     * @param values Default string representations of values to be stored in
     *        this map.
     * @param locale Locale to be used for error messages.
     * @throws ValidationException if violations occured.
     * @see Transaction#setRawValues(Map)
     */
    public final void setRawValues(Map<String, String> values, Locale locale) {
        writeLock.lock();
        try {
            Transaction tx = new Transaction(locale);
            tx.setRawValues(values);
            tx.commit(false);
        } finally {
            writeLock.unlock();
        }
    }
    
    /**
     * Returns localized string representations of this map values.
     * 
     * @return Localized string representations of this map values.
     * @see #getLocalizedValues(Locale)
     */
    public final Map<String, String> getLocalizedValues() {
        return getLocalizedValues(Locale.getDefault());
    }
    
    /**
     * Returns localized string representations of this map values.
     * 
     * @param locale Desired locale.
     * @return Localized string representations of this map values.
     */
    public final Map<String, String> getLocalizedValues(Locale locale) {
        readLock.lock();
        try {
            return validator.newContext()
                            .setMessageLocale(locale)
                            .setLocalizedConvert(true)
                            .getEncodedValues(this);
        } finally {
            readLock.unlock();
        }
    }
    
    /**
     * Assigns the specified map values to this map values.
     * 
     * @param values Localized string representations of values to be stored in
     *        this map.
     * @throws ValidationException if violations occured.
     * @see #setLocalizedValues(Map, Locale)
     */
    public final void setLocalizedValues(Map<String, String> values) {
        setLocalizedValues(values, Locale.getDefault());
    }
    
    /**
     * Assigns the specified map values to this map values.
     * 
     * @param values Localized string representations of values to be stored in
     *        this map.
     * @param locale Desired locale.
     * @throws ValidationException if violations occured.
     * @see Transaction#setLocalizedValues(Map)
     */
    public final void setLocalizedValues(Map<String, String> values, Locale locale) {
        writeLock.lock();
        try {
            Transaction tx = new Transaction(locale);
            tx.setLocalizedValues(values);
            tx.commit(false);
        } finally {
            writeLock.unlock();
        }
    }
    
    /**
     * Resets all the values of this map to its default values.
     * 
     * @throws ValidationException if violations occured.
     * @see #resetValues(Locale)
     */
    public final void resetValues() {
        resetValues(Locale.getDefault());
    }
    
    /**
     * Resets all the values of this map to its default values.
     * 
     * @param locale Locale to be used for error messages.
     * @throws ValidationException if violations occured.
     * @see Transaction#resetValues()
     */
    public final void resetValues(Locale locale) {
        writeLock.lock();
        try {
            Transaction tx = new Transaction(locale);
            tx.resetValues();
            tx.commit(false);
        } finally {
            writeLock.unlock();
        }
    }
    
    /**
     * Creates a new transaction initialized with default locale.
     * 
     * @return A new transaction initialized with default locale.
     * @see #newTransaction(Locale)
     */
    public final Transaction newTransaction() {
        return newTransaction(Locale.getDefault());
    }
    
    /**
     * Creates a new transaction initialized with the specified locale.
     * 
     * @param locale Locale to be used for error messages and value conversions
     *        into and from string representation.
     * @return A new transaction initialized with the specified locale.
     */
    public final Transaction newTransaction(Locale locale) {
        readLock.lock();
        try {
            return new Transaction(locale);
        } finally {
            readLock.unlock();
        }
    }
    
    // View of the map keys
    
    /**
     * Set view of the keys contained in this map.
     */
    private transient Set<String> keySet;
    
    /**
     * Returns set view of the keys contained in this map.
     * 
     * @return Set view of the keys contained in this map.
     * @see Map#keySet()
     */
    public final Set<String> keySet() {
        return keySet == null ? (keySet = new KeySet()) : keySet;
    }
    
    /**
     * Set view of the keys.
     * 
     * @author Fox Mulder
     */
    private final class KeySet extends AbstractSet<String> {
        @Override public int size() {
            return ConstrainedMap.this.size();
        }
        @Override public boolean contains(Object obj) {
            return ConstrainedMap.this.containsKey(obj);
        }
        @Override public Iterator<String> iterator() {
            return new KeyItr();
        }
        @Override public boolean remove(Object obj) {
            return ConstrainedMap.this.remove(obj) != null;
        }
        @Override public void clear() {
            ConstrainedMap.this.resetValues();
        }
    }
    
    /**
     * Iterator of the keys.
     * 
     * @author Fox Mulder
     */
    private final class KeyItr implements Iterator<String> {
        Iterator<Entry<?>> itr = dataset.values().iterator();
        Entry<?> next;
        @Override public boolean hasNext() {
            readLock.lock();
            try {
                return itr.hasNext();
            } finally {
                readLock.unlock();
            }
        }
        @Override public String next() {
            readLock.lock();
            try {
                next = itr.next();
                return next.getKey();
            } finally {
                readLock.unlock();
            }
        }
        @Override public void remove() {
            if (next == null)
                throw new IllegalStateException();
            next.resetValue();
            next = null;
        }
    }
    
    // View of the map values
    
    /**
     * Collection view of the values contained in this map.
     */
    private transient Collection<Object> values;
    
    /**
     * Returns collection view of the values contained in this map.
     * 
     * @return Collection view of the values contained in this map
     * @see Map#values()
     */
    public final Collection<Object> values() {
        return values == null ? values = new Values() : values;
    }
    
    /**
     * Collection view of the values.
     * 
     * @author Fox Mulder
     */
    private final class Values extends AbstractCollection<Object> {
        @Override public int size() {
            return ConstrainedMap.this.size();
        }
        @Override public boolean contains(Object obj) {
            return ConstrainedMap.this.containsValue(obj);
        }
        @Override public Iterator<Object> iterator() {
            return new ValueItr();
        }
        @Override public void clear() {
            ConstrainedMap.this.resetValues();
        }
    }
    
    /**
     * Iterator of the values.
     * 
     * @author Fox Mulder
     */
    private final class ValueItr implements Iterator<Object> {
        Iterator<Entry<?>> itr = dataset.values().iterator();
        Entry<?> next;
        @Override public boolean hasNext() {
            readLock.lock();
            try {
                return itr.hasNext();
            } finally {
                readLock.unlock();
            }
        }
        @Override public Object next() {
            readLock.lock();
            try {
                next = itr.next();
                return next.value;
            } finally {
                readLock.unlock();
            }
        }
        @Override public void remove() {
            if (next == null)
                throw new IllegalStateException();
            next.resetValue();
            next = null;
        }
    }
    
    // View of the map entries
    
    /**
     * Set view of the entries contained in this map.
     */
    private transient Set<Map.Entry<String, Object>> entrySet;
    
    /**
     * Returns set view of the entries contained in this map.
     * 
     * @return Set view of the entries contained in this map.
     * @see Map#entrySet()
     */
    public final Set<Map.Entry<String, Object>> entrySet() {
        return entrySet == null ? entrySet = new EntrySet() : entrySet;
    }
    
    /**
     * Set view of the entries.
     * 
     * @author Fox Mulder
     */
    private final class EntrySet extends AbstractSet<Map.Entry<String, Object>> {
        @Override public int size() {
            return ConstrainedMap.this.size();
        }
        @Override public boolean contains(Object obj) {
            if (!(obj instanceof Map.Entry<?, ?>))
                return false;
            readLock.lock();
            try {
                Map.Entry<?, ?> other = (Map.Entry<?, ?>) obj;
                Entry<?> entry = dataset.get(other.getKey());
                return entry.equals(other);
            } finally {
                readLock.unlock();
            }
        }
        @Override public Iterator<Map.Entry<String, Object>> iterator() {
            return new EntryItr();
        }
        @Override public boolean remove(Object obj) {
            if (!(obj instanceof Map.Entry<?, ?>))
                return false;
            writeLock.lock();
            try {
                Map.Entry<?, ?> other = (Map.Entry<?, ?>) obj;
                Entry<?> entry = dataset.get(other.getKey());
                if (entry.equals(other)) {
                    entry.resetValue();
                    return true;
                }
                return false;
            } finally {
                writeLock.unlock();
            }
        }
        @Override public void clear() {
            ConstrainedMap.this.resetValues();
        }
    }
    
    /**
     * Iterator of the entries.
     * 
     * @author Fox Mulder
     */
    private final class EntryItr implements Iterator<Map.Entry<String, Object>> {
        Iterator<Entry<?>> itr = dataset.values().iterator();
        Entry<?> next;
        @Override public boolean hasNext() {
            readLock.lock();
            try {
                return itr.hasNext();
            } finally {
                readLock.unlock();
            }
        }
        @Override public Map.Entry<String, Object> next() {
            readLock.lock();
            try {
                return next = itr.next();
            } finally {
                readLock.unlock();
            }
        }
        @Override public void remove() {
            if (next == null)
                throw new IllegalStateException();
            next.resetValue();
            next = null;
        }
    }
    
    // Entry iterator
    
    /**
     * Returns iterator of the map entries.
     * 
     * @return Iterator of the map entries.
     */
    public final Iterator<Entry<?>> iterator() {
        return new FilteredEntryItr(PropertyFilter.ALL);
    }
    
    /**
     * Returns iterator of the map entries with the specified property filter.
     * 
     * @param filter Filter to be used for filtering entries.
     * @return Iterator of the map entries with the specified property filter.
     */
    public final Iterator<Entry<?>> iterator(PropertyFilter filter) {
        return new FilteredEntryItr(filter);
    }
    
    /**
     * Iterator of the entries with filtering capability.
     * 
     * @author Fox Mulder
     */
    private final class FilteredEntryItr implements Iterator<Entry<?>> {
        Iterator<Entry<?>> itr = dataset.values().iterator();
        PropertyFilter filter;
        Entry<?> next;
        FilteredEntryItr(PropertyFilter filter) {
            this.filter = filter;
        }
        @Override public boolean hasNext() {
            readLock.lock();
            try {
                while (next == null) {
                    if (!itr.hasNext())
                        return false;
                    Entry<?> entry = itr.next();
                    if (filter.accept(entry.metadata))
                        next = entry;
                }
                return true;
            } finally {
                readLock.unlock();
            }
        }
        @Override public Entry<?> next() {
            if (hasNext()) {
                Entry<?> entry = next;
                next = null;
                return entry;
            }
            throw new NoSuchElementException();
        }
        @Override public void remove() {
            if (next == null)
                throw new IllegalStateException();
            writeLock.lock();
            try {
                next.resetValue();
                next = null;
            } finally {
                writeLock.unlock();
            }
        }
    }
    
    // Comparison and hashing
    
    /**
     * Returns a hash code value for this map.
     * 
     * @return A hash code value for this map.
     */
    public int hashCode() {
        readLock.lock();
        try {
            int hash = 0;
            for (Entry<?> entry : dataset.values())
                hash = 31 * hash + entry.hashCode();
            return hash;
        } finally {
            readLock.unlock();
        }
    }
    
    /**
     * Determines if this map equals to the specified one. Two maps considered
     * to be equal if they have the same keys with the same values.
     * 
     * @param obj Another map.
     * @return <code>true</code> if this map equals to the specified one;
     *         <code>false</code> otherwise.
     */
    public boolean equals(Object obj) {
        if (obj == this)
            return true;
        if (!(obj instanceof Map<?, ?>))
            return false;
        readLock.lock();
        try {
            Map<?, ?> other = (Map<?, ?>) obj;
            if (other.size() != dataset.size())
                return false;
            for (Map.Entry<?, ?> e : other.entrySet()) {
                Entry<?> entry = dataset.get(e.getKey());
                if (!entry.equals(e))
                    return false;
            }
            return true;
        } finally {
            readLock.unlock();
        }
    }
    
    /**
     * Returns string representing this map.
     * 
     * @return String representing this map.
     */
    public String toString() {
        readLock.lock();
        try {
            StringBuilder buf = new StringBuilder();
            for (Entry<?> entry : dataset.values())
                buf.append(entry).append('\n');
            return buf.toString();
        } finally {
            readLock.unlock();
        }
    }
    
    // Entry
    
    /**
     * This map entry holds metadata information and provides advanced
     * operations.
     * 
     * @author Fox Mulder
     */
    public final class Entry<V> implements Map.Entry<String, Object> {
        
        /**
         * Entry metadata.
         */
        private final PropertyMetaData<?, V> metadata;
        
        /**
         * Comment message key for this entry.
         */
        private final String comment;
        
        /**
         * Current value.
         */
        private V value;
        
        /**
         * Constructs a new <code>Entry</code> with the specified metadata.
         * 
         * @param metadata Entry metadata.
         */
        private Entry(PropertyMetaData<?, V> metadata) {
            this.metadata = metadata;
            this.value = metadata.getDefaultValue();
            this.comment = metadata.getName() + ".comment";
        }
        
        /**
         * Returns map this entry belongs to.
         * 
         * @return Map this entry belongs to.
         */
        public ConstrainedMap getMap() {
            return ConstrainedMap.this;
        }
        
        // Metadata operations
        
        /**
         * Returns this entry value type.
         * 
         * @return This entry value type.
         */
        public Class<V> getType() {
            return metadata.getConverter().getType();
        }
        
        /**
         * Returns key corresponding to this entry.
         * 
         * @return Key corresponding to this entry.
         */
        public String getKey() {
            return metadata.getName();
        }
        
        /**
         * Returns default value of this entry.
         * 
         * @return Default value of this entry.
         */
        public V getDefaultValue() {
            return metadata.getDefaultValue();
        }
        
        /**
         * Returns converter to be used for value conversion into and from
         * string representation.
         * 
         * @return Converter to be used for value conversion into and from
         *         string representation.
         */
        public Converter<V> getConverter() {
            return metadata.getConverter();
        }
        
        /**
         * Returns constraint to be used for value validation. If there is no
         * constraint defined then this method returns <code>null</code>.
         * 
         * @return Constraint to be used for value validation.
         */
        public Constraint<? super V> getConstraint() {
            return metadata.getConstraint();
        }
        
        /**
         * Determines if this entry requires value.
         * 
         * @return <code>true</code> if this entry requires value;
         *         <code>false</code> otherwise.
         */
        public boolean isRequired() {
            return metadata.isRequired();
        }
        
        /**
         * Returns comment corresponding to this entry for default locale.
         * 
         * @return Comment corresponding to this entry for default locale.
         * @see #getComment(Locale)
         */
        public String getComment() {
            return getComment(Locale.getDefault());
        }
        
        /**
         * Returns comment corresponding to this entry for the specified locale.
         * 
         * @param locale Desired locale.
         * @return Comment corresponding to this entry for the specified locale.
         */
        public String getComment(Locale locale) {
            try {
                return validator.getMessageResolver().resolveMessage(comment, locale);
            } catch (MissingResourceException e) {
                return null;
            }
        }
        
        // Data operations
        
        /**
         * Returns value corresponding to this entry.
         * 
         * @return Value corresponding to this entry.
         */
        public V getValue() {
            readLock.lock();
            try {
                return value;
            } finally {
                readLock.unlock();
            }
        }
        
        /**
         * Assigns the specified value for this entry.
         * 
         * @param value Value to be assigned for this entry.
         * @return Previous value assigned for this entry.
         * @throws ValidationException if violations occured.
         * @see #setValue(Object, Locale)
         */
        public V setValue(Object value) {
            return setValue(value, Locale.getDefault());
        }
        
        /**
         * Assigns the specified value for this entry.
         * 
         * @param value Value to be assigned for this entry.
         * @param locale Locale to be used for error messages.
         * @return Previous value assigned for this entry.
         * @throws ValidationException if violations occured.
         * @see Transaction#setValue(String, Object)
         */
        public V setValue(Object value, Locale locale) {
            writeLock.lock();
            try {
                V oldValue = this.value;
                Transaction tx = new Transaction(locale);
                tx.setValue(getKey(), value);
                tx.commit(false);
                return oldValue;
            } finally {
                writeLock.unlock();
            }
        }
        
        /**
         * Returns default string representation of value corresponding to this
         * entry.
         * 
         * @return Default string representation of value corresponding to this
         *         entry.
         */
        public String getRawValue() {
            readLock.lock();
            try {
                return validator.newContext().encodeValue(getConverter(), value);
            } finally {
                readLock.unlock();
            }
        }
        
        /**
         * Returns localized string representation of value corresponding to
         * this entry.
         * 
         * @return Localized string representation of value corresponding to
         *         this entry.
         * @see #getLocalizedValue(Locale)
         */
        public String getLocalizedValue() {
            return getLocalizedValue(Locale.getDefault());
        }
        
        /**
         * Returns localized string representation of value corresponding to
         * this entry.
         *
         * @param locale Desired locale.
         * @return Localized string representation of value corresponding to
         *         this entry.
         */
        public String getLocalizedValue(Locale locale) {
            readLock.lock();
            try {
                return validator.newContext()
                                .setMessageLocale(locale)
                                .setLocalizedConvert(true)
                                .encodeValue(getConverter(), value);
            } finally {
                readLock.unlock();
            }
        }
        
        /**
         * Assigns the specified value for this entry.
         * 
         * @param value Default string representation of value to be assigned
         *        for this entry.
         * @return Previous value assigned for this entry.
         * @throws ValidationException if violations occured.
         * @see #setRawValue(String, Locale)
         */
        public V setRawValue(String value) {
            return setRawValue(value, Locale.getDefault());
        }
        
        /**
         * Assigns the specified value for this entry.
         * 
         * @param value Default string representation of value to be assigned
         *        for this entry.
         * @param locale Locale to be used for error messages.
         * @return Previous value assigned for this entry.
         * @throws ValidationException if violations occured.
         * @see Transaction#setRawValue(String, String)
         */
        public V setRawValue(String value, Locale locale) {
            writeLock.lock();
            try {
                V oldValue = this.value;
                Transaction tx = new Transaction(locale);
                tx.setRawValue(getKey(), value);
                tx.commit(false);
                return oldValue;
            } finally {
                writeLock.unlock();
            }
        }
        
        /**
         * Assigns the specified value for this entry.
         * 
         * @param value Localized string representation of value to be assigned
         *        for this entry.
         * @return Previous value assigned for this entry.
         * @throws ValidationException if violations occured.
         * @see #setLocalizedValue(String, Locale)
         */
        public V setLocalizedValue(String value) {
            return setLocalizedValue(value, Locale.getDefault());
        }
        
        /**
         * Assigns the specified value for this entry.
         * 
         * @param value Localized string representation of value to be assigned
         *        for this entry.
         * @param locale Desired locale.
         * @return Previous value assigned for this entry.
         * @throws ValidationException if violations occured.
         * @see Transaction#setLocalizedValue(String, String)
         */
        public V setLocalizedValue(String value, Locale locale) {
            writeLock.lock();
            try {
                V oldValue = this.value;
                Transaction tx = new Transaction(locale);
                tx.setLocalizedValue(getKey(), value);
                tx.commit(false);
                return oldValue;
            } finally {
                writeLock.unlock();
            }
        }
        
        /**
         * Resets value of this entry.
         * 
         * @return Previous value assigned for this entry.
         * @throws ValidationException if violations occured.
         * @see #resetValue(Locale)
         */
        public V resetValue() {
            return resetValue(Locale.getDefault());
        }
        
        /**
         * Resets value of this entry.
         * 
         * @param locale Locale to be used for error messages.
         * @return Previous value assigned for this entry.
         * @throws ValidationException if violations occured.
         * @see #setValue(Object, Locale)
         */
        public V resetValue(Locale locale) {
            return setValue(getDefaultValue(), locale);
        }
        
        /**
         * Determines if this entry value equals to default value.
         * 
         * @return <code>true</code> if this entry value equals to default
         *         value; <code>false</code> otherwise.
         */
        public boolean isDefault() {
            readLock.lock();
            try {
                V defaultValue = metadata.getDefaultValue();
                return value == null ? defaultValue == null : value.equals(defaultValue);
            } finally {
                readLock.unlock();
            }
        }
        
        // Comparison and hashing
        
        /**
         * Returns a hash code value for this entry.
         * 
         * @return A hash code value for this entry.
         */
        public int hashCode() {
            Object value = getValue();
            return metadata.hashCode() ^ (value == null ? 0 : value.hashCode());
        }
        
        /**
         * Determines if this entry equals to the specified one. Two entries
         * considered to be equal if they have equal keys and values.
         * 
         * @param obj Another entry.
         * @return <code>true</code> if this entry equals to the specified one;
         *         <code>false</code> otherwise.
         */
        public boolean equals(Object obj) {
            if (obj instanceof Map.Entry) {
                Map.Entry<?, ?> other = (Map.Entry<?, ?>) obj;
                if (getKey().equals(other.getKey())) {
                    Object thisValue = getValue();
                    Object thatValue = other.getValue();
                    return thisValue == null ? thatValue == null : thisValue.equals(thatValue);
                }
            }
            return false;
        }
        
        /**
         * Returns string representing this entry.
         * 
         * @return String representing this entry.
         */
        public String toString() {
            return getKey() + "=" + getRawValue();
        }
        
    }
    
    /**
     * Returns map entry for the specified key.
     * 
     * @param key Key for which entry to be returned.
     * @return Map entry for the specified key.
     * @throws IllegalArgumentException if this map contains no entry for the
     *         specified key.
     */
    @SuppressWarnings("unchecked")
    private final <V> Entry<V> entryFor(Object key) {
        Entry<?> entry = dataset.get(key);
        if (entry == null)
            throw new IllegalArgumentException((String) key);
        return (Entry<V>) entry;
    }
    
    /**
     * Creates a snapshot of this map.
     * 
     * @return A snapshot of this map.
     */
    private final Map<String, Object> makeSnapshot() {
        Map<String, Object> snapshot = new LinkedHashMap<String, Object>();
        for (Entry<?> entry : dataset.values())
            snapshot.put(entry.getKey(), deepCloneOf(entry.value));
        return snapshot;
    }
    
    // Transaction
    
    /**
     * This class allows to perform modification operations of this map on a
     * snapshot that is created at the moment of construction of the
     * transaction.
     * 
     * <p>Note that <code>Transaction</code> is not thread-safe and should be
     * used in a single thread.</p>
     * 
     * @author Fox Mulder
     */
    public final class Transaction {
        
        /**
         * Snapshot of values.
         */
        private final Map<String, Object> snapshot;
        
        /**
         * Map of violations.
         */
        private final Map<String, ViolationException> violations;
        
        /**
         * Validation context.
         */
        private final Validator<Map<String, Object>>.ContextBuilder context;
        
        /**
         * Constructs a new <code>Transaction</code> with the specified locale.
         * 
         * @param locale Locale to be used for error messages and value
         *        conversions into and from string representation.
         */
        private Transaction(Locale locale) {
            this.snapshot = makeSnapshot();
            this.violations = new LinkedHashMap<String, ViolationException>();
            this.context = validator.newContext().setMessageLocale(locale);
        }
        
        /**
         * Returns snapshot value to which the specified key is mapped.
         * 
         * @param key Key whose associated value to be returned.
         * @return Value to which the specified key is mapped.
         * @throws IllegalArgumentException if this map contains no entry for
         *         the specified key.
         */
        public <V> V getValue(String key) {
            return validator.getValue(snapshot, key);
        }
        
        /**
         * Assigns the specified value for the specified key in this snapshot.
         * 
         * @param key Key for which the specified value to be assigned.
         * @param value Value to be assigned for the specified key.
         * @throws IllegalArgumentException if this map contains no entry for
         *         the specified key.
         */
        public void setValue(String key, Object value) {
            validator.setValue(snapshot, key, value);
        }
        
        /**
         * Returns default string representation of snapshot value to which the
         * specified key is mapped.
         * 
         * @param key Key whose associated default string representation of
         *        value to be returned.
         * @return Default string representation of value to which the
         *         specified key is mapped.
         * @throws IllegalArgumentException if this map contains no entry for
         *         the specified key.
         */
        public String getRawValue(String key) {
            return context.setLocalizedConvert(false).getEncodedValue(snapshot, key);
        }
        
        /**
         * Assigns the specified value for the specified key in this snapshot.
         * 
         * @param key Key for which the specified value to be assigned.
         * @param value Default string representation of value to be assigned
         *        for the specified key.
         * @throws IllegalArgumentException if this map contains no entry for
         *         the specified key.
         */
        public void setRawValue(String key, String value) {
            try {
                violations.remove(key);
                context.setLocalizedConvert(false).setEncodedValue(snapshot, key, value);
            } catch (ViolationException e) {
                violations.put(key, e);
            }
        }
        
        /**
         * Returns localized string representation of snapshot value to which
         * the specified key is mapped.
         * 
         * @param key Key whose associated localized string representation of
         *        value to be returned.
         * @return Localized string representation of value to which the
         *         specified key is mapped.
         * @throws IllegalArgumentException if this map contains no entry for
         *         the specified key.
         */
        public String getLocalizedValue(String key) {
            return context.setLocalizedConvert(true).getEncodedValue(snapshot, key);
        }
        
        /**
         * Assigns the specified value for the specified key in this snapshot.
         * 
         * @param key Key for which the specified value to be assigned.
         * @param value Localized string representation of value to be assigned
         *        for the specified key.
         * @throws IllegalArgumentException if this map contains no entry for
         *         the specified key.
         */
        public void setLocalizedValue(String key, String value) {
            try {
                violations.remove(key);
                context.setLocalizedConvert(true).setEncodedValue(snapshot, key, value);
            } catch (ViolationException e) {
                violations.put(key, e);
            }
        }
        
        /**
         * Resets snapshot value of the specified key to its default value.
         * 
         * @param key Key for which the specified value to be reseted.
         * @throws IllegalArgumentException if this map contains no entry for
         *         the specified key.
         */
        public void resetValue(String key) {
            violations.remove(key);
            snapshot.put(key, entryFor(key).getDefaultValue());
        }
        
        /**
         * Assigns the specified map values to this snapshot.
         * 
         * @param values Values to be stored in this snapshot.
         */
        public void setValues(Map<String, Object> values) {
            violations.keySet().removeAll(values.keySet());
            validator.setValues(snapshot, values);
        }
        
        /**
         * Returns default string representations of this snapshot values.
         * 
         * @return Default string representations of this snapshot values.
         */
        public Map<String, String> getRawValues() {
            return context.setLocalizedConvert(false).getEncodedValues(snapshot);
        }
        
        /**
         * Assigns the specified map values to this snapshot.
         * 
         * @param values Default string representations of values to be stored
         *        in this snapshot.
         */
        public void setRawValues(Map<String, String> values) {
            try {
                violations.keySet().removeAll(values.keySet());
                context.setLocalizedConvert(false).setEncodedValues(snapshot, values);
            } catch (ValidationException e) {
                for (ViolationException violation : e.getRootViolations())
                    violations.put(violation.getElementName(), violation);
            }
        }
        
        /**
         * Returns localized string representations of this snapshot values.
         * 
         * @return Localized string representations of this snapshot values.
         */
        public Map<String, String> getLocalizedValues() {
            return context.setLocalizedConvert(true).getEncodedValues(snapshot);
        }
        
        /**
         * Assigns the specified map values to this snapshot.
         * 
         * @param values Localized string representations of values to be
         *        stored in this snapshot.
         */
        public void setLocalizedValues(Map<String, String> values) {
            try {
                violations.keySet().removeAll(values.keySet());
                context.setLocalizedConvert(true).setEncodedValues(snapshot, values);
            } catch (ValidationException e) {
                for (ViolationException violation : e.getRootViolations())
                    violations.put(violation.getElementName(), violation);
            }
        }
        
        /**
         * Resets all the values of this snapshot to its default values.
         */
        public void resetValues() {
            readLock.lock();
            try {
                violations.clear();
                for (Entry<?> entry : dataset.values())
                    snapshot.put(entry.getKey(), entry.getDefaultValue());
            } finally {
                readLock.unlock();
            }
        }
        
        /**
         * Completes the transaction. In other words, this method copies
         * this snapshot values to the map.
         * 
         * @param invalid Determines if this transaction should be committed
         *        anyway (even with invalid values).
         * @throws ViolationException if this snapshot contains invalid values.
         */
        public void commit(boolean invalid) {
            writeLock.lock();
            try {
                apply(context.validateEntity(snapshot));
                violations.clear();
            } catch (ValidationException e) {
                if (invalid)
                    apply(snapshot);
                if (violations.isEmpty())
                    throw e;
                for (ViolationException violation : e.getRootViolations())
                    if (violations.get(violation.getElementName()) == null)
                        violations.put(violation.getElementName(), violation);
                throw new ValidationException(new ArrayList<ViolationException>(violations.values()));
            } finally {
                writeLock.unlock();
            }
        }
        
        /**
         * Rolls back the transaction. In other words, this method copies
         * current map values to this snapshot.
         */
        public void rollback() {
            readLock.lock();
            try {
                for (Entry<?> entry : dataset.values())
                    snapshot.put(entry.getKey(), entry.value);
                violations.clear();
            } finally {
                readLock.unlock();
            }
        }
        
        /**
         * Copies this snapshot values to the map.
         * 
         * @param values New values of the map.
         */
        private void apply(Map<String, Object> values) {
            for (Map.Entry<String, Object> e : values.entrySet()) {
                Entry<Object> entry = entryFor(e.getKey());
                Object oldValue = entry.value;
                Object newValue = entry.value = e.getValue();
                pcs.firePropertyChange(e.getKey(), oldValue, newValue);
            }
        }
        
    }
    
    // Persistence
    
    /**
     * Loads this map values from the specified file.
     * 
     * @param file File from which to read the values.
     * @return This constrained map instance.
     * @throws IOException if an error occurred when reading from the file.
     * @throws ValidationException if violations occur.
     * @see #load(File, Locale)
     */
    public ConstrainedMap load(File file) throws IOException {
        return load(file, Locale.getDefault());
    }
    
    /**
     * Loads this map values from the specified file.
     * 
     * @param file File from which to read the values.
     * @param locale Locale to be used for error messages.
     * @return This constrained map instance.
     * @throws IOException if an error occurred when reading from the file.
     * @throws ValidationException if violations occur.
     * @see #load(Reader, Locale)
     */
    public ConstrainedMap load(File file, Locale locale) throws IOException {
        FileReader reader = new FileReader(file);
        try {
            return load(reader, locale);
        } finally {
            reader.close();
        }
    }
    
    /**
     * Loads this map values from the specified input stream.
     * 
     * @param stream Input stream from which to read the values.
     * @return This constrained map instance.
     * @throws IOException if an error occurred when reading from the stream.
     * @throws ValidationException if violations occur.
     * @see #load(InputStream, Locale)
     */
    public ConstrainedMap load(InputStream stream) throws IOException {
        return load(stream, Locale.getDefault());
    }
    
    /**
     * Loads this map values from the specified input stream.
     * 
     * @param stream Input stream from which to read the values.
     * @param locale Locale to be used for error messages.
     * @return This constrained map instance.
     * @throws IOException if an error occurred when reading from the stream.
     * @throws ValidationException if violations occur.
     * @see #load(Reader, Locale)
     */
    public ConstrainedMap load(InputStream stream, Locale locale) throws IOException {
        return load(new InputStreamReader(stream), locale);
    }
    
    /**
     * Loads this map values from the specified input character stream.
     * 
     * @param reader Input character stream from which to read the values.
     * @return This constrained map instance.
     * @throws IOException if an error occurred when reading from the stream.
     * @throws ValidationException if violations occur.
     * @see #load(Reader, Locale)
     */
    public ConstrainedMap load(Reader reader) throws IOException {
        return load(reader, Locale.getDefault());
    }
    
    /**
     * Loads this map values from the specified input character stream.
     * 
     * @param reader Input character stream from which to read the values.
     * @param locale Locale to be used for error messages.
     * @return This constrained map instance.
     * @throws IOException if an error occurred when reading from the stream.
     * @throws ValidationException if violations occur.
     * @see #doLoad(BufferedReader)
     */
    public ConstrainedMap load(Reader reader, Locale locale) throws IOException {
        Map<String, String> values = doLoad(reader instanceof BufferedReader
            ? (BufferedReader) reader
            : new BufferedReader(reader));
        writeLock.lock();
        try {
            Transaction tx = new Transaction(locale);
            tx.setRawValues(values);
            tx.commit(true);
            return this;
        } finally {
            writeLock.unlock();
        }
    }
    
    /**
     * Reads a key-value pairs from the specified input character stream in a
     * simple line-oriented format.
     * 
     * @param in Input character stream.
     * @return Map of key-value pairs that has been read from the stream.
     * @throws IOException if an error occurred when reading from the stream.
     */
    protected Map<String, String> doLoad(BufferedReader in) throws IOException {
        Map<String, String> values = new LinkedHashMap<String, String>();
        
        for (String line = in.readLine(); line != null; line = in.readLine()) {
            line = line.trim();
            if (line.isEmpty() || line.charAt(0) == '#')
                continue;
            
            int index = line.indexOf('=');
            if (index < 0) {
                values.put(line, null);
            } else {
                String key = line.substring(0, index).trim();
                String value = line.substring(index + 1).trim();
                values.put(key, value);
            }
        }
        
        return values;
    }
    
    /**
     * Saves this map key-value pairs to the specified file.
     * 
     * @param file File to which to write the key-value pairs.
     * @param comment A comment to be written in the first line.
     * @return This constrained map instance.
     * @throws IOException if an error occurred when writing to the file.
     * @see #save(File, Locale, String)
     */
    public ConstrainedMap save(File file, String comment) throws IOException {
        return save(file, Locale.getDefault(), comment);
    }
    
    /**
     * Saves this map key-value pairs to the specified file.
     * 
     * @param file File to which to write the key-value pairs.
     * @param locale Locale to be used for comments of key-value pairs.
     * @param comment A comment to be written in the first line.
     * @return This constrained map instance.
     * @throws IOException if an error occurred when writing to the file.
     * @see #save(Writer, Locale, String)
     */
    public ConstrainedMap save(File file, Locale locale, String comment) throws IOException {
        FileWriter writer = new FileWriter(file);
        try {
            return save(writer, locale, comment);
        } finally {
            writer.close();
        }
    }
    
    /**
     * Saves this map key-value pairs to the specified output stream.
     * 
     * @param stream Output stream to which to write the key-value pairs.
     * @param comment A comment to be written in the first line.
     * @return This constrained map instance.
     * @throws IOException if an error occurred when writing to the stream.
     * @see #save(OutputStream, Locale, String)
     */
    public ConstrainedMap save(OutputStream stream, String comment) throws IOException {
        return save(stream, Locale.getDefault(), comment);
    }
    
    /**
     * Saves this map key-value pairs to the specified output stream.
     * 
     * @param stream Output stream to which to write the key-value pairs.
     * @param locale Locale to be used for comments of key-value pairs.
     * @param comment A comment to be written in the first line.
     * @return This constrained map instance.
     * @throws IOException if an error occurred when writing to the stream.
     * @see #save(Writer, Locale, String)
     */
    public ConstrainedMap save(OutputStream stream, Locale locale, String comment) throws IOException {
        return save(new OutputStreamWriter(stream), locale, comment);
    }
    
    /**
     * Saves this map key-value pairs to the specified output character stream.
     * 
     * @param writer Output character stream to which to write the key-value pairs.
     * @param comment A comment to be written in the first line.
     * @return This constrained map instance.
     * @throws IOException if an error occurred when writing to the stream.
     * @see #save(Writer, Locale, String)
     */
    public ConstrainedMap save(Writer writer, String comment) throws IOException {
        return save(writer, Locale.getDefault(), comment);
    }
    
    /**
     * Saves this map key-value pairs to the specified output character stream.
     * 
     * @param writer Output character stream to which to write the key-value pairs.
     * @param locale Locale to be used for comments of key-value pairs.
     * @param comment A comment to be written in the first line.
     * @return This constrained map instance.
     * @throws IOException if an error occurred when writing to the stream.
     * @see #doSave(BufferedWriter, Locale, String)
     */
    public ConstrainedMap save(Writer writer, Locale locale, String comment) throws IOException {
        BufferedWriter out = writer instanceof BufferedWriter
            ? (BufferedWriter) writer
            : new BufferedWriter(writer);
        doSave(out, locale, comment);
        return this;
    }
    
    /**
     * Writes a key-value pairs from this map to the specified output character
     * stream in a simple line-oriented format.
     * 
     * @param out Output character stream.
     * @param locale Locale to be used for comments of key-value pairs.
     * @param comment A comment to be written in the first line.
     * @throws IOException if an error occurred when writing to the stream.
     */
    protected void doSave(BufferedWriter out, Locale locale, String comment)
            throws IOException {
        writeComment(out, new java.util.Date().toString());
        if (comment != null)
            writeComment(out, comment);
        out.newLine();
        
        readLock.lock();
        try {
            Validator<Map<String, Object>>.ContextBuilder context = validator.newContext();
            Map<String, String> values = context.getEncodedValues(this);
            context.setMessageLocale(locale);
            
            Constraint<?> constraint = validator.getMetaData().getConstraint();
            if (constraint != null) {
                writeComment(out, context.buildMessage(constraint));
                out.newLine();
            }
            
            for (Entry<?> entry : dataset.values()) {
                comment = entry.getComment();
                if (comment != null)
                    writeComment(out, comment);
                constraint = entry.getConstraint();
                if (constraint != null)
                    writeComment(out, context.buildMessage(constraint));
                
                writeKey(out, entry.getKey());
                out.write(" = ");
                writeValue(out, values.get(entry.getKey()));
                
                out.newLine();
            }
        } finally {
            readLock.unlock();
        }
        
        out.flush();
    }
    
    /**
     * Writes the specified comment to the specified output character stream.
     * This method can write multi-line comments.
     * 
     * @param out Output character stream.
     * @param comment A comment to be written.
     * @throws IOException if an error occurred when writing to the stream.
     */
    protected static void writeComment(BufferedWriter out, String comment) throws IOException {
        if (comment != null) {
            StringTokenizer tokenizer = new StringTokenizer(comment, "\r\n");
            while (tokenizer.hasMoreTokens()) {
                String line = tokenizer.nextToken();
                out.write('#');
                out.write(' ');
                out.write(line);
                out.newLine();
            }
        }
    }
    
    /**
     * Writes the entry key to the specified output character stream.
     * 
     * @param out Output character stream.
     * @param key An entry key to be written.
     * @throws IOException if an error occurred when writing to the stream.
     */
    protected static void writeKey(BufferedWriter out, String key)
            throws IOException {
        StringBuilder buf = new StringBuilder(key.length());
        StringTokenizer tokenizer = new StringTokenizer(key, " \t\r\n\f\b");
        while (tokenizer.hasMoreTokens())
            buf.append(tokenizer.nextToken());
        out.write(buf.toString());
    }
    
    /**
     * Writes the entry value to the specified output character stream.
     * 
     * @param out Output character stream.
     * @param value An entry value to be written.
     * @throws IOException if an error occurred when writing to the stream.
     */
    protected static void writeValue(BufferedWriter out, String value)
            throws IOException {
        StringBuilder buf = new StringBuilder(value.length());
        StringTokenizer tokenizer = new StringTokenizer(value, "\t\r\n\f\b");
        while (tokenizer.hasMoreTokens()) {
            buf.append(tokenizer.nextToken());
            if (tokenizer.hasMoreTokens())
                buf.append(' ');
        }
        out.write(buf.toString());
    }
    
    // Value change support
    
    /**
     * Support for value change events.
     */
    private final PropertyChangeSupport pcs = new PropertyChangeSupport(this);
    
    /**
     * Adds the specified listener to receive value change events from this map.
     * 
     * @param listener Listener to be added.
     */
    public final void addListener(PropertyChangeListener listener) {
        pcs.addPropertyChangeListener(listener);
    }
    
    /**
     * Removes the specified listener so that it no longer receives events from
     * this map.
     * 
     * @param listener Listener to be removed.
     */
    public final void removeListener(PropertyChangeListener listener) {
        pcs.removePropertyChangeListener(listener);
    }
    
    /*
     * Returns deep clone of the specified object.
     * FIXME We can't clone beans, collections, maps, etc.
     */
    private static Object deepCloneOf(Object obj) {
        if (obj == null)
            return null;
        if (obj.getClass().isArray()) {
            Object[] array = (Object[]) obj;
            Object[] clone = array.clone();
            for (int i = 0; i < array.length; i++)
                clone[i] = deepCloneOf(array[i]);
            return clone;
        }
        return obj;
    }
    
}
