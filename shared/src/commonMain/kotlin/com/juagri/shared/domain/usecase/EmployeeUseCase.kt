package com.juagri.shared.domain.usecase

import com.juagri.shared.domain.repo.EmployeeRepository

class EmployeeUseCase(private val repository: EmployeeRepository) {
    suspend fun getEmployeeDetails(mobileNo: String) = repository.getEmployeeDetails(mobileNo)
}