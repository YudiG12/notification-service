package com.modak.domain.port

import com.modak.domain.model.NotificationType

interface GatewayUseCase {
    fun send(userId: String, message: String, type: NotificationType)
}