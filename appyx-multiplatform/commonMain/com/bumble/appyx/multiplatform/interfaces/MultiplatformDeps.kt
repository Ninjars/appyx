package com.bumble.appyx.multiplatform.interfaces

interface MultiplatformDeps {
    val lifecycleRegistryProvider: LifecycleRegistryProvider
    val onBackPressedDispatcherProvider: OnBackPressedDispatcherProvider
    val localLifecycleOwnerProvider: LocalLifecycleOwnerProvider
}
