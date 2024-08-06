package com.modak.domain.service

import com.modak.domain.port.NotificationUseCase
import com.modak.domain.model.NotificationType
import com.modak.domain.port.GatewayUseCase
import com.modak.domain.port.RateLimitUseCase
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service

const val COUNTER_FIELD_NAME = "counter"
const val EXPIRES_AT_FIELD_NAME = "expiresAt"

@Service
class NotificationServiceImpl(
    private val gateway: GatewayUseCase,
    private val rateLimit: RateLimitUseCase
) : NotificationUseCase {

    val logger: Logger = LoggerFactory.getLogger(this::class.java)

    override fun send(type: NotificationType, userId: String, message: String): ResponseEntity<String> {

        if (rateLimit.isLimitExpired(userId, type)) {

            rateLimit.resetLimit(userId, type)

        } else if (rateLimit.isLimitReached(userId, type)) {

            val limitReachedMessage = "[USER $userId] LIMIT REACHED: Limit of [${type.capacity} notification every " +
                    "${type.duration} ${type.durationUnit}] reached for type $type."
            logger.info(limitReachedMessage)
            return ResponseEntity.status(429).body(limitReachedMessage)

        }

        gateway.send(userId, message, type)

        val limitCounter = rateLimit.increaseLimitCounter(userId, type)
        return ResponseEntity.ok("Message sent! Counter: [$limitCounter]")
    }
}