package ru.cache.cachehw;

import java.util.*;

public class MyCache<K, V> implements HwCache<K, V> {
    // Надо реализовать эти методы
    private final Map<K, V> cacheHashMap = new WeakHashMap<>();
    private final Set<HwListener<K, V>> listeners = new HashSet<>();

    @Override
    public void put(K key, V value) {
        cacheHashMap.put(key, value);
        notifyListeners(key, value, "PUT");
    }

    @Override
    public void remove(K key) {
        V value = cacheHashMap.get(key);
        cacheHashMap.remove(key);
        notifyListeners(key, value, "REMOVE");
    }

    @Override
    public V get(K key) {
        V value = cacheHashMap.get(key);
        notifyListeners(key, value, "GET");
        return value;
    }

    @Override
    public void addListener(HwListener<K, V> listener) {
        if (listeners.contains(listener)) {
            throw new IllegalArgumentException("Данный listener уже добавлен в listeners");
        }
        listeners.add(listener);
    }

    @Override
    public void removeListener(HwListener<K, V> listener) {
        if (listeners.contains(listener)) {
            listeners.remove(listener);
        } else {
            throw new IllegalArgumentException("listener не найден");
        }
    }

    private void notifyListeners(K key, V value, String action) {
        try {
        List<HwListener<K, V>> listenersCopy = new ArrayList<>(listeners);
        for (HwListener<K, V> listener : listenersCopy) {
            listener.notify(key, value, action);
        } }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
        }
    }

