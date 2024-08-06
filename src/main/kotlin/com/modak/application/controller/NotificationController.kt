package com.modak.application.controller

import com.modak.application.request.SendNotificationRequest
import com.modak.domain.model.NotificationType
import com.modak.domain.port.NotificationUseCase
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
class NotificationController(
    private val notificationService: NotificationUseCase
) {

    @PostMapping("/notification")
    fun sendNotification(
        @RequestBody request: SendNotificationRequest
    ): ResponseEntity<String> {
        return notificationService.send(NotificationType.valueOf(request.type), request.userId, request.message)
    }
}