package com.juagri.shared.domain.usecase

import com.juagri.shared.domain.repo.login.EmployeeRepository

class EmployeeUseCase(private val repository: EmployeeRepository) {
    suspend fun getEmployeeDetails(mobileNo: String, withMenu: Boolean = false) = repository.getEmployeeDetails(mobileNo, withMenu)
}