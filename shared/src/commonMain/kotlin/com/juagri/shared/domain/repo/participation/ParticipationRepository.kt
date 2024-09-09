package com.juagri.shared.domain.repo.participation

import com.juagri.shared.domain.model.employee.JUEmployee
import com.juagri.shared.domain.model.promotion.ParticipationCounts
import com.juagri.shared.domain.model.promotion.PromotionEventItem
import com.juagri.shared.utils.ResponseState
import kotlinx.coroutines.flow.Flow

interface ParticipationRepository {
    suspend fun getParticipationDetails(employee: JUEmployee): Flow<ResponseState<List<ParticipationCounts>>>
}