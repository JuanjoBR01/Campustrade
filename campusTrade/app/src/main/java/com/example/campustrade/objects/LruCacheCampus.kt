package com.example.campustrade.objects

import androidx.collection.LruCache

object LruCacheCampus {
    class LRUCache<K, V>(private val maxSize: Int) {
        private val cache: LinkedHashMap<K, V> = object : LinkedHashMap<K, V>(maxSize, 0.75f, true) {
            override fun removeEldestEntry(eldest: MutableMap.MutableEntry<K, V>?): Boolean {
                return size > maxSize
            }
        }

        fun get(key: K): V? {
            return cache[key]
        }

        fun put(key: K, value: V) {
            cache[key] = value
        }

        fun getAllValues(): MutableCollection<V> {
            return cache.values
        }

        fun delete(key: K){
            cache.remove(key)
        }
    }

    var lruCacheApp = LRUCache<String, Any>(10)
}