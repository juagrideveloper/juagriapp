package com.juagri.shared.utils

import androidx.compose.runtime.Composable

expect object PermissionUtils {
    @Composable
    fun CameraPermission(result: (Boolean)-> Unit)

    @Composable
    fun LocationPermission(result: (Boolean)-> Unit)

    @Composable
    fun GetCurrentLocation(latLong: (Double,Double)-> Unit)
}
/*
val permissionCheckResult =
    ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA)
if (permissionCheckResult == PackageManager.PERMISSION_GRANTED) {
    cameraLauncher.launch(uri)
} else {
    // Request a permission
    permissionLauncher.launch(Manifest.permission.CAMERA)
}*/
