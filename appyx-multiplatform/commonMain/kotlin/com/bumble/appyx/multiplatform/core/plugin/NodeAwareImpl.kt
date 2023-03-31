package com.bumble.appyx.core.plugin

import com.bumble.appyx.core.node.Node
import kotlin.com.bumble.appyx.multiplatform.core.plugin.NodeAware


class NodeAwareImpl<N : Node> : NodeAware<N> {
    override lateinit var node: N
        private set

    override fun init(node: N) {
        this.node = node
    }
}
