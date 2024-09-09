package com.juagri.shared.data.remote.user

import Constants
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
    private val loginInfoDB: CollectionReference
): LoginInfoRepository {
    override suspend fun getLoginInfoDetails(employee: JUEmployee): Flow<ResponseState<List<LoginInfo>>>  = callbackFlow {
        trySend(ResponseState.Loading(true))
        val response = when(employee.menuId.value()){
            Constants.EMP_MENU_DM, Constants.EMP_MENU_RM -> {
                loginInfoDB
                    .where { Constants.FIELD_REG_CODE inArray employee.regionCode.value().split(",") }
                    .where { Constants.FIELD_ROLE_ID equalTo "CDO" }
                    .orderBy(Constants.FIELD_EMP_Name).get().documents
            }
            Constants.EMP_MENU_SO -> {
                loginInfoDB
                    .where {Constants.FIELD_T_CODE inArray employee.territoryCode.value().split(",") }
                    .where { Constants.FIELD_ROLE_ID equalTo "CDO" }
                    .orderBy(Constants.FIELD_EMP_Name).get().documents
            }
            else -> listOf()
        }
        trySend(ResponseState.Loading())
        trySend(ResponseState.Success(response.map { it.data() }))
        awaitClose {
            channel.close()
        }
    }
}