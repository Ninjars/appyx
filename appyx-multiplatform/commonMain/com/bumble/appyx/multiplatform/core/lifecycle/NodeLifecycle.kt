package com.bumble.appyx.multiplatform.core.lifecycle

import com.bumble.appyx.multiplatform.interfaces.Lifecycle
import com.bumble.appyx.multiplatform.interfaces.LifecycleOwner

interface NodeLifecycle : LifecycleOwner {

    fun updateLifecycleState(state: Lifecycle.State)

}
