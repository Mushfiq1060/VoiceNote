package com.openai.voicenote.core.common

import javax.inject.Qualifier

@Qualifier
@Retention(AnnotationRetention.RUNTIME)
annotation class Dispatcher(val vnDispatcher: VnDispatchers)
enum class VnDispatchers {
    Default,
    IO
}