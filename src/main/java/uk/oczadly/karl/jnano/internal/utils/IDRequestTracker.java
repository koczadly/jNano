/*
 * Copyright (c) 2021 Karl Oczadly (karl@oczadly.uk)
 * Licensed under the MIT License
 */

package uk.oczadly.karl.jnano.internal.utils;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Tracks a pending request.
 */
public class IDRequestTracker<T> {
    
    private final AtomicLong nextId = new AtomicLong();
    private final Map<String, Tracker> map = new ConcurrentHashMap<>();
    
    
    public synchronized void cancelAll() {
        for (Tracker t : map.values()) {
            t.set(null, false);
        }
    }
    
    public synchronized Tracker newTracker() {
        String id = Long.toHexString(nextId.incrementAndGet());
        Tracker tracker = new Tracker(id);
        map.put(id.toLowerCase(), tracker);
        return tracker;
    }
    
    public synchronized boolean complete(String id, T result) {
        Tracker tracker = map.get(id.toLowerCase());
        if (tracker != null)
            return tracker.set(result, true);;
        return false;
    }
    
    
    @Override
    protected void finalize() throws Throwable {
        try {
            cancelAll();
        } finally {
            super.finalize();
        }
    }
    
    public class Tracker {
        private final String id;
        private final CountDownLatch latch = new CountDownLatch(1);
        private volatile T result;
        private volatile boolean success;
    
        public Tracker(String id) {
            this.id = id;
        }
    
        public String getID() {
            return id;
        }
    
        public T await() throws InterruptedException, TrackerExpiredException {
            try {
                latch.await();
                if (success)
                    return result;
                throw new TrackerExpiredException();
            } finally {
                map.remove(id.toLowerCase());
            }
        }
    
        public T await(long timeout, TimeUnit unit) throws InterruptedException, TimeoutException,
                                                           TrackerExpiredException {
            try {
                if (!latch.await(timeout, unit))
                    throw new TimeoutException();
                if (success)
                    return result;
                throw new TrackerExpiredException();
            } finally {
                map.remove(id.toLowerCase());
            }
        }
        
        public void expire() {
            if (latch.getCount() != 0)
                set(null, false);
            map.remove(id.toLowerCase());
        }
        
        private synchronized boolean set(T result, boolean success) {
            if (latch.getCount() > 0) {
                this.result = result;
                this.success = success;
                this.latch.countDown();
                return true;
            }
            return false;
        }
    }
    
    public static class TrackerExpiredException extends Exception {}

}
