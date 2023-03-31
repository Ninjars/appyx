package com.bumble.appyx.multiplatform.core.plugin

import com.bumble.appyx.multiplatform.core.node.Node


class NodeAwareImpl<N : Node> : NodeAware<N> {
    override lateinit var node: N
        private set

    override fun init(node: N) {
        this.node = node
    }
}
