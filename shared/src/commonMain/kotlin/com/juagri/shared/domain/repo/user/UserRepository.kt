package com.juagri.shared.domain.repo.user

import com.juagri.shared.domain.model.user.FinMonth
import com.juagri.shared.domain.model.user.FinYear
import com.juagri.shared.domain.model.user.JUDealer
import com.juagri.shared.domain.model.user.JURegion
import com.juagri.shared.domain.model.user.JUTerritory
import com.juagri.shared.utils.ResponseState
import dev.gitlive.firebase.firestore.Timestamp
import kotlinx.coroutines.flow.Flow

interface UserRepository {
    suspend fun getRegionList(regCodes: String = ""): Flow<ResponseState<List<JURegion>>>
    suspend fun getTerritoryList(regCode: String): Flow<ResponseState<List<JUTerritory>>>

    suspend fun getDealerList(tCode: String): Flow<ResponseState<List<JUDealer>>>

    suspend fun getFinYear(): Flow<ResponseState<List<FinYear>>>

    suspend fun getFinMonth(startDate: Timestamp, endDate: Timestamp): Flow<ResponseState<List<FinMonth>>>
}