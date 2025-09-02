package com.juagri.shared.utils

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import com.google.firebase.storage.storageMetadata
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileInputStream

class PendingUploadsWorker(
    appContext: Context,
    params: WorkerParameters
) : CoroutineWorker(appContext, params) {

    private val storage = Firebase.storage

    override suspend fun doWork(): Result {
        println("PendingUploadsWorker starts")
        val files = PendingCacheFiles.list()
        if (files.isEmpty()) return Result.success()

        for (file in files) {
            try {
                println("PendingUploadsWorker file: ${file.name}")
                val filePath = "${Constants.FOLDER_PROMOTION_ENTRY_IMAGES}/${file.name}"
                //val ref = storage.getReference(filePath).putBytes(file.readBytes())
                val ref = storage.getReference(filePath)
                val metadata = storageMetadata {
                    contentType = guessMime(file) // e.g., "image/jpeg"
                }

                // Stream upload (memory-friendly)
                withContext(Dispatchers.IO) {
                    FileInputStream(file).use { stream ->
                        ref.putStream(stream, metadata).await()
                    }
                }

                // (Optional) get download URL
                val url = ref.downloadUrl.await()

                // Success → remove
                file.delete()
            } catch (t: Throwable) {
                // Leave failed file; let WorkManager retry
                return Result.retry()
            }
        }
        println("PendingUploadsWorker ends")
        return Result.success()
    }

    private fun guessMime(file: File): String = when {
        file.name.endsWith(".jpg", true) || file.name.endsWith(".jpeg", true) -> "image/jpeg"
        file.name.endsWith(".png", true) -> "image/png"
        else -> "application/octet-stream"
    }
}