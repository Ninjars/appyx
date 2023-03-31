package com.bumble.appyx.multiplatform.interfaces

interface LifecycleObserver {
    fun onCreate(owner: LifecycleOwner)
    fun onStart(owner: LifecycleOwner)
    fun onResume(owner: LifecycleOwner)
    fun onPause(owner: LifecycleOwner)
    fun onStop(owner: LifecycleOwner)
    fun onDestroy(owner: LifecycleOwner)
}

interface DefaultLifecycleObserver : LifecycleObserver {
    override fun onCreate(owner: LifecycleOwner) {}
    override fun onStart(owner: LifecycleOwner) {}
    override fun onResume(owner: LifecycleOwner) {}
    override fun onPause(owner: LifecycleOwner) {}
    override fun onStop(owner: LifecycleOwner) {}
    override fun onDestroy(owner: LifecycleOwner) {}
}