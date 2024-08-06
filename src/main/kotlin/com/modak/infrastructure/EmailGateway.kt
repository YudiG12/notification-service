package com.modak.infrastructure

import com.modak.domain.model.NotificationType
import com.modak.domain.port.GatewayUseCase
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service

@Service
class EmailGateway: GatewayUseCase {

    val logger: Logger = LoggerFactory.getLogger(this::class.java)

    override fun send(userId: String, message: String, type: NotificationType) {
        logger.info("[USER $userId] SEND NOTIFICATION: Sending message to user of type $type")
    }
}