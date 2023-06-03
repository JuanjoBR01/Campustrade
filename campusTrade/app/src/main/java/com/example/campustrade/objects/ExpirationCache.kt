package com.example.campustrade.objects

object ExpirationCache {

    class ExpiringCache<K, V>(private val maxSize: Int, private val expirationMs: Long) {
        private val cache: MutableMap<K, CacheEntry<V>> = HashMap()

        private data class CacheEntry<V>(
            val value: V,
            val expirationTime: Long
        )

        fun put(key: K, value: V) {
            if (cache.size >= maxSize) {
                clearExpiredEntries()
                if (cache.size >= maxSize) {
                    val oldestKey = cache.keys.iterator().next()
                    cache.remove(oldestKey)
                }
            }
            val expirationTime = System.currentTimeMillis() + expirationMs
            cache[key] = CacheEntry(value, expirationTime)
        }

        fun get(key: K): V? {
            val entry = cache[key]
            if (entry != null && entry.expirationTime >= System.currentTimeMillis()) {
                return entry.value
            }
            return null
        }

        fun clear() {
            cache.clear()
        }

        private fun clearExpiredEntries() {
            val iterator = cache.entries.iterator()
            while (iterator.hasNext()) {
                val entry = iterator.next()
                if (entry.value.expirationTime < System.currentTimeMillis()) {
                    iterator.remove()
                }
            }
        }
    }

    var expiringCacheApp = ExpiringCache<String, Any>(10, 60000)
}