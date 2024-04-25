package com.juagri.shared.domain.repo.focusProduct

import com.juagri.shared.domain.model.doctor.JUDoctorDataItem
import com.juagri.shared.domain.model.focusProduct.CDOFocusProductItem
import com.juagri.shared.utils.ResponseState
import kotlinx.coroutines.flow.Flow

interface CDOFocusProductRepository {
    suspend fun getCDOFocusProductItems(cdoId: String): Flow<ResponseState<CDOFocusProductItem>>
}