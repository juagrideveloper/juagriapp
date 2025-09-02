package com.juagri.jucdo

import android.app.Application
import com.juagri.shared.utils.PendingCacheFiles

class JUApp: Application() {

    override fun onCreate() {
        super.onCreate()
        PendingCacheFiles.apply {
            initContext(this@JUApp)
            uploadFiles()
        }
    }
}