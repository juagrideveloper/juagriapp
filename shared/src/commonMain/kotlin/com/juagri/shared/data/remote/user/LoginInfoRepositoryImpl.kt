package com.juagri.shared.data.remote.user

import com.juagri.shared.utils.Constants
import com.juagri.shared.domain.model.employee.JUEmployee
import com.juagri.shared.domain.model.user.LoginInfo
import com.juagri.shared.domain.repo.user.LoginInfoRepository
import com.juagri.shared.utils.ResponseState
import com.juagri.shared.utils.value
import dev.gitlive.firebase.firestore.CollectionReference
import dev.gitlive.firebase.firestore.orderBy
import dev.gitlive.firebase.firestore.where
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

class LoginInfoRepositoryImpl(
    private val loginInfoDB: CollectionReference,
    private val empAccessDB: CollectionReference
): LoginInfoRepository {
    override suspend fun getLoginInfoDetails(employee: JUEmployee): Flow<ResponseState<List<LoginInfo>>>  = callbackFlow {
        trySend(ResponseState.Loading(true))
        val response = when(employee.roleId.value()){
            Constants.EMP_ROLE_DM, Constants.EMP_ROLE_RM -> {
                loginInfoDB
                    .where { Constants.FIELD_REG_CODE inArray employee.regionCode.value().split(",") }
                    .where { Constants.FIELD_ROLE_ID inArray arrayListOf(Constants.EMP_ROLE_CDO, Constants.EMP_ROLE_SO) }
                    .orderBy(Constants.FIELD_EMP_Name).get().documents
            }
            Constants.EMP_ROLE_SO-> {
                loginInfoDB
                    .where {Constants.FIELD_T_CODE inArray employee.territoryCode.value().split(",") }
                    .where { Constants.FIELD_ROLE_ID equalTo "CDO" }
                    .orderBy(Constants.FIELD_EMP_Name).get().documents
            }
            else -> listOf()
        }
        val deActivatedUsers = empAccessDB
            .where { Constants.FIELD_REG_CODE inArray employee.regionCode.value().split(",") }
            .where { Constants.FIELD_ACTIVE equalTo false }
            .get().documents.map {
               val item =  (it.data() as JUEmployee)
                println("DeActivated : " + item.name)
                item.code.value()
            }
        val result = response.map { it.data() as LoginInfo }.filter {
            println(it.empName)
            !deActivatedUsers.contains(it.empCode)
        }

        trySend(ResponseState.Loading())
        trySend(ResponseState.Success(result))
        awaitClose {
            channel.close()
        }
    }
}