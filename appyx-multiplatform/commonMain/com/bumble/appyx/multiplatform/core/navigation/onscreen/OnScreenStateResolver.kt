package com.bumble.appyx.multiplatform.core.navigation.onscreen

fun interface OnScreenStateResolver<State> {

    fun isOnScreen(state: State): Boolean
}
