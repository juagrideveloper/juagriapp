package com.juagri.shared.domain.usecase

import com.juagri.shared.domain.model.employee.JUEmployee
import com.juagri.shared.domain.repo.ledger.OsConfirmRepository

class OsConfirmUseCase(private val repository: OsConfirmRepository) {
    suspend fun getOsConfirmConfig() = repository.getOsConfirmConfig()
    suspend fun getOsConfirmCustomer(ccode: String) = repository.getOsConfirmCustomer(ccode)
    suspend fun updateOsConfirmStatus(ccode: String, status: Int, employee: JUEmployee) =
        repository.updateOsConfirmStatus(ccode, status, employee)
}
