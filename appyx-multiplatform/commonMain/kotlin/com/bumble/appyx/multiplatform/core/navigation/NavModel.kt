package com.bumble.appyx.core.navigation

import androidx.compose.runtime.Stable
import kotlinx.coroutines.flow.StateFlow
import kotlin.com.bumble.appyx.multiplatform.core.plugin.BackPressHandler
import kotlin.com.bumble.appyx.multiplatform.core.plugin.SavesInstanceState
import kotlin.com.bumble.appyx.multiplatform.core.plugin.UpNavigationHandler

@Stable
interface NavModel<NavTarget, State> : NavModelAdapter<NavTarget, State>,
    UpNavigationHandler,
    SavesInstanceState,
    BackPressHandler {

    val elements: StateFlow<NavElements<NavTarget, out State>>

    fun onTransitionFinished(key: NavKey<NavTarget>) {
        onTransitionFinished(listOf(key))
    }

    fun onTransitionFinished(keys: Collection<NavKey<NavTarget>>)

    fun accept(operation: Operation<NavTarget, State>) = Unit

    override fun handleUpNavigation(): Boolean =
        handleOnBackPressed()

}
