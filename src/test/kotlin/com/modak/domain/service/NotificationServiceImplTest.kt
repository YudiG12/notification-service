package com.modak.domain.service

import com.modak.domain.model.NotificationType
import com.modak.domain.port.GatewayUseCase
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlin.test.Test
import kotlin.test.assertEquals

class NotificationServiceImplTest {
    private val gatewayService = mockk<GatewayUseCase>()
    private val rateLimitService = mockk<RateLimitService>()
    private val notificationService = NotificationServiceImpl(gatewayService, rateLimitService)

    @Test
    fun `Send notification`() {
        every { gatewayService.send(any(), any(), any()) } returns Unit
        every { rateLimitService.isLimitExpired(any(), any()) } returns false
        every { rateLimitService.isLimitReached(any(), any()) } returns false
        every { rateLimitService.increaseLimitCounter(any(), any()) } returns 1

        val response = notificationService.send(NotificationType.NEWS, "user", "news 1")

        verify(exactly = 0) { rateLimitService.resetLimit(any(), any()) }
        verify(exactly = 1) { gatewayService.send(any(), any(), any()) }
        assertEquals(200, response.statusCode.value())
    }

    @Test
    fun `Limit expired`() {
        every { gatewayService.send(any(), any(), any()) } returns Unit
        every { rateLimitService.isLimitExpired(any(), any()) } returns true
        every { rateLimitService.resetLimit(any(), any()) } returns Unit
        every { rateLimitService.isLimitReached(any(), any()) } returns false
        every { rateLimitService.increaseLimitCounter(any(), any()) } returns 1

        val response = notificationService.send(NotificationType.NEWS, "user", "news 1")

        verify(exactly = 1) { rateLimitService.resetLimit(any(), any()) }
        assertEquals(200, response.statusCode.value())
    }

    @Test
    fun `Limit reached`() {
        every { gatewayService.send(any(), any(), any()) } returns Unit
        every { rateLimitService.isLimitExpired(any(), any()) } returns false
        every { rateLimitService.isLimitReached(any(), any()) } returns true
        every { rateLimitService.increaseLimitCounter(any(), any()) } returns 1

        val response = notificationService.send(NotificationType.NEWS, "user", "news 1")

        assertEquals(429, response.statusCode.value())
    }
}