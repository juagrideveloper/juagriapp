package com.juagri.shared.data.remote.login

import dev.gitlive.firebase.firestore.CollectionReference
import com.juagri.shared.domain.model.employee.JUEmployee
import com.juagri.shared.domain.usecase.LoginRepository
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import com.juagri.shared.utils.ResponseState

class LoginRepositoryImpl(private val db:CollectionReference): LoginRepository {
    override suspend fun getEmployeeDetails(mobileNo: String): Flow<ResponseState<JUEmployee>> = callbackFlow {
        trySend(ResponseState.Loading(true))
        val result = db.document(mobileNo).get().reference
        //println("12312312312: "+ result.get().data<JUEmployee>())
        trySend(ResponseState.Loading())
        trySend(ResponseState.Success(result.get().data()))
        awaitClose {
            channel.close()
        }
    }
}