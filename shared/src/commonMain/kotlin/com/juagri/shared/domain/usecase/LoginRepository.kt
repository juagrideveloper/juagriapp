package com.juagri.shared.domain.usecase

import com.juagri.shared.domain.model.employee.JUEmployee
import kotlinx.coroutines.flow.Flow
import com.juagri.shared.utils.ResponseState

interface LoginRepository {
    suspend fun getEmployeeDetails(mobileNo: String): Flow<ResponseState<JUEmployee>>
}