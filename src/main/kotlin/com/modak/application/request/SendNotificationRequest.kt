package com.modak.application.request

data class SendNotificationRequest(
    val userId: String,
    val message: String,
    val type: String
)