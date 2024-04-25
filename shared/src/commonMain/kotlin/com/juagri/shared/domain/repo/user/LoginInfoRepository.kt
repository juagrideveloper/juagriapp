package com.juagri.shared.domain.repo.user

import com.juagri.shared.domain.model.employee.JUEmployee
import com.juagri.shared.domain.model.user.LoginInfo
import com.juagri.shared.utils.ResponseState
import kotlinx.coroutines.flow.Flow

interface LoginInfoRepository {

    suspend fun getLoginInfoDetails(employee: JUEmployee): Flow<ResponseState<List<LoginInfo>>>
}