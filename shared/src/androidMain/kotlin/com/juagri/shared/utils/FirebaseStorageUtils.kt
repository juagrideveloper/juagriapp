package com.juagri.shared.utils

import com.google.firebase.Firebase
import com.google.firebase.storage.storage
import android.graphics.BitmapFactory
import android.graphics.Bitmap
import java.io.ByteArrayOutputStream
import kotlin.math.max

actual fun uploadImages(images: List<ByteArray>, filenames: List<String>, result:()->Unit){
    var successCount = 0
    images.forEachIndexed { index, bytes ->
        PendingCacheFiles.save(bytes, filenames[index])
//        val filePath = "${Constants.FOLDER_PROMOTION_ENTRY_IMAGES}/${filenames[index]}"
//        Firebase.storage.getReference(filePath).putBytes(bytes).addOnCompleteListener{
//            println("Upload File Path: $filePath")
//            if(it.isSuccessful){
//                println("Upload File Success")
//                //println("Upload File Url ${it.result.uploadSessionUri}")
//                successCount++
//            }
//            if(successCount == images.size){
//                result.invoke()
//            }
//        }
    }
    PendingCacheFiles.uploadFiles()
    result()
}

actual fun uploadFiles(path: String, files: List<ByteArray>, filenames: List<String>, result:()->Unit){
    var successCount = 0
    files.forEachIndexed { index, bytes ->
        Firebase.storage.getReference("${path}/${filenames[index]}").putBytes(bytes).addOnCompleteListener{
            if(it.isSuccessful){
                successCount++
            }
            if(successCount == files.size){
                result.invoke()
            }
        }
    }
}

// commonMain
actual fun reduceImage(
    data: ByteArray,
    maxWidth: Int,
    maxHeight: Int,
    quality: Float // 0f..1f
): ByteArray{
// First decode bounds
    val opts = BitmapFactory.Options().apply { inJustDecodeBounds = true }
    BitmapFactory.decodeByteArray(data, 0, data.size, opts)

    // Compute inSampleSize
    fun sampleSize(): Int {
        var sample = 1
        while (opts.outWidth / sample > maxWidth || opts.outHeight / sample > maxHeight) {
            sample *= 2
        }
        return max(1, sample)
    }

    val decodeOpts = BitmapFactory.Options().apply { inSampleSize = sampleSize() }
    val bmp = BitmapFactory.decodeByteArray(data, 0, data.size, decodeOpts) ?: return data

    // Final scale (keeps aspect)
    val ratio = minOf(maxWidth.toFloat() / bmp.width, maxHeight.toFloat() / bmp.height, 1f)
    val scaled = if (ratio < 1f) {
        Bitmap.createScaledBitmap(
            bmp,
            (bmp.width * ratio).toInt(),
            (bmp.height * ratio).toInt(),
            true
        )
    } else bmp

    return ByteArrayOutputStream().use { out ->
        scaled.compress(Bitmap.CompressFormat.JPEG, (quality * 100).toInt(), out)
        out.toByteArray()
    }
}