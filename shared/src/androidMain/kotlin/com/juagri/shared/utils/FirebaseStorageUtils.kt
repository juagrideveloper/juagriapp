package com.juagri.shared.utils

import com.google.firebase.Firebase
import com.google.firebase.storage.storage

actual fun uploadImages(images: List<ByteArray>, filenames: List<String>, result:()->Unit){
    var successCount = 0
    images.forEachIndexed { index, bytes ->
        Firebase.storage.getReference("${Constants.FOLDER_PROMOTION_ENTRY_IMAGES}/${filenames[index]}").putBytes(bytes).addOnCompleteListener{
            if(it.isSuccessful){
                successCount++
            }
            if(successCount == images.size){
                result.invoke()
            }
        }
    }
}