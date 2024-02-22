package com.juagri.shared.domain.repo.login

import com.juagri.shared.utils.ResponseState
import kotlinx.coroutines.flow.Flow

interface FirebaseAuthRepository {
    suspend fun auth(): Flow<ResponseState<String>>
}