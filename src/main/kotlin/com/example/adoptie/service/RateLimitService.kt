package com.example.adoptie.service

import io.github.bucket4j.Bandwidth
import io.github.bucket4j.Bucket
import io.github.bucket4j.Refill
import org.springframework.stereotype.Service
import java.time.Duration
import java.util.concurrent.ConcurrentHashMap

@Service
class RateLimitService {
    private val buckets = ConcurrentHashMap<String, Bucket>()

    private val capacity: Long = 3
    private val minutes: Long = 2

    private fun createNewBucket(): Bucket {
        val limit = Bandwidth.classic(capacity, Refill.intervally(capacity, Duration.ofMinutes(minutes)))
        return Bucket.builder()
            .addLimit(limit)
            .build()
    }

    fun resolveBucket(key: String): Bucket {
        return buckets.computeIfAbsent(key) { createNewBucket() }
    }
}