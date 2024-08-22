package com.juagri.shared.utils

expect object AppUtils{
    fun logout()

    fun getDeviceInfo(): Map<String,String>

    fun getAppVersion(): Int
}