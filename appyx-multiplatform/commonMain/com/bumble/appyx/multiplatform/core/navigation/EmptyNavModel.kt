package com.bumble.appyx.multiplatform.core.navigation

import com.bumble.appyx.multiplatform.core.navigation.onscreen.OnScreenStateResolver

/**
 * An implementation of a NavModel that won't add any children.
 * This is potentially useful if your ParentNode only uses
 * [com.bumble.appyx.core.navigation.model.permanent.PermanentNavModel]
 */
class EmptyNavModel<NavTarget, State> : BaseNavModel<NavTarget, State>(
    savedStateMap = null,
    finalState = null,
    screenResolver = OnScreenStateResolver { true }
) {
    override val initialElements: NavElements<NavTarget, State>
        get() = emptyList()
}
