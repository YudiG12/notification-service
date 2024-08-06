package com.modak.domain.model

data class RateLimiterKeyName(
    val type: NotificationType,
    val userId: String
) {
    override fun toString(): String {
        return "${type.name}::$userId"
    }
}
