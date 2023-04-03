package com.bumble.appyx.multiplatform.interfaces

interface PlatformDeps {
    val lifecycleRegistryProvider: LifecycleRegistryProvider
    val onBackPressedDispatcherProvider: OnBackPressedDispatcherProvider
    val localLifecycleOwnerProvider: LocalLifecycleOwnerProvider
}
