package com.wei.picquest.core.network

import javax.inject.Qualifier
import kotlin.annotation.AnnotationRetention.RUNTIME

@Qualifier
@Retention(RUNTIME)
annotation class Dispatcher(val pqDispatcher: PqDispatchers)

enum class PqDispatchers {
    Default,
    IO,
}
