package com.juagri.shared.domain.repo.login

import com.juagri.shared.domain.model.employee.JUEmployee
import com.juagri.shared.utils.ResponseState
import kotlinx.coroutines.flow.Flow

interface EmployeeRepository {
    suspend fun getEmployeeDetails(mobileNo: String, withMenu: Boolean = false): Flow<ResponseState<JUEmployee>>
}