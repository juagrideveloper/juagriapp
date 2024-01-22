package com.juagri.shared.data.remote.login

import com.juagri.shared.com.juagri.shared.domain.repo.EmployeeRepository
import com.juagri.shared.domain.model.employee.JUEmployee
import com.juagri.shared.utils.ResponseState
import dev.gitlive.firebase.firestore.CollectionReference
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

class EmployeeRepositoryImpl(private val db:CollectionReference):
    EmployeeRepository {

    override suspend fun getEmployeeDetails(mobileNo: String): Flow<ResponseState<JUEmployee>> = callbackFlow {
        trySend(ResponseState.Loading(true))
        val result = db.document(mobileNo).get().reference
        delay(2000)
        trySend(ResponseState.Loading())
        trySend(ResponseState.Success(result.get().data()))
        awaitClose {
            channel.close()
        }
    }
}