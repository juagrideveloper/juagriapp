package com.juagri.shared.domain.repo.app

import com.juagri.shared.domain.model.app.AppConfig
import com.juagri.shared.utils.ResponseState
import kotlinx.coroutines.flow.Flow

interface AppConfigRepository {
    suspend fun getAppConfig(): Flow<ResponseState<AppConfig>>
}