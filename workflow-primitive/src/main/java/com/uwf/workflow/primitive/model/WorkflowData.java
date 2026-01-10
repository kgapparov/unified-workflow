package com.uwf.workflow.primitive.model;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * Serializable class for mutable, shared data between workflow tasks.
 * Provides thread-safe access to shared workflow data.
 */
public class WorkflowData implements Serializable {
    private final Map<String, Object> data;

    /**
     * Creates a new empty WorkflowData instance.
     */
    public WorkflowData() {
        this.data = new HashMap<>();
    }

    /**
     * Creates a new WorkflowData instance with initial data.
     *
     * @param initialData the initial data map
     */
    public WorkflowData(Map<String, Object> initialData) {
        this.data = new HashMap<>(initialData);
    }

    /**
     * Puts a key-value pair into the workflow data.
     *
     * @param key   the key
     * @param value the value
     */
    public synchronized void put(String key, Object value) {
        data.put(key, value);
    }

    /**
     * Gets a value from the workflow data.
     *
     * @param key the key
     * @return the value, or null if not found
     */
    public synchronized Object get(String key) {
        return data.get(key);
    }

    /**
     * Gets a value from the workflow data with type safety.
     *
     * @param <T>   the expected type
     * @param key   the key
     * @param clazz the expected class
     * @return the value, or null if not found or type mismatch
     */
    @SuppressWarnings("unchecked")
    public synchronized <T> T get(String key, Class<T> clazz) {
        Object value = data.get(key);
        if (value != null && clazz.isInstance(value)) {
            return (T) value;
        }
        return null;
    }

    /**
     * Checks if the workflow data contains a specific key.
     *
     * @param key the key to check
     * @return true if the key exists, false otherwise
     */
    public synchronized boolean containsKey(String key) {
        return data.containsKey(key);
    }

    /**
     * Removes a key-value pair from the workflow data.
     *
     * @param key the key to remove
     * @return the removed value, or null if not found
     */
    public synchronized Object remove(String key) {
        return data.remove(key);
    }

    /**
     * Clears all data from the workflow data.
     */
    public synchronized void clear() {
        data.clear();
    }

    /**
     * Gets a copy of the current data map.
     *
     * @return a copy of the data map
     */
    public synchronized Map<String, Object> toMap() {
        return new HashMap<>(data);
    }

    /**
     * Creates a deep copy of this WorkflowData.
     * Attempts to deep copy all values using serialization.
     * If serialization fails for any value, falls back to shallow copy.
     *
     * @return a deep copy of this WorkflowData
     */
    public synchronized WorkflowData deepCopy() {
        Map<String, Object> copiedData = new HashMap<>();
        
        for (Map.Entry<String, Object> entry : data.entrySet()) {
            String key = entry.getKey();
            Object value = entry.getValue();
            
            if (value == null) {
                copiedData.put(key, null);
            } else if (value instanceof Serializable) {
                try {
                    // Attempt deep copy via serialization
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    ObjectOutputStream oos = new ObjectOutputStream(baos);
                    oos.writeObject(value);
                    oos.close();
                    
                    ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
                    ObjectInputStream ois = new ObjectInputStream(bais);
                    Object copiedValue = ois.readObject();
                    ois.close();
                    
                    copiedData.put(key, copiedValue);
                } catch (Exception e) {
                    // Fall back to shallow copy if serialization fails
                    copiedData.put(key, value);
                }
            } else {
                // Non-serializable object, use shallow copy
                copiedData.put(key, value);
            }
        }
        
        return new WorkflowData(copiedData);
    }

    /**
     * Creates a shallow copy of this WorkflowData.
     * The map is copied but values are not deep copied.
     *
     * @return a shallow copy of this WorkflowData
     */
    public synchronized WorkflowData shallowCopy() {
        return new WorkflowData(new HashMap<>(data));
    }

    /**
     * Gets the size of the workflow data.
     *
     * @return the number of key-value pairs
     */
    public synchronized int size() {
        return data.size();
    }

    /**
     * Checks if the workflow data is empty.
     *
     * @return true if empty, false otherwise
     */
    public synchronized boolean isEmpty() {
        return data.isEmpty();
    }

    @Override
    public synchronized boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        WorkflowData that = (WorkflowData) o;
        return Objects.equals(data, that.data);
    }

    @Override
    public synchronized int hashCode() {
        return Objects.hash(data);
    }

    @Override
    public synchronized String toString() {
        return "WorkflowData{" +
                "data=" + data +
                '}';
    }
}
