package com.bumble.appyx.multiplatform.core.node

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.Stable
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.SaverScope
import androidx.compose.ui.Modifier
import com.bumble.appyx.multiplatform.core.integrationpoint.IntegrationPoint
import com.bumble.appyx.multiplatform.core.integrationpoint.IntegrationPointStub
import com.bumble.appyx.multiplatform.core.integrationpoint.requestcode.RequestCodeClient
import com.bumble.appyx.multiplatform.core.lifecycle.NodeLifecycle
import com.bumble.appyx.multiplatform.core.lifecycle.NodeLifecycleImpl
import com.bumble.appyx.multiplatform.core.modality.AncestryInfo
import com.bumble.appyx.multiplatform.core.modality.BuildContext
import com.bumble.appyx.multiplatform.core.plugin.BackPressHandler
import com.bumble.appyx.multiplatform.core.plugin.Destroyable
import com.bumble.appyx.multiplatform.core.plugin.NodeLifecycleAware
import com.bumble.appyx.multiplatform.core.plugin.NodeReadyObserver
import com.bumble.appyx.multiplatform.core.plugin.Plugin
import com.bumble.appyx.multiplatform.core.plugin.SavesInstanceState
import com.bumble.appyx.multiplatform.core.plugin.UpNavigationHandler
import com.bumble.appyx.multiplatform.core.plugin.plugins
import com.bumble.appyx.multiplatform.core.state.MutableSavedStateMap
import com.bumble.appyx.multiplatform.core.state.MutableSavedStateMapImpl
import com.bumble.appyx.multiplatform.core.state.SavedStateMap
import com.bumble.appyx.multiplatform.interfaces.DefaultLifecycleObserver
import com.bumble.appyx.multiplatform.interfaces.Lifecycle
import com.bumble.appyx.multiplatform.interfaces.LifecycleOwner
import com.bumble.appyx.multiplatform.interfaces.MultiplatformDeps
import kotlinx.coroutines.withContext
import java.util.*
import kotlin.coroutines.CoroutineContext

