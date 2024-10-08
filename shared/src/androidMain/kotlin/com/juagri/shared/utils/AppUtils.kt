package com.juagri.shared.utils

import android.os.Build
import android.os.Process


actual object AppUtils{
    actual fun logout(){
        Process.killProcess(Process.myPid())
        //exitProcess(10)
    }

    actual fun getDeviceInfo(): Map<String,String> {
        return mapOf(
            "app_version" to com.juagri.shared.BuildConfig.CDOVersion.toString(),
            "device_os" to Build.VERSION.CODENAME.toString(),
            "device_sdk" to Build.VERSION.SDK_INT.toString(),
            "device_mode" to "Android",
        )
    }

    actual fun getAppVersion():Int{
        return com.juagri.shared.BuildConfig.CDOVersion
    }
}