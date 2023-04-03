package com.bumble.appyx.multiplatform.core.lifecycle

import com.bumble.appyx.multiplatform.interfaces.Lifecycle
import com.bumble.appyx.multiplatform.interfaces.LifecycleRegistry
import kotlinx.coroutines.CoroutineScope

internal class NodeLifecycleImpl(
    private val lifecycleRegistry: LifecycleRegistry
) : NodeLifecycle {

    override val lifecycle: Lifecycle = lifecycleRegistry
    override val lifecycleScope: CoroutineScope = lifecycleRegistry.coroutineScope

    override fun updateLifecycleState(state: Lifecycle.State) {
        lifecycleRegistry.setCurrentState(state)
    }
}
