package com.juagri.shared.domain.repo.starclub

import com.juagri.shared.domain.model.starclub.StarClubCustomer
import kotlinx.coroutines.flow.Flow

interface StarClubRepository {
    fun getStarClubCustomer(ccode: String): Flow<StarClubCustomer?>
}
