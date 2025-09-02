package com.juagri.shared.utils

import android.annotation.SuppressLint
import android.content.Context
import java.io.File
import java.io.FileOutputStream

@SuppressLint("StaticFieldLeak")
object PendingCacheFiles {
    private const val DIR = "pending_uploads"
    private lateinit var context: Context

    fun initContext(context: Context){
        this.context = context
    }

    private fun dir(): File = File(context.cacheDir, DIR).apply {
        if(!this.exists()) mkdirs()
    }

    fun save(bytes: ByteArray, safe: String): File {
        val file = File(dir(), safe)
        FileOutputStream(file).use { it.write(bytes) }
        return file
    }

    fun list(): List<File> = dir().listFiles()?.filter { it.isFile } ?: emptyList()

    fun uploadFiles() = UploadScheduler.enqueue(context)
}