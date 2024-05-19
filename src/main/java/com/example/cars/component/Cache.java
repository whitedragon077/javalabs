package com.example.cars.component;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Component;

/**
 * A simple caching component based on a LinkedHashMap with a maximum number of entries.
 *
 * @param <K> the type of keys maintained by this cache
 * @param <V> the type of mapped values
 */
@Component
public class Cache<K, V> {

  private static final int MAX_ENTRIES = 10;

  private final Map<K, List<V>> carCache = new LinkedHashMap<>(MAX_ENTRIES, 0.75f, true) {
    @Override
      protected boolean removeEldestEntry(Map.Entry<K, List<V>> eldest) {
        return size() > MAX_ENTRIES;
    }
  };


  /**
  * Retrieve the value associated with the specified key from the cache.
  *
  * @param key the key whose associated value is to be returned
  * @return the value to which the specified key is mapped, or null if the cache contains no
  *     mapping for the key
  */
  public V get(K key) {
    List<V> valueList = carCache.get(key);
    if (valueList != null && !valueList.isEmpty()) {
      return valueList.get(0);
    }
    return null;
  }

  /**
  * Put a key-value pair into the cache.
  *
  * @param key   the key with which the specified value is to be associated
  * @param value the value to be associated with the specified key
  */
  public void put(K key, V value) {
    List<V> valueList = new ArrayList<>();
    valueList.add(value);
    carCache.put(key, valueList);
  }

  public boolean containsKey(K key) {
    return carCache.containsKey(key);
  }

  public void clear() {
    carCache.clear();
  }

  public void putList(K key, List<V> valueList) {
    carCache.put(key, valueList);
  }

  public List<V> getList(K key) {
    return carCache.get(key);
  }

}