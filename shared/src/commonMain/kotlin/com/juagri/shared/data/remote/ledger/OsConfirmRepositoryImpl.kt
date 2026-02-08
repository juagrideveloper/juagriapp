package com.juagri.shared.data.remote.ledger

import com.juagri.shared.domain.model.ledger.OsConfirmConfig
import com.juagri.shared.domain.model.ledger.OsConfirmCustomer
import com.juagri.shared.domain.repo.ledger.OsConfirmRepository
import com.juagri.shared.utils.Constants
import com.juagri.shared.utils.ResponseState
import dev.gitlive.firebase.firestore.CollectionReference
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

class OsConfirmRepositoryImpl(
    private val configDB: CollectionReference,
    private val osConfirmDB: CollectionReference
) : OsConfirmRepository {

    override suspend fun getOsConfirmConfig(): Flow<ResponseState<OsConfirmConfig?>> = callbackFlow {
        trySend(ResponseState.Loading(true))
        try {
            val snap = configDB.document(Constants.TABLE_OS_CONFIRM).get()
            val config = if (snap.exists) snap.data<OsConfirmConfig>() else null
            trySend(ResponseState.Loading())
            trySend(ResponseState.Success(config))
        } catch (e: Exception) {
            e.printStackTrace()
            trySend(ResponseState.Error())
        }
        awaitClose { channel.close() }
    }

    override suspend fun getOsConfirmCustomer(
        ccode: String
    ): Flow<ResponseState<OsConfirmCustomer?>> = callbackFlow {
        trySend(ResponseState.Loading(true))
        try {
            val snap = osConfirmDB.document(ccode).get()
            val customer = if (snap.exists) snap.data<OsConfirmCustomer>() else null
            trySend(ResponseState.Loading())
            trySend(ResponseState.Success(customer))
        } catch (e: Exception) {
            e.printStackTrace()
            trySend(ResponseState.Error())
        }
        awaitClose { channel.close() }
    }
}