@Suppress("TooManyFunctions")
@Stable
open class Node(
    private val multiplatformDeps: MultiplatformDeps,
    buildContext: BuildContext,
    val view: NodeView = EmptyNodeView,
    plugins: List<Plugin> = emptyList()
) : NodeLifecycle, NodeView by view, RequestCodeClient {

    val id = getNodeId(buildContext)

    override val requestCodeClientId: String = id

    override val lifecycle get() = nodeLifecycle.lifecycle

    @Suppress("LeakingThis") // Implemented in the same way as in androidx.Fragment
    private val nodeLifecycle = NodeLifecycleImpl(multiplatformDeps.lifecycleRegistryProvider(this))

    val plugins: List<Plugin> = plugins + listOfNotNull(this as? Plugin)

    val ancestryInfo: AncestryInfo =
        buildContext.ancestryInfo

    val isRoot: Boolean =
        ancestryInfo == AncestryInfo.Root

    val parent: ParentNode<*>? =
        when (ancestryInfo) {
            is AncestryInfo.Child -> ancestryInfo.anchor
            is AncestryInfo.Root -> null
        }

    var integrationPoint: IntegrationPoint = IntegrationPointStub()
        get() {
            return if (isRoot) field
            else parent?.integrationPoint ?: error(
                "Non-root Node should have a parent"
            )
        }
        set(value) {
            check(isRoot) { "Only a root Node can have an integration point" }
            field = value
        }

    private var wasBuilt = false

    init {
        lifecycle.addObserver(object : DefaultLifecycleObserver {
            override fun onCreate(owner: LifecycleOwner) {
                if (!wasBuilt) error("onBuilt was not invoked for $this")
            }
        })
    }

    private fun getNodeId(buildContext: BuildContext): String {
        val state = buildContext.savedStateMap ?: return UUID.randomUUID().toString()

        return state[NODE_ID_KEY] as String? ?: error(
            "super.onSaveInstanceState() was not called for the node: ${this::class.qualifiedName}"
        )
    }

    protected suspend inline fun <reified T : Node> executeAction(
        crossinline action: () -> Unit
    ): T = withContext(getScopedCoroutineContext()) {
        action()
        this@Node as T
    }

    open fun onBuilt() {
        require(!wasBuilt) { "onBuilt was already invoked" }
        wasBuilt = true
        updateLifecycleState(Lifecycle.State.CREATED)
        plugins<NodeReadyObserver<Node>>().forEach { it.init(this) }
        plugins<NodeLifecycleAware>().forEach { it.onCreate(lifecycle) }
    }

    @Composable
    fun Compose(modifier: Modifier = Modifier) {
        CompositionLocalProvider(
            LocalNode provides this,
//            LocalLifecycleOwner provides this, TODO(): what replaces this?
        ) {
            HandleBackPress()
            View(modifier)
        }
    }

    @Composable
    private fun HandleBackPress() {
        // can't use BackHandler Composable because plugins provide OnBackPressedCallback which is not observable
        // mimic the behaviour of BackHandler Composable here instead
        val backPressHandlerPlugins = remember {
            // reversed order because we want direct order, but onBackPressedDispatcher invokes them in reversed order
            plugins.filterIsInstance<BackPressHandler>().reversed()
        }
        val dispatcher = multiplatformDeps.onBackPressedDispatcherProvider() ?: return
        val lifecycleOwner = multiplatformDeps.localLifecycleOwnerProvider()
        DisposableEffect(lifecycleOwner, dispatcher) {
            backPressHandlerPlugins.forEach { plugin ->
                if (!plugin.isCorrect()) {
                    com.bumble.appyx.Appyx.reportException(
                        IllegalStateException(
                            "Plugin $plugin has implementation for both BackPressHandler properties, implement only one"
                        )
                    )
                }
                plugin.onBackPressedCallbackList.forEach { callback ->
                    dispatcher.addCallback(lifecycleOwner, callback)
                }
            }
            onDispose {
                backPressHandlerPlugins.forEach { plugin ->
                    plugin.onBackPressedCallbackList.forEach { it.remove() }
                }
            }
        }
    }

    fun getScopedCoroutineContext(): CoroutineContext =
        lifecycle.getScopedCoroutineContext()

    override fun updateLifecycleState(state: Lifecycle.State) {
        if (lifecycle.currentState == state) return
        if (lifecycle.currentState == Lifecycle.State.DESTROYED && state != Lifecycle.State.DESTROYED) {
            com.bumble.appyx.Appyx.reportException(
                IllegalStateException(
                    "Trying to change lifecycle state of already destroyed node ${this::class.qualifiedName}"
                )
            )
            return
        }
        nodeLifecycle.updateLifecycleState(state)
        if (state == Lifecycle.State.DESTROYED) {
            plugins<Destroyable>().forEach { it.destroy() }
        }
    }

    fun saveInstanceState(scope: SaverScope): SavedStateMap {
        val writer = MutableSavedStateMapImpl(saverScope = scope)
        onSaveInstanceState(writer)
        plugins
            .filterIsInstance<SavesInstanceState>()
            .forEach { it.saveInstanceState(writer) }
        return writer.savedState
    }

    protected open fun onSaveInstanceState(state: MutableSavedStateMap) {
        state[NODE_ID_KEY] = id
    }

    fun finish() {
        parent?.onChildFinished(this) ?: integrationPoint.onRootFinished()
    }

    /**
     * Triggers parents up navigation (back navigation by default).
     *
     * This method is useful for different cases like:
     * - Close button on the screen which leads back to the previous screen.
     * - Blocker screen that intercepts back button clicks but closes itself when condition is met.
     *
     * To properly handle blocker case this method skips the current node plugins (like router),
     * and invokes the parent directly.
     */
    fun navigateUp() {
        require(parent != null || isRoot) {
            "Can't navigate up, neither parent nor integration point is presented"
        }
        if (parent?.performUpNavigation() != true) {
            integrationPoint.handleUpNavigation()
        }
    }

    protected open fun performUpNavigation(): Boolean =
        handleUpNavigationByPlugins() || parent?.performUpNavigation() == true

    private fun handleUpNavigationByPlugins(): Boolean =
        plugins<UpNavigationHandler>().any { it.handleUpNavigation() }

    companion object {
        private const val NODE_ID_KEY = "node.id"

        // BackPressHandler is correct when only one of its properties is implemented.
        private fun BackPressHandler.isCorrect(): Boolean {
            val listIsOverriddenOrPluginIgnored = onBackPressedCallback == null
            val onlySingleCallbackIsOverridden = onBackPressedCallback != null &&
                    onBackPressedCallbackList.size == 1 &&
                    onBackPressedCallbackList[0] == onBackPressedCallback
            return onlySingleCallbackIsOverridden || listIsOverriddenOrPluginIgnored
        }

    }
}
