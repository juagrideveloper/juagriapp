package com.juagri.shared.utils

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.gms.tasks.CancellationTokenSource

actual object PermissionUtils {
    @Composable
    actual fun CameraPermission(result: (Boolean)-> Unit){
        val context = LocalContext.current
        val permissionCheckResult =
            ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA)
        val permissionLauncher = rememberLauncherForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) {
            result.invoke(it)
        }
        if (permissionCheckResult == PackageManager.PERMISSION_GRANTED) {
            result.invoke(true)
        } else {
            SideEffect {
                // Request a permission
                permissionLauncher.launch(Manifest.permission.CAMERA)
            }
        }
    }

    @Composable
    actual fun LocationPermission(result: (Boolean)-> Unit) {
        val locationsPermissions = mutableListOf(
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
        )
        val permissionStatus: MutableList<Boolean> = mutableListOf()
        locationsPermissions.forEach {
            checkPermission(it) { status ->
                permissionStatus.add(status)
                if (permissionStatus.size == locationsPermissions.size) {
                    result.invoke(permissionStatus.none { granted -> (!granted) })
                }
            }
        }
    }

    @SuppressLint("MissingPermission")
    @Composable
    actual fun GetCurrentLocation(latLong: (Double,Double)-> Unit){
        LocationServices.getFusedLocationProviderClient(LocalContext.current)
            .getCurrentLocation(Priority.PRIORITY_BALANCED_POWER_ACCURACY, CancellationTokenSource().token)
            .addOnCompleteListener { task ->
                try {
                    if (task.isSuccessful) {
                        latLong.invoke(task.result.latitude, task.result.longitude)
                    } else {
                        latLong.invoke(0.0, 0.0)
                    }
                }catch (e: Exception){
                    e.printStackTrace()
                    latLong.invoke(0.0, 0.0)
                }
            }
    }

    @Composable
    fun checkPermission(permission: String,result: (Boolean)-> Unit){
        val context = LocalContext.current
        val permissionCheckResult =
            ContextCompat.checkSelfPermission(context, permission)
        val permissionLauncher = rememberLauncherForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) {
            result.invoke(it)
        }
        if (permissionCheckResult == PackageManager.PERMISSION_GRANTED) {
            result.invoke(true)
        } else {
            SideEffect {
                // Request a permission
                permissionLauncher.launch(permission)
            }
        }
    }
}
