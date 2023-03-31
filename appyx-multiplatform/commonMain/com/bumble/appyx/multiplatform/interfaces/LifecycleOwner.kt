package com.bumble.appyx.multiplatform.interfaces

interface LifecycleOwner {
    val lifecycle: Lifecycle
}

interface LocalLifecycleOwnerProvider : () -> LifecycleOwner
