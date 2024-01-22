package com.juagri.shared.com.juagri.shared.data.remote.login

import com.juagri.shared.com.juagri.shared.domain.repo.OTPRepository
import com.juagri.shared.data.remote.ApiConfig
import com.juagri.shared.domain.model.employee.JUEmployee
import com.juagri.shared.domain.model.login.OTPResponse
import com.juagri.shared.utils.ResponseState
import dev.gitlive.firebase.firestore.CollectionReference
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

class OTPRepositoryImpl(private val client: HttpClient):
    OTPRepository {
    override suspend fun sendOTP(employee: JUEmployee): Flow<ResponseState<OTPResponse>> = callbackFlow {
        trySend(ResponseState.Loading(true))
        val response = client.get(ApiConfig.API_URL +employee.mobile).body<OTPResponse>()
        delay(2000)
        trySend(ResponseState.Loading())
        trySend(ResponseState.Success(response))
        awaitClose {
            channel.close()
        }
    }
}