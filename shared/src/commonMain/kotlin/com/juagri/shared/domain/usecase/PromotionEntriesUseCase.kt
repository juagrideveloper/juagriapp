package com.juagri.shared.domain.usecase

import com.juagri.shared.domain.model.promotion.ParticipationEntry
import com.juagri.shared.domain.repo.promotion.PromotionEntriesRepository

class PromotionEntriesUseCase(
    private val repository: PromotionEntriesRepository
) {
    fun getRecentPromotionEntries(tCode: String, roleId: String) = repository.getRecentPromotionEntries(tCode, roleId)
    fun setPromotionParticipation(entry: ParticipationEntry, updatedDetails: Map<String, Any>,roleId: String) =
        repository.setPromotionParticipation(entry, updatedDetails, roleId)
}