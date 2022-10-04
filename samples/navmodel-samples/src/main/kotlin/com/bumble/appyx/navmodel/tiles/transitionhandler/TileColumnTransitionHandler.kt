package com.bumble.appyx.navmodel.tiles.transitionhandler

import android.annotation.SuppressLint
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.Transition
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.spring
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import com.bumble.appyx.core.navigation.transition.ModifierTransitionHandler
import com.bumble.appyx.core.navigation.transition.TransitionDescriptor
import com.bumble.appyx.core.navigation.transition.TransitionSpec
import com.bumble.appyx.navmodel.tiles.Tiles

@Suppress("TransitionPropertiesLabel")
class TileColumnTransitionHandler<T>(
    private val maxWidth: Dp,
    private val maxHeight: Dp,
    private val transitionSpec: TransitionSpec<Tiles.State, Float> = { spring() }
) : ModifierTransitionHandler<T, Tiles.State>() {

    @SuppressLint("ModifierFactoryExtensionFunction")
    override fun createModifier(
        modifier: Modifier,
        transition: Transition<Tiles.State>,
        descriptor: TransitionDescriptor<T, Tiles.State>
    ): Modifier = modifier.composed {
        val horizontalPadding = transition.animateFloat(
            transitionSpec = transitionSpec,
            targetValueByState = {
                when (it) {
                    Tiles.State.CREATED -> 24f
                    Tiles.State.STANDARD -> 16f
                    Tiles.State.SELECTED -> 0f
                    Tiles.State.DESTROYED -> 24f
                }
            })

        val destroyProgress = transition.animateFloat(
            transitionSpec = transitionSpec,
            targetValueByState = {
                when (it) {
                    Tiles.State.DESTROYED -> 1f
                    else -> 0f
                }
            })

        val minHeightProgress = transition.animateFloat(
            transitionSpec = transitionSpec,
            targetValueByState = {
                when (it) {
                    Tiles.State.CREATED -> 0f
                    Tiles.State.STANDARD -> 0f
                    Tiles.State.SELECTED -> 1f
                    Tiles.State.DESTROYED -> 0f
                }
            })

        val maxHeightProgress = transition.animateFloat(
            transitionSpec = transitionSpec,
            targetValueByState = {
                when (it) {
                    Tiles.State.CREATED -> 1f
                    Tiles.State.STANDARD -> 0f
                    Tiles.State.SELECTED -> 1f
                    Tiles.State.DESTROYED -> 0f
                }
            })

        /**
         * Manipulating the min and max heights allow us to control the absolute sizing of the
         * element and so expand and collapse it in a way which is reflected by the view bounds
         * within the list - so we can collapse destroyed items in a way which LazyColumn doesn't
         * currently support.
         */
        if (transition.targetState == Tiles.State.SELECTED) {
            heightIn(
                min = maxHeight * minHeightProgress.value,
                max = maxHeight * maxHeightProgress.value
            )
        } else {
            heightIn(
                min = maxHeight * minHeightProgress.value,
            )
        }
            .padding(horizontal = horizontalPadding.value.dp)
            .offset {
                IntOffset(
                    x = (maxWidth * destroyProgress.value).roundToPx(),
                    y = 0,
                )
            }
            .rotate(90f * destroyProgress.value)
    }
}

@Composable
fun <T> rememberTileColumnTransitionHandler(
    maxWidth: Dp,
    maxHeight: Dp,
    transitionSpec: TransitionSpec<Tiles.State, Float> = { spring(stiffness = Spring.StiffnessVeryLow) }
): ModifierTransitionHandler<T, Tiles.State> = remember {
    TileColumnTransitionHandler(maxWidth, maxHeight, transitionSpec)
}
