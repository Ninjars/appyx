package com.bumble.appyx.multiplatform.core.lifecycle

import com.bumble.appyx.multiplatform.interfaces.Lifecycle
import com.bumble.appyx.multiplatform.interfaces.LifecycleRegistry
import kotlin.coroutines.CoroutineContext

internal class NodeLifecycleImpl(
    private val lifecycleRegistry: LifecycleRegistry
) : NodeLifecycle {

    override fun getLifecycle(): Lifecycle =
        lifecycleRegistry

    override fun getScopedCoroutineContext(): CoroutineContext =
        lifecycleRegistry.getScopedCoroutineContext()

    override fun updateLifecycleState(state: Lifecycle.State) {
        lifecycleRegistry.setCurrentState(state)
    }
}
