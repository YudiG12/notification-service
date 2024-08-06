package com.modak.domain.port

import com.modak.domain.model.NotificationType

interface RateLimitUseCase {
    fun isLimitExpired(userId: String, type: NotificationType): Boolean
    fun isLimitReached(userId: String, type: NotificationType): Boolean
    fun resetLimit( userId: String, type: NotificationType)
    fun increaseLimitCounter(userId: String, type: NotificationType): Int
}