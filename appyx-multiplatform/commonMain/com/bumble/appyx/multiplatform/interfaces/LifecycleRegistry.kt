package com.bumble.appyx.multiplatform.interfaces

interface LifecycleRegistry : Lifecycle {
    fun setCurrentState(state: Lifecycle.State)
}

interface LifecycleRegistryProvider : (LifecycleOwner) -> LifecycleRegistry