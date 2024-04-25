package com.juagri.shared.utils

import kotlin.system.exitProcess

actual object AppUtils{
    actual fun logout(){
        exitProcess(0)
    }


    actual fun getDeviceInfo(): Map<String, String> {
        return mapOf()
    }
}