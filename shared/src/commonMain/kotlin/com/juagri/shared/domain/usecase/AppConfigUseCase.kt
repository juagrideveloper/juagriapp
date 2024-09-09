package com.juagri.shared.domain.usecase

import com.juagri.shared.domain.repo.app.AppConfigRepository

class AppConfigUseCase(private val repository: AppConfigRepository) {
    suspend fun getAppConfig() = repository.getAppConfig()
}