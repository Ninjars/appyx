package com.bumble.appyx.multiplatform.interfaces

interface OnBackPressedDispatcher {
    fun addCallback(lifecycleOwner: LifecycleOwner, callback: OnBackPressedCallback)
}
