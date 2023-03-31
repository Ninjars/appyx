package com.bumble.appyx.multiplatform.core.lifecycle

import com.bumble.appyx.multiplatform.interfaces.DefaultLifecycleObserver
import com.bumble.appyx.multiplatform.interfaces.Lifecycle
import com.bumble.appyx.multiplatform.interfaces.LifecycleEventObserver
import com.bumble.appyx.multiplatform.interfaces.LifecycleOwner
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

fun LifecycleOwner.asFlow(): Flow<Lifecycle.State> =
    lifecycle.asFlow()

fun Lifecycle.asFlow(): Flow<Lifecycle.State> =
    callbackFlow {
        val observer = object : LifecycleEventObserver {
            override fun onStateChanged(source: LifecycleOwner, event: Lifecycle.Event) {
                trySend(source.lifecycle.currentState)
            }
        }
        trySend(currentState)
        addObserver(observer)
        awaitClose { removeObserver(observer) }
    }

internal val Lifecycle.isDestroyed: Boolean
    get() = currentState == Lifecycle.State.DESTROYED

fun Lifecycle.subscribe(
    onCreate: () -> Unit = {},
    onStart: () -> Unit = {},
    onResume: () -> Unit = {},
    onPause: () -> Unit = {},
    onStop: () -> Unit = {},
    onDestroy: () -> Unit = {}
) {
    addObserver(
        object : DefaultLifecycleObserver {
            override fun onCreate(owner: LifecycleOwner) {
                onCreate()
            }

            override fun onStart(owner: LifecycleOwner) {
                onStart()
            }

            override fun onResume(owner: LifecycleOwner) {
                onResume()
            }

            override fun onPause(owner: LifecycleOwner) {
                onPause()
            }

            override fun onStop(owner: LifecycleOwner) {
                onStop()
            }

            override fun onDestroy(owner: LifecycleOwner) {
                onDestroy()
            }
        }
    )
}
