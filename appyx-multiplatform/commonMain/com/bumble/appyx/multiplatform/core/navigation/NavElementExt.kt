package com.bumble.appyx.multiplatform.core.navigation

val NavElement<*, *>.isTransitioning: Boolean
    get() = fromState != targetState
