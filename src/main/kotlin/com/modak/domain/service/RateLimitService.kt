package com.modak.domain.service

import com.modak.domain.model.NotificationType
import com.modak.domain.model.RateLimiterKeyName
import com.modak.domain.port.RateLimitUseCase
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import redis.clients.jedis.JedisPool
import kotlin.time.toTimeUnit

@Service
class RateLimitService: RateLimitUseCase {

    val logger: Logger = LoggerFactory.getLogger(this::class.java)

    override fun isLimitExpired(userId: String, type: NotificationType): Boolean {
        val keyName = RateLimiterKeyName(type, userId).toString()
        JedisPool().resource.use { jedis ->
            val expiresAt = jedis.hget(keyName, EXPIRES_AT_FIELD_NAME)?.toLong() ?: 0
            return expiresAt < System.currentTimeMillis()
        }
    }

    override fun isLimitReached(userId: String, type: NotificationType): Boolean {
        val keyName = RateLimiterKeyName(type, userId).toString()
        JedisPool().resource.use { jedis ->
            val capacityCounter = jedis.hget(keyName, COUNTER_FIELD_NAME)?.toInt() ?: 0
            return capacityCounter >= type.capacity
        }
    }

    override fun resetLimit(userId: String, type: NotificationType) {
        val keyName = RateLimiterKeyName(type, userId).toString()
        JedisPool().resource.use { jedis ->
            jedis.hset(keyName, COUNTER_FIELD_NAME, "0")
            val millisDuration = type.durationUnit.toTimeUnit().toMillis(type.duration.toLong())
            jedis.hset(keyName, EXPIRES_AT_FIELD_NAME, (System.currentTimeMillis() + millisDuration).toString())
            logger.info("[USER $userId] LIMIT RESET: [$type]")
        }
    }

    override fun increaseLimitCounter(userId: String, type: NotificationType): Int {
        val keyName = RateLimiterKeyName(type, userId).toString()
        JedisPool().resource.use { jedis ->
            val capacityCounterAfterUpdate = jedis.hget(keyName, COUNTER_FIELD_NAME)?.toInt() ?: 0
            jedis.hset(keyName, COUNTER_FIELD_NAME, (capacityCounterAfterUpdate + 1).toString())
            return capacityCounterAfterUpdate + 1
        }
    }
}