package com.bumble.appyx.multiplatform.core.navigation

import com.bumble.appyx.multiplatform.core.modality.BuildContext
import com.bumble.appyx.multiplatform.core.node.Node

interface Resolver<NavTarget> {
    fun resolve(navTarget: NavTarget, buildContext: BuildContext): Node
}
