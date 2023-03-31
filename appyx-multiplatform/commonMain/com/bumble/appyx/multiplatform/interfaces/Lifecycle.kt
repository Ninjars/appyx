package com.bumble.appyx.multiplatform.interfaces

import kotlin.coroutines.CoroutineContext

interface Lifecycle {
    var currentState: State

    fun addObserver(observer: LifecycleObserver)

    /*
     on android: Lifecycle.coroutineScope
     */
    fun getScopedCoroutineContext(): CoroutineContext

    enum class State {
        CREATED,
        DESTROYED,
    }
}
