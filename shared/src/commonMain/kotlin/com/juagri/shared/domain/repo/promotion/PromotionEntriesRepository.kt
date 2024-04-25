package com.juagri.shared.domain.repo.promotion

import com.juagri.shared.domain.model.promotion.ParticipationEntry
import com.juagri.shared.domain.model.promotion.PromotionEventItem
import com.juagri.shared.utils.ResponseState
import kotlinx.coroutines.flow.Flow

interface PromotionEntriesRepository {

    fun getRecentPromotionEntries(tCode: String, roleId: String): Flow<ResponseState<List<Map<String, String>>>>

    fun setPromotionParticipation(entry: ParticipationEntry, updatedDetails: Map<String,Any>, roleId: String): Flow<ResponseState<String>>
}