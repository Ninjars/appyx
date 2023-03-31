package com.bumble.appyx.core.clienthelper.interactor

import com.bumble.appyx.core.children.ChildAware
import com.bumble.appyx.core.children.ChildAwareImpl
import com.bumble.appyx.core.node.Node
import kotlin.com.bumble.appyx.multiplatform.core.plugin.NodeAware
import kotlin.com.bumble.appyx.multiplatform.core.plugin.NodeLifecycleAware
import kotlin.com.bumble.appyx.multiplatform.core.plugin.SavesInstanceState

open class Interactor<N : Node>(
    private val childAwareImpl: ChildAware<N> = ChildAwareImpl()
) : NodeAware<N>,
    NodeLifecycleAware,
    ChildAware<N> by childAwareImpl,
    SavesInstanceState
