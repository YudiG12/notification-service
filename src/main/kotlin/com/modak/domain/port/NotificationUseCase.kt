package com.modak.domain.port

import com.modak.domain.model.NotificationType
import org.springframework.http.ResponseEntity

interface NotificationUseCase {
    fun send(type: NotificationType, userId: String, message: String): ResponseEntity<String>
}