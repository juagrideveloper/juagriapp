package com.juagri.shared.domain.repo.promotion

import com.juagri.shared.domain.model.employee.JUEmployee
import com.juagri.shared.domain.model.promotion.DistrictItem
import com.juagri.shared.domain.model.promotion.PromotionDashboard
import com.juagri.shared.domain.model.promotion.PromotionEventItem
import com.juagri.shared.domain.model.promotion.PromotionField
import com.juagri.shared.domain.model.promotion.VillageItem
import com.juagri.shared.utils.ResponseState
import kotlinx.coroutines.flow.Flow

interface PromotionRepository {

    suspend fun getPromotionEventList(): Flow<ResponseState<List<PromotionEventItem>>>
    suspend fun getPromotionEventFieldsList(actId: String): Flow<ResponseState<List<PromotionField>>>
    suspend fun getDistrictList(stCode: String): Flow<ResponseState<List<DistrictItem>>>
    suspend fun getVillageList(districtId: String): Flow<ResponseState<List<VillageItem>>>
    suspend fun setPromotionEntry(entryItems: MutableMap<String,Any>, files: List<ByteArray>): Flow<ResponseState<Boolean>>
    suspend fun getDashboard(employee: JUEmployee): Flow<ResponseState<List<PromotionDashboard>>>
}