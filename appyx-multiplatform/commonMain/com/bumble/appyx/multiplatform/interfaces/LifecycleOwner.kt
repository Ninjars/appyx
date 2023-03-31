package com.bumble.appyx.multiplatform.interfaces

import kotlin.coroutines.CoroutineContext

interface LifecycleOwner {
    fun getLifecycle(): Lifecycle

    /*
     on android: Lifecycle.coroutineScope
     */
    fun getScopedCoroutineContext(): CoroutineContext
}

interface LocalLifecycleOwnerProvider : () -> LifecycleOwner
