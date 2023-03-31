package com.bumble.appyx.core.navigation

import com.bumble.appyx.core.modality.BuildContext
import com.bumble.appyx.core.node.Node

interface Resolver<NavTarget> {
    fun resolve(navTarget: NavTarget, buildContext: BuildContext): Node
}
