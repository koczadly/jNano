/*
 * Copyright (c) 2021 Karl Oczadly (karl@oczadly.uk)
 * Licensed under the MIT License
 */

package uk.oczadly.karl.jnano.internal.utils;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author Karl Oczadly
 */
public final class LimitedCacheMap<K, V> extends LinkedHashMap<K, V> {
    
    private final int maxSize;
    
    public LimitedCacheMap(int maxSize) {
        this.maxSize = maxSize;
    }
    
    public LimitedCacheMap(int maxSize, int initialCapacity, float loadFactor) {
        super(initialCapacity, loadFactor);
        this.maxSize = maxSize;
    }
    
    
    public int getMaxSize() {
        return maxSize;
    }
    
    @Override
    public V put(K key, V value) {
        // Re-insert to head of list
        V val = remove(key);
        super.put(key, value);
        return val;
    }
    
    @Override
    protected boolean removeEldestEntry(Map.Entry eldest) {
        return size() > maxSize;
    }
    
}
