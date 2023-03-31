package com.bumble.appyx.multiplatform.core.plugin

import com.bumble.appyx.multiplatform.core.node.Node
import com.bumble.appyx.multiplatform.core.state.MutableSavedStateMap
import com.bumble.appyx.multiplatform.interfaces.Lifecycle
import com.bumble.appyx.multiplatform.interfaces.OnBackPressedCallback

interface Plugin


inline fun <reified P : Plugin> Node.plugins(): List<P> =
    this.plugins.filterIsInstance(P::class.java)

interface NodeAware<N : Node> : NodeReadyObserver<N> {
    val node: N
}

interface NodeReadyObserver<N : Node> : Plugin {
    fun init(node: N) {}
}

interface NodeLifecycleAware : Plugin {
    fun onCreate(lifecycle: Lifecycle) {}
}

interface UpNavigationHandler : Plugin {
    fun handleUpNavigation(): Boolean = false
}

fun interface Destroyable : Plugin {
    fun destroy()
}

/**
 * Implementing class can handle back presses via [OnBackPressedCallback].
 *
 * Implement either [onBackPressedCallback] or [onBackPressedCallbackList], not both.
 * In case if both implemented, [onBackPressedCallback] will be ignored.
 * There is runtime check in [Node] to verify correctness.
 */
interface BackPressHandler : Plugin {

    val onBackPressedCallback: OnBackPressedCallback? get() = null

    /** It is impossible to combine multiple [OnBackPressedCallback] into the single one, they are not observable. */
    val onBackPressedCallbackList: List<OnBackPressedCallback>
        get() = listOfNotNull(onBackPressedCallback)

    fun handleOnBackPressed(): Boolean =
        onBackPressedCallbackList.any { callback ->
            val isEnabled = callback.isEnabled
            if (isEnabled) callback.handleOnBackPressed()
            isEnabled
        }

}

/**
 * Bundle for future state restoration.
 * Result should be supported by [androidx.compose.runtime.saveable.SaverScope.canBeSaved].
 */
interface SavesInstanceState : Plugin {
    fun saveInstanceState(state: MutableSavedStateMap) {}
}
