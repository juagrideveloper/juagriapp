package com.juagri.shared.domain.repo.login

import com.juagri.shared.domain.model.employee.JUEmployee
import com.juagri.shared.domain.model.login.OTPResponse
import com.juagri.shared.utils.ResponseState
import kotlinx.coroutines.flow.Flow

interface OTPRepository {
    suspend fun sendOTP(employee: JUEmployee): Flow<ResponseState<OTPResponse>>
}