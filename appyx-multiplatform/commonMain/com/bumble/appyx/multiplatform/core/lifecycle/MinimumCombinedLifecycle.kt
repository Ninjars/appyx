package com.bumble.appyx.multiplatform.core.lifecycle

import com.bumble.appyx.multiplatform.interfaces.DefaultLifecycleObserver
import com.bumble.appyx.multiplatform.interfaces.Lifecycle
import com.bumble.appyx.multiplatform.interfaces.LifecycleOwner
import com.bumble.appyx.multiplatform.interfaces.LifecycleRegistryProvider
import kotlinx.coroutines.CoroutineScope

/**
 * Combines multiple lifecycles and provides a minimum of their states.
 *
 * For example:
 * - RESUMED + STARTED + RESUMED -> STARTED
 * - CREATED + RESUMED + DESTROYED -> DESTROYED
 * - INITIALIZED + DESTROYED -> DESTROYED
 */
internal class MinimumCombinedLifecycle(
    lifecycleRegistryProvider: LifecycleRegistryProvider,
    vararg lifecycles: Lifecycle,
) : LifecycleOwner {
    private val registry = lifecycleRegistryProvider(this)
    private val lifecycles = ArrayList<Lifecycle>()

    init {
        /*
        Sort list to avoid unnecessary state jumps.
        If Lifecycle(RESUMED) + Lifecycle(DESTROYED) is passed,
        then we should have the final state in DESTROYED state without additional jumping to RESUMED.
         */
        lifecycles.sortedBy { it.currentState }.forEach { manage(it) }
    }

    override val lifecycle: Lifecycle = registry
    override val lifecycleScope: CoroutineScope = registry.coroutineScope

    fun manage(lifecycle: Lifecycle) {
        lifecycles += lifecycle
        lifecycle.addObserver(object : DefaultLifecycleObserver {
            override fun onCreate(owner: LifecycleOwner) {
                update()
            }

            override fun onStart(owner: LifecycleOwner) {
                update()
            }

            override fun onResume(owner: LifecycleOwner) {
                update()
            }

            override fun onPause(owner: LifecycleOwner) {
                update()
            }

            override fun onStop(owner: LifecycleOwner) {
                update()
            }

            override fun onDestroy(owner: LifecycleOwner) {
                update()
            }
        })
        update()
    }

    private fun update() {
        lifecycles
            .minByOrNull { it.currentState }
            ?.takeIf { it.currentState != Lifecycle.State.INITIALIZED }
            ?.also { registry.currentState = it.currentState }
    }

}
