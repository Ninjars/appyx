package com.bumble.appyx.multiplatform.core.node

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.bumble.appyx.multiplatform.core.modality.BuildContext
import com.bumble.appyx.multiplatform.interfaces.MultiplatformDeps

open class ComposableNode(
    multiplatformDeps: MultiplatformDeps,
    buildContext: BuildContext,
    private val composable: @Composable (Modifier) -> Unit
) : Node(
    multiplatformDeps = multiplatformDeps,
    buildContext = buildContext,
) {

    @Composable
    override fun View(modifier: Modifier) {
        composable(modifier)
    }
}

fun node(
    multiplatformDeps: MultiplatformDeps,
    buildContext: BuildContext,
    composable: @Composable (Modifier) -> Unit
): Node =
    ComposableNode(multiplatformDeps, buildContext, composable)
