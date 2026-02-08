package com.juagri.shared.domain.repo.ledger

import com.juagri.shared.domain.model.ledger.OsConfirmConfig
import com.juagri.shared.domain.model.ledger.OsConfirmCustomer
import com.juagri.shared.utils.ResponseState
import kotlinx.coroutines.flow.Flow

interface OsConfirmRepository {
    suspend fun getOsConfirmConfig(): Flow<ResponseState<OsConfirmConfig?>>
    suspend fun getOsConfirmCustomer(ccode: String): Flow<ResponseState<OsConfirmCustomer?>>
}
