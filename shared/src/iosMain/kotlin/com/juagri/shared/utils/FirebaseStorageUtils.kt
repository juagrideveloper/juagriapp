package com.juagri.shared.utils

import io.ktor.utils.io.core.toByteArray

actual fun uploadImages(images: List<ByteArray>, filenames: List<String>, result:()->Unit){

}

actual fun uploadFiles(path: String, files: List<ByteArray>, filenames: List<String>, result:()->Unit){

}

// commonMain
actual fun reduceImage(
    data: ByteArray,
    maxWidth: Int,
    maxHeight: Int,
    quality: Float // 0f..1f
): ByteArray{
    return "A".toByteArray()
}