package com.bumble.appyx.multiplatform.interfaces

interface LifecycleOwner {
    fun getLifecycle(): Lifecycle
}

interface LocalLifecycleOwnerProvider : () -> LifecycleOwner
