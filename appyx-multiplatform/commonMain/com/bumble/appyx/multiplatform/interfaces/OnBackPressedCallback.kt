package com.bumble.appyx.multiplatform.interfaces

interface OnBackPressedCallback {
    val isEnabled: Boolean

    fun handleOnBackPressed()

    fun remove()
}