/*
 * Copyright (c) 2021 Karl Oczadly (karl@oczadly.uk)
 * Licensed under the MIT License
 */

package uk.oczadly.karl.jnano.util.workgen;

import uk.oczadly.karl.jnano.internal.NanoConst;
import uk.oczadly.karl.jnano.model.HexData;
import uk.oczadly.karl.jnano.model.work.WorkDifficulty;
import uk.oczadly.karl.jnano.model.work.WorkSolution;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;

/**
 * A thread-safe class used for caching and retrieving generated work values.
 *
 * <p>New values will overwrite older values when the limit is reached, and works with higher difficulties will
 * overwrite any lower difficulty work solutions which may be stored.</p>
 *
 * @see #GLOBAL_INSTANCE
 */
public final class WorkCache {
    
    /**
     * A global WorkCache instance which stores up to {@code 100} cached work solutions. Used internally by the
     * {@link AbstractWorkGenerator} instances.
     */
    public static final WorkCache GLOBAL_INSTANCE = new WorkCache(100);
    
    private final int maxSize;
    private final Map<HexData, CachedWork> map;
    
    /**
     * Constructs a work cache with the specified maximum cache size.
     * @param maxSize the maximum number of work solutions to cache
     */
    public WorkCache(int maxSize) {
        this.maxSize = maxSize;
        this.map = new LimitedMap<>();
    }
    
    
    /**
     * Returns the maximum number of work values which can be cached.
     * @return the maximum number of cached values
     */
    public int getMaxSize() {
        return maxSize;
    }
    
    /**
     * Returns the current number of items stored in the cache.
     * @return the current size of the cache
     */
    public synchronized int getCacheSize() {
        return map.size();
    }
    
    /**
     * Clears all the values stored in the cache.
     */
    public synchronized void clear() {
        map.clear();
    }
    
    /**
     * Stores the generated work in the cache.
     * @param work the generated work to cache
     */
    public synchronized void store(GeneratedWork work) {
        if (work == null) throw new IllegalArgumentException("Work cannot be null.");
        store(work.getRequestRoot(), work.getWork(), work.getDifficulty());
    }
    
    /**
     * Stores the generated work in the cache.
     * @param root the root the work was generated for
     * @param work the generated work solution
     */
    public synchronized void store(HexData root, WorkSolution work) {
        if (root == null) throw new IllegalArgumentException("Root cannot be null.");
        if (work == null) throw new IllegalArgumentException("Work cannot be null.");
        store(root, work, work.calculateDifficulty(root));
    }
    
    private synchronized void store(HexData root, WorkSolution work, WorkDifficulty difficulty) {
        if (root.length() != NanoConst.LEN_HASH_B) throw new IllegalArgumentException("Invalid root length.");
        
        CachedWork current = map.get(root);
        if (current != null && difficulty.compareTo(current.difficulty) < 0) {
            map.put(root, current); // Bump existing cache
        } else {
            map.put(root, new CachedWork(work, difficulty)); // Add to cache
        }
    }
    
    /**
     * Returns the current cached value for the given root <em>if</em> the cached work meets the specified difficulty
     * threshold.
     *
     * @param root      the root hash that the work is for
     * @param threshold the minimum difficulty threshold
     * @return the cached work value, or empty if not cached or if the work doesn't meet the difficulty threshold
     */
    public synchronized Optional<WorkSolution> get(HexData root, WorkDifficulty threshold) {
        if (root == null) throw new IllegalArgumentException("Root cannot be null.");
        if (root.length() != NanoConst.LEN_HASH_B) throw new IllegalArgumentException("Invalid root length.");
        if (threshold == null) throw new IllegalArgumentException("Threshold cannot be null.");
        
        CachedWork cache = map.get(root);
        if (cache != null && cache.difficulty.isValid(threshold))
            return Optional.of(cache.work);
        return Optional.empty();
    }
    
    @Override
    public String toString() {
        return "WorkCache{" +
                "maxSize=" + getMaxSize() +
                ", cacheSize=" + getCacheSize() + '}';
    }
    
    
    /** Stores the work and difficulty for the given root. */
    private static class CachedWork {
        private final WorkSolution work;
        private final WorkDifficulty difficulty;
        
        public CachedWork(WorkSolution work, WorkDifficulty difficulty) {
            this.work = work;
            this.difficulty = difficulty;
        }
    }
    
    /** Map limited to the maxSize */
    private class LimitedMap<K, V> extends LinkedHashMap<K, V> {
        @Override
        public V put(K key, V value) {
            // Re-insert to head of list by removing and re-adding
            V val = remove(key);
            super.put(key, value);
            return val;
        }
        
        @Override
        protected boolean removeEldestEntry(Map.Entry eldest) {
            return size() > maxSize;
        }
    }
    
}
