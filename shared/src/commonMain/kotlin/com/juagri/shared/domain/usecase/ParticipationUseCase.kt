package com.juagri.shared.domain.usecase

import com.juagri.shared.domain.model.employee.JUEmployee
import com.juagri.shared.domain.repo.participation.ParticipationRepository

class ParticipationUseCase(
    private val repository: ParticipationRepository
) {
    suspend fun getParticipationDetails(employee: JUEmployee) = repository.getParticipationDetails(employee)
}