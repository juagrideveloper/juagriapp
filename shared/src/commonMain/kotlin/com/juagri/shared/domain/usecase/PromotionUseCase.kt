package com.juagri.shared.domain.usecase

import com.juagri.shared.domain.model.employee.JUEmployee
import com.juagri.shared.domain.repo.promotion.PromotionRepository
import com.juagri.shared.domain.repo.user.UserRepository
import kotlinx.coroutines.flow.map

class PromotionUseCase(private val repository: PromotionRepository,private val userRepository: UserRepository) {
    suspend fun getPromotionEventList() = repository.getPromotionEventList()
    suspend fun getPromotionEventFieldsList(actId: String) = repository.getPromotionEventFieldsList(actId)
    suspend fun getDistrictList(stCode: String) = repository.getDistrictList(stCode)
    suspend fun getVillageList(districtId: String) = repository.getVillageList(districtId)
    suspend fun setPromotionEntry(entryItems: MutableMap<String,Any>, files: List<ByteArray>) = repository.setPromotionEntry(entryItems,files)
    suspend fun getDashboard(employee: JUEmployee) = repository.getDashboard(employee)
    suspend fun getDealerListByCDO(cdoCode: String) = userRepository.getDealerListByCDO(cdoCode)

}