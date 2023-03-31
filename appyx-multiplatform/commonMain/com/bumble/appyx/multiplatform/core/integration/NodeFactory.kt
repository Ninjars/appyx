package com.bumble.appyx.multiplatform.core.integration

import androidx.compose.runtime.Stable
import com.bumble.appyx.multiplatform.core.modality.BuildContext
import com.bumble.appyx.multiplatform.core.node.Node

@Stable
fun interface NodeFactory<N : Node> {
    fun create(buildContext: BuildContext): N
}
