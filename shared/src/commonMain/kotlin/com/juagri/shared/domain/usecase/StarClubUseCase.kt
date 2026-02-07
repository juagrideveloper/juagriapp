package com.juagri.shared.domain.usecase

import com.juagri.shared.domain.model.starclub.StarClubCustomer
import com.juagri.shared.domain.repo.starclub.StarClubRepository
import kotlinx.coroutines.flow.Flow

class StarClubUseCase(private val repository: StarClubRepository) {
    fun getStarClubCustomer(ccode: String): Flow<StarClubCustomer?> =
        repository.getStarClubCustomer(ccode)
}
