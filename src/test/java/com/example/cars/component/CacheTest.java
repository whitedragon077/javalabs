package com.example.cars.component;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CacheTest {

    private Cache<String, String> cache;

    @BeforeEach
    void setUp() {
        cache = new Cache<>();
    }

    @Test
    void testPutAndGet() {
        cache.put("key1", "value1");
        assertEquals("value1", cache.get("key1"));
    }

    @Test
    void testContainsKey() {
        cache.put("key1", "value1");
        assertTrue(cache.containsKey("key1"));
    }

    @Test
    void testClear() {
        cache.put("key1", "value1");
        cache.clear();
        assertFalse(cache.containsKey("key1"));
    }

    @Test
    void testPutListAndGetList() {
        List<String> valueList = new ArrayList<>();
        valueList.add("value1");
        valueList.add("value2");
        cache.putList("key1", valueList);
        assertEquals(valueList, cache.getList("key1"));
    }
}
