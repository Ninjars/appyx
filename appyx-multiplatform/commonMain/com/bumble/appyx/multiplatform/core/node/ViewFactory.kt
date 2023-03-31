package com.bumble.appyx.multiplatform.core.node

fun interface ViewFactory<out View : NodeView> : () -> View
