package ru.job4j.cache;

import java.lang.ref.SoftReference;
import java.util.HashMap;
import java.util.Map;

public abstract class AbstractCache<K, V> {

    protected final Map<K, SoftReference<V>> cache = new HashMap<>();

    public void put(K key) {
        cache.put(key, new SoftReference<>(load(key)));
    }

    public V get(K key) {
        V value = cache.getOrDefault(key, new SoftReference<>(null)).get();
        while (value == null) {
            put(key);
            value = get(key);
        }
        return value;
    }

    protected abstract V load(K key);

}