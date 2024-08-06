package com.modak.domain.model

import kotlin.time.DurationUnit

enum class NotificationType(
    val capacity: Int,
    val duration: Int,
    val durationUnit: DurationUnit
) {
    STATUS(2, 1, DurationUnit.MINUTES),
    NEWS(1, 1, DurationUnit.DAYS),
    MARKETING(3, 1, DurationUnit.HOURS),
}