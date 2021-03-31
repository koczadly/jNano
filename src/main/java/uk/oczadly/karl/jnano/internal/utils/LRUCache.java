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
public class LRUCache<K, V> extends LinkedHashMap<K, V> {
    
    private final int maxSize;
    
    public LRUCache(int maxSize, int initialCapacity, float loadFactor) {
        super(initialCapacity, loadFactor, true);
        this.maxSize = maxSize;
    }
    
    public LRUCache(int maxSize, int initialCapacity) {
        this(maxSize, initialCapacity, 0.75f);
    }
    
    public LRUCache(int maxSize) {
        this(maxSize, 16, 0.75f);
    }
    
    
    @Override
    protected boolean removeEldestEntry(Map.Entry eldest) {
        return size() > maxSize;
    }
    
    public final int maxSize() {
        return maxSize;
    }

}
