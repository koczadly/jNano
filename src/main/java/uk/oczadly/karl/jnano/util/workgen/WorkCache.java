/*
 * Copyright (c) 2021 Karl Oczadly (karl@oczadly.uk)
 * Licensed under the MIT License
 */

package uk.oczadly.karl.jnano.util.workgen;

import uk.oczadly.karl.jnano.internal.NanoConst;
import uk.oczadly.karl.jnano.internal.utils.LRUCache;
import uk.oczadly.karl.jnano.model.HexData;
import uk.oczadly.karl.jnano.model.work.WorkDifficulty;
import uk.oczadly.karl.jnano.model.work.WorkSolution;

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
    
    private final LRUCache<HexData, CachedWork> map;
    
    /**
     * Constructs a work cache with the specified maximum cache size.
     * @param maxSize the maximum number of work solutions to cache
     */
    public WorkCache(int maxSize) {
        if (maxSize < 1)
            throw new IllegalArgumentException("Must have a maximum cache size of at least 1.");
        this.map = new LRUCache<>(maxSize);
    }
    
    
    /**
     * Returns the maximum number of work values which can be cached.
     * @return the maximum number of cached values
     */
    public int getMaxSize() {
        return map.maxSize();
    }
    
    /**
     * Returns the current number of items stored in the cache.
     * @return the current size of the cache
     */
    public synchronized int size() {
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
     *
     * @param work the generated work to cache
     * @return true if the work was written to the cache, false if the cache already has a higher difficulty value
     *         stored for this root
     */
    public boolean store(GeneratedWork work) {
        if (work == null) throw new IllegalArgumentException("Work cannot be null.");
        return store(work.getWork(), work.getDifficulty(), work.getRequestRoot());
    }
    
    /**
     * Stores the generated work in the cache.
     *
     * @param work the generated work solution
     * @param root the root the work was generated for
     * @return true if the work was written to the cache, false if the cache already has a higher difficulty value
     *         stored for this root
     */
    public boolean store(WorkSolution work, HexData root) {
        if (work == null) throw new IllegalArgumentException("Work cannot be null.");
        if (root == null) throw new IllegalArgumentException("Root cannot be null.");
        return store(work, work.calculateDifficulty(root), root);
    }
    
    private synchronized boolean store(WorkSolution work, WorkDifficulty difficulty, HexData root) {
        if (root.length() != NanoConst.LEN_HASH_B)
            throw new IllegalArgumentException("Invalid root length.");
        
        CachedWork current = map.get(root);
        if (current != null && difficulty.compareTo(current.difficulty) < 0) {
            // Bump existing cache (but don't overwrite)
            map.put(root, current);
            return false;
        } else {
            // Add to cache
            map.put(root, new CachedWork(work, difficulty));
            return true;
        }
    }
    
    /**
     * Removes any cached work for the specified root.
     *
     * @param root the root hash
     * @return true if a cached work value was removed
     */
    public synchronized boolean remove(HexData root) {
        if (root == null)
            throw new IllegalArgumentException("Root cannot be null.");
        if (root.length() != NanoConst.LEN_HASH_B)
            throw new IllegalArgumentException("Invalid root length.");
        return map.remove(root) != null;
    }
    
    /**
     * Returns the current cached value for the given root if the cached work is greater than the specified difficulty
     * threshold.
     *
     * @param root      the root hash that the work is for
     * @param threshold the minimum acceptable difficulty threshold of the work
     * @return the cached work value, or empty if not cached or if the work doesn't meet the difficulty threshold
     */
    public synchronized Optional<WorkSolution> get(HexData root, WorkDifficulty threshold) {
        if (root == null)
            throw new IllegalArgumentException("Root cannot be null.");
        if (root.length() != NanoConst.LEN_HASH_B)
            throw new IllegalArgumentException("Invalid root length.");
        if (threshold == null)
            throw new IllegalArgumentException("Threshold cannot be null.");
        
        CachedWork cache = map.get(root);
        if (cache != null && cache.difficulty.isValid(threshold))
            return Optional.of(cache.work);
        return Optional.empty();
    }
    
    @Override
    public String toString() {
        return "WorkCache{" +
                "maxSize=" + getMaxSize() +
                ", size=" + size() + '}';
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
    
}
