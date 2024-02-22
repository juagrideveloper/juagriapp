package com.juagri.shared.data.remote.login

import com.juagri.shared.domain.model.employee.JUEmployee
import com.juagri.shared.domain.repo.login.EmployeeRepository
import com.juagri.shared.utils.ResponseState
import dev.gitlive.firebase.firestore.CollectionReference
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

class EmployeeRepositoryImpl(private val db:CollectionReference,private val menuDB:CollectionReference):
    EmployeeRepository {

    override suspend fun getEmployeeDetails(
        mobileNo: String,
        withMenu: Boolean
    ): Flow<ResponseState<JUEmployee>> = callbackFlow {
        trySend(ResponseState.Loading(true))
        try {
            val result = db.document(mobileNo).get().reference
            val employee = result.get().data() as JUEmployee
            employee.menuId?.let {
                employee.menuItems = menuDB.document(it).get().reference.get().data()
            }
            trySend(ResponseState.Loading())
            trySend(ResponseState.Success(employee))
        } catch (e: Exception) {
            trySend(ResponseState.Error(e))
        }
        awaitClose {
            channel.close()
        }
    }
}