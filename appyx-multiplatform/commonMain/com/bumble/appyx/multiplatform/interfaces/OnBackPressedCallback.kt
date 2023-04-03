package com.bumble.appyx.multiplatform.interfaces

abstract class OnBackPressedCallback(var isEnabled: Boolean) {

    abstract fun handleOnBackPressed()

    open fun remove() {}
}