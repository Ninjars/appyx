package com.bumble.appyx.multiplatform.interfaces

import kotlinx.coroutines.CoroutineScope

interface LifecycleOwner {
    val lifecycle: Lifecycle
    val lifecycleScope: CoroutineScope
}

interface LocalLifecycleOwnerProvider : () -> LifecycleOwner
