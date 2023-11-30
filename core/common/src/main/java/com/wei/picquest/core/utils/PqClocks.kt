package com.wei.picquest.core.utils

import javax.inject.Qualifier
import kotlin.annotation.AnnotationRetention.RUNTIME

@Qualifier
@Retention(RUNTIME)
annotation class Clocks(val pqClock: PqClocks)

enum class PqClocks {
    DefaultClock,
    UtcClock,
}
