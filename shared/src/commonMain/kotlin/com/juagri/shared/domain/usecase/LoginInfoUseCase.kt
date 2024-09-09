package com.juagri.shared.domain.usecase

import com.juagri.shared.domain.model.employee.JUEmployee
import com.juagri.shared.domain.repo.user.LoginInfoRepository

class LoginInfoUseCase(
    private val repository: LoginInfoRepository
) {

    suspend fun getLoginInfoDetails(employee: JUEmployee) = repository.getLoginInfoDetails(employee)
}