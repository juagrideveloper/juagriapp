package com.juagri.shared.domain.usecase

import com.juagri.shared.domain.repo.user.UserRepository
import dev.gitlive.firebase.firestore.Timestamp

class UserDetailsUseCase(private val repository: UserRepository) {
    suspend fun getRegionList(regCodes: String = "") = repository.getRegionList(regCodes)

    suspend fun getTerritoryList(regCode: String) = repository.getTerritoryList(regCode)

    suspend fun getDealerList(tCode: String) = repository.getDealerList(tCode)

    suspend fun getFinYear() = repository.getFinYear()

    suspend fun getFinMonth(startDate: Timestamp, endDate: Timestamp) = repository.getFinMonth(startDate, endDate)
}