package com.juagri.shared.domain

import com.juagri.shared.domain.usecase.LoginRepository

class LoginUseCase(private val repository: LoginRepository) {
    suspend fun getEmployeeDetails(mobileNo: String) = repository.getEmployeeDetails(mobileNo)
}