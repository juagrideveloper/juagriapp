package com.juagri.shared.utils

import androidx.compose.runtime.Composable

actual object PermissionUtils {
    @Composable
    actual fun CameraPermission(result: (Boolean)-> Unit){

    }

    @Composable
    actual fun LocationPermission(result: (Boolean)-> Unit){

    }

    @Composable
    actual fun GetCurrentLocation(latLong: (Double,Double)-> Unit){

    }
}
