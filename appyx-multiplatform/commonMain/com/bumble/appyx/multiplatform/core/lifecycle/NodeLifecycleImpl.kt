package com.bumble.appyx.multiplatform.core.lifecycle

import com.bumble.appyx.multiplatform.interfaces.Lifecycle
import com.bumble.appyx.multiplatform.interfaces.LifecycleRegistry

internal class NodeLifecycleImpl(
    private val lifecycleRegistry: LifecycleRegistry
) : NodeLifecycle {

    override fun getLifecycle(): Lifecycle =
        lifecycleRegistry

    override fun updateLifecycleState(state: Lifecycle.State) {
        lifecycleRegistry.setCurrentState(state)
    }
}
