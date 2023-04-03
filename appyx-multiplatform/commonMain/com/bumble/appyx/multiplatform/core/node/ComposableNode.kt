package com.bumble.appyx.multiplatform.core.node

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.bumble.appyx.multiplatform.core.modality.BuildContext
import com.bumble.appyx.multiplatform.interfaces.PlatformDeps

open class ComposableNode(
    platformDeps: PlatformDeps,
    buildContext: BuildContext,
    private val composable: @Composable (Modifier) -> Unit
) : Node(
    platformDeps = platformDeps,
    buildContext = buildContext,
) {

    @Composable
    override fun View(modifier: Modifier) {
        composable(modifier)
    }
}

fun node(
    platformDeps: PlatformDeps,
    buildContext: BuildContext,
    composable: @Composable (Modifier) -> Unit
): Node =
    ComposableNode(platformDeps, buildContext, composable)
