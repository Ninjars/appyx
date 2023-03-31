package com.bumble.appyx.core.integrationpoint.permissionrequester

interface PermissionRequestResultHandler {

    fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    )
}
