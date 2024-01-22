package com.juagri.shared.domain.repo

import com.juagri.shared.domain.model.employee.JUEmployee
import com.juagri.shared.domain.model.login.OTPResponse
import kotlinx.coroutines.flow.Flow
import com.juagri.shared.utils.ResponseState

interface EmployeeRepository {
    suspend fun getEmployeeDetails(mobileNo: String): Flow<ResponseState<JUEmployee>>
}