package com.bumble.appyx.multiplatform.core.children

import com.bumble.appyx.multiplatform.core.node.Node

val <T> ChildEntry<T>.nodeOrNull: Node?
    get() =
        when (this) {
            is ChildEntry.Initialized -> node
            is ChildEntry.Suspended -> null
        }
