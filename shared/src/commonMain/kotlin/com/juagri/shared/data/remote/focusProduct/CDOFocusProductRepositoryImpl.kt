package com.juagri.shared.data.remote.focusProduct

import com.juagri.shared.domain.model.focusProduct.CDOFocusProductItem
import com.juagri.shared.domain.repo.focusProduct.CDOFocusProductRepository
import com.juagri.shared.utils.JUError
import com.juagri.shared.utils.ResponseState
import dev.gitlive.firebase.firestore.CollectionReference
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

class CDOFocusProductRepositoryImpl(
    private val focusProductDB: CollectionReference
): CDOFocusProductRepository {
    override suspend fun getCDOFocusProductItems(cdoId: String): Flow<ResponseState<CDOFocusProductItem>> = callbackFlow{
        trySend(ResponseState.Loading(true))
        try {
            val result = focusProductDB.document(cdoId).get().reference.get().data<CDOFocusProductItem>()
            trySend(ResponseState.Loading())
            trySend(ResponseState.Success(result))
        }catch (e:Exception){
            e.printStackTrace()
            trySend(ResponseState.Error(JUError.CustomError("No data found!")))
        }
        awaitClose {
            channel.close()
        }
    }
}