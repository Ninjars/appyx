package com.bumble.appyx.multiplatform.core.navigation.transition

import androidx.compose.animation.core.Transition
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import com.bumble.appyx.multiplatform.core.collections.ImmutableList

class CombinedHandler<T, S>(
    private val handlers: List<ModifierTransitionHandler<T, S>>
) : ModifierTransitionHandler<T, S>() {

    override fun createModifier(
        modifier: Modifier,
        transition: Transition<S>,
        descriptor: TransitionDescriptor<T, S>
    ): Modifier =
        handlers
            .map { it.createModifier(Modifier, transition, descriptor = descriptor) }
            .fold(modifier) { acc: Modifier, currentModifier: Modifier ->
                acc.then(currentModifier)
            }
}

@Suppress("UnstableCollections")
@Deprecated(
    message = "Use rememberCombinedHandler with immutable handlers instead. This function will be removed in 1.1",
    replaceWith = ReplaceWith(
        "rememberCombinedHandler<T, S>(handlers.toImmutableList<T, S>())",
        "kotlin.com.bumble.appyx.multiplatform.core.collections.toImmutableList",
    )
)
@Composable
fun <T, S> rememberCombinedHandler(handlers: List<ModifierTransitionHandler<T, S>>): ModifierTransitionHandler<T, S> =
    remember { CombinedHandler(handlers = handlers) }

@Composable
fun <T, S> rememberCombinedHandler(
    handlers: ImmutableList<ModifierTransitionHandler<T, S>>
): ModifierTransitionHandler<T, S> =
    remember(handlers) { CombinedHandler(handlers = handlers) }
