package com.juagri.shared.domain.repo.doctor

import com.juagri.shared.domain.model.doctor.JUDoctorDataItem
import com.juagri.shared.utils.ResponseState
import kotlinx.coroutines.flow.Flow

interface JUDoctorRepository {
    suspend fun getJUDoctorItems(parentId: String): Flow<ResponseState<List<JUDoctorDataItem>>>
}