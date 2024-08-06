package com.modak

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class Main

fun main(args: Array<String>) {
    runApplication<Main>(*args)

//    Hello Modak team! If you dont want to use the api, you can run the sample below.
//    runSample()
}

//fun runSample() {
//    val notificationService = NotificationServiceImpl(EmailGateway(), RateLimitService())
//    notificationService.send(NotificationType.NEWS, "user", "news 1")
//    notificationService.send(NotificationType.NEWS, "user2", "news 1")
//    notificationService.send(NotificationType.STATUS, "user2", "status 1")
//    notificationService.send(NotificationType.MARKETING, "user", "marketing 1")
//}