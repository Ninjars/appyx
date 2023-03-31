package com.bumble.appyx.multiplatform.core.clienthelper.interactor

import com.bumble.appyx.multiplatform.core.children.ChildAware
import com.bumble.appyx.multiplatform.core.children.ChildAwareImpl
import com.bumble.appyx.multiplatform.core.node.Node
import com.bumble.appyx.multiplatform.core.plugin.NodeAware
import com.bumble.appyx.multiplatform.core.plugin.NodeLifecycleAware
import com.bumble.appyx.multiplatform.core.plugin.SavesInstanceState

open class Interactor<N : Node>(
    private val childAwareImpl: ChildAware<N> = ChildAwareImpl()
) : NodeAware<N>,
    NodeLifecycleAware,
    ChildAware<N> by childAwareImpl,
    SavesInstanceState
