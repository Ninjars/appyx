package com.bumble.appyx.multiplatform.core.navigation.transition

import androidx.compose.runtime.Immutable
import com.bumble.appyx.multiplatform.core.navigation.Operation

@Immutable
data class TransitionDescriptor<NavTarget, out State>(
    val params: TransitionParams,
    val operation: Operation<NavTarget, out State>,
    val element: NavTarget,
    val fromState: State,
    val toState: State
)
