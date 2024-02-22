package com.juagri.shared.data.remote.login

import com.juagri.shared.domain.repo.login.FirebaseAuthRepository
import com.juagri.shared.utils.ResponseState
import kotlinx.coroutines.flow.Flow

class FirebaseAuthRepositoryImpl(/*private val auth: FirebaseAuth*/): FirebaseAuthRepository {
    override suspend fun auth(): Flow<ResponseState<String>> {
        TODO("Not yet implemented")
    }
}