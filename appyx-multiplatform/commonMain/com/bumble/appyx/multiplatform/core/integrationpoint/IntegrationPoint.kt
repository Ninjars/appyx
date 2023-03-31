package com.bumble.appyx.multiplatform.core.integrationpoint

import android.os.Bundle
import androidx.compose.runtime.Stable
import com.bumble.appyx.multiplatform.core.integrationpoint.activitystarter.ActivityStarter
import com.bumble.appyx.multiplatform.core.integrationpoint.permissionrequester.PermissionRequester
import com.bumble.appyx.multiplatform.core.integrationpoint.requestcode.RequestCodeRegistry
import com.bumble.appyx.multiplatform.core.navigation.upnavigation.UpNavigationHandler

@Stable
abstract class IntegrationPoint(
    protected val savedInstanceState: Bundle?
) : UpNavigationHandler {

    protected val requestCodeRegistry = RequestCodeRegistry(savedInstanceState)

    abstract val activityStarter: ActivityStarter

    abstract val permissionRequester: PermissionRequester

    fun onSaveInstanceState(outState: Bundle) {
        requestCodeRegistry.onSaveInstanceState(outState)
    }

    abstract fun onRootFinished()
}
