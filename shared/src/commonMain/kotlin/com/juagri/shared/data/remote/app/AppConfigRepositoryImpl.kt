package com.juagri.shared.data.remote.app

import Constants
import com.juagri.shared.domain.model.app.AppConfig
import com.juagri.shared.domain.repo.app.AppConfigRepository
import com.juagri.shared.utils.ResponseState
import dev.gitlive.firebase.firestore.CollectionReference
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

class AppConfigRepositoryImpl(
    private val db: CollectionReference
) : AppConfigRepository {
    override suspend fun getAppConfig(): Flow<ResponseState<AppConfig>> = callbackFlow {
        try {
            trySend(ResponseState.Loading(true))
            val result = db.document(Constants.TABLE_APP).get().data<AppConfig>()
            trySend(ResponseState.Loading())
            trySend(ResponseState.Success(result))
        } catch (e: Exception) {
            e.printStackTrace()
            trySend(ResponseState.Error())
        }
        awaitClose {
            channel.close()
        }
    }
}