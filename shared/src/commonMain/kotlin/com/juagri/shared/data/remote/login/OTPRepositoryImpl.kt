package com.juagri.shared.data.remote.login

import com.juagri.shared.data.remote.ApiConfig
import com.juagri.shared.domain.model.employee.JUEmployee
import com.juagri.shared.domain.model.login.OTPResponse
import com.juagri.shared.domain.repo.login.OTPRepository
import com.juagri.shared.utils.ResponseState
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

class OTPRepositoryImpl(private val client: HttpClient):
    OTPRepository {
    override suspend fun sendOTP(employee: JUEmployee): Flow<ResponseState<OTPResponse>> = callbackFlow {
        trySend(ResponseState.Loading(true))
        val response = client.get(ApiConfig.API_URL +employee.mobile).body<OTPResponse>()
        println(response)
        //val response = client.get(ApiConfig.API_URL +"8122422905").body<OTPResponse>()
        //val response = client.get(ApiConfig.API_URL +"9578080988").body<OTPResponse>()
        trySend(ResponseState.Loading())
        trySend(ResponseState.Success(response))
        awaitClose {
            channel.close()
        }
    }
}