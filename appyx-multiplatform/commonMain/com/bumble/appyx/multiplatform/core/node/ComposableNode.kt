package com.bumble.appyx.multiplatform.core.node

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.bumble.appyx.multiplatform.core.modality.BuildContext

open class ComposableNode(
    buildContext: BuildContext,
    private val composable: @Composable (Modifier) -> Unit
) : Node(
    buildContext = buildContext,
) {

    @Composable
    override fun View(modifier: Modifier) {
        composable(modifier)
    }
}

fun node(buildContext: BuildContext, composable: @Composable (Modifier) -> Unit): Node =
    ComposableNode(buildContext, composable)
