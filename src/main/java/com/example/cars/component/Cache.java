package com.example.cars.component;

import org.springframework.stereotype.Component;

import java.util.LinkedHashMap;
import java.util.Map;

@Component
public class Cache<K, V> {

   private static final int MAX_ENTRIES = 100;

   private final Map<K, V> cache = new LinkedHashMap<>(MAX_ENTRIES, 0.75f, true) {
      @Override
      protected boolean removeEldestEntry(Map.Entry<K, V> eldest) {
         return size() > MAX_ENTRIES;
      }
   };

   public V get(K key) {
      return cache.get(key);
   }

   public void put(K key, V value) {
      cache.put(key, value);
   }

   public boolean containsKey(K key) {
      return cache.containsKey(key);
   }

   public void evict(K key) {
      cache.remove(key);
   }

   public void clear() {
      cache.clear();
   }
}