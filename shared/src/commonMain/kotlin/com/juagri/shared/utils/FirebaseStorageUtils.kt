package com.juagri.shared.utils

expect fun uploadImages(images: List<ByteArray>, filenames: List<String>, result: ()->Unit)

expect fun uploadFiles(path: String, files: List<ByteArray>, filenames: List<String>, result:()->Unit)

// commonMain
expect fun reduceImage(
    data: ByteArray,
    maxWidth: Int,
    maxHeight: Int,
    quality: Float // 0f..1f
): ByteArray