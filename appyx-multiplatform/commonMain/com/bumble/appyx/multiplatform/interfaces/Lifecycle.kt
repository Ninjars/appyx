package com.bumble.appyx.multiplatform.interfaces

import kotlin.coroutines.CoroutineContext

interface Lifecycle {
    var currentState: State

    fun addObserver(observer: LifecycleObserver)

    fun removeObserver(observer: LifecycleObserver)

    /*
     on android: Lifecycle.coroutineScope
     */
    fun getScopedCoroutineContext(): CoroutineContext

    enum class State {
        INITIALIZED,
        CREATED,
        RESUMED,
        DESTROYED,
    }

    enum class Event {
        ON_CREATE,
        ON_START,
        ON_RESUME,
        ON_PAUSE,
        ON_STOP,
        ON_DESTROY,
        ON_ANY,
    }
}
