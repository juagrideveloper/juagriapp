package com.juagri.shared.domain.usecase

import com.juagri.shared.domain.repo.ledger.OsConfirmRepository

class OsConfirmUseCase(private val repository: OsConfirmRepository) {
    suspend fun getOsConfirmConfig() = repository.getOsConfirmConfig()
    suspend fun getOsConfirmCustomer(ccode: String) = repository.getOsConfirmCustomer(ccode)
}
