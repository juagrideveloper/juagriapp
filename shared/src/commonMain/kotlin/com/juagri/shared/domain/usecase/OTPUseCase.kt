package com.juagri.shared.domain.usecase

import com.juagri.shared.domain.repo.OTPRepository
import com.juagri.shared.domain.model.employee.JUEmployee

class OTPUseCase(private val repository: OTPRepository) {
    suspend fun sendOTP(employee: JUEmployee) = repository.sendOTP(employee)
}