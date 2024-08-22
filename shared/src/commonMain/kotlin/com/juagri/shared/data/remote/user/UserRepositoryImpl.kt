package com.juagri.shared.data.remote.user

import com.juagri.shared.data.local.dao.common.UserDetailsDao
import com.juagri.shared.domain.model.user.FinMonth
import com.juagri.shared.domain.model.user.FinYear
import com.juagri.shared.domain.model.user.JUDealer
import com.juagri.shared.domain.model.user.JURegion
import com.juagri.shared.domain.model.user.JUTerritory
import com.juagri.shared.domain.repo.user.UserRepository
import com.juagri.shared.utils.ResponseState
import com.juagri.shared.utils.endTime
import com.juagri.shared.utils.filterRegCodeUpdatedTime
import com.juagri.shared.utils.filterTCodeUpdatedTime
import com.juagri.shared.utils.filterUpdatedTime
import com.juagri.shared.utils.startTime
import dev.gitlive.firebase.firestore.CollectionReference
import dev.gitlive.firebase.firestore.Timestamp
import dev.gitlive.firebase.firestore.fromMilliseconds
import dev.gitlive.firebase.firestore.where
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

class UserRepositoryImpl(
    private val regionDB: CollectionReference,
    private val territoryDB: CollectionReference,
    private val dealerDB: CollectionReference,
    private val finYearDB: CollectionReference,
    private val finMonthDB: CollectionReference,
    private val userDetailsDao: UserDetailsDao
):UserRepository {
    override suspend fun getRegionList(regCodes: String): Flow<ResponseState<List<JURegion>>> = callbackFlow{
        trySend(ResponseState.Loading(true))
        val result = if(regCodes.isNotEmpty()){
            regionDB.where {
                Constants.FIELD_UPDATED_TIME greaterThan Timestamp.fromMilliseconds(userDetailsDao.getRegionLastUpdatedTime())
            }.where {
                Constants.FIELD_REG_CODE inArray  regCodes.split(",")
            }.get().documents
        }else{
            regionDB.filterUpdatedTime(userDetailsDao.getRegionLastUpdatedTime())
        }
        try {
            val regionList: List<JURegion> = result.map { it.data() }
            userDetailsDao.insertRegionMaster(regionList)
            trySend(ResponseState.Loading())
            trySend(ResponseState.Success(userDetailsDao.getRegionList()))
        }catch (e:Exception){
            trySend(ResponseState.Error())
        }
        awaitClose {
            channel.close()
        }
    }

    override suspend fun getTerritoryList(regCode: String): Flow<ResponseState<List<JUTerritory>>> = callbackFlow{
        trySend(ResponseState.Loading(true))
        val result = territoryDB.filterRegCodeUpdatedTime(regCode,userDetailsDao.getTerritoryLastUpdatedTime())
        try {
            val territoryList: List<JUTerritory> = result.map { it.data() }
            trySend(ResponseState.Loading())
            userDetailsDao.insertTerritoryMaster(territoryList)
            trySend(ResponseState.Success(userDetailsDao.getTerritoryList(regCode)))
        }catch (e:Exception){
            trySend(ResponseState.Error())
        }
        awaitClose {
            channel.close()
        }
    }

    override suspend fun getDealerList(tCode: String): Flow<ResponseState<List<JUDealer>>> = callbackFlow{
        trySend(ResponseState.Loading(true))
        val result = dealerDB.filterTCodeUpdatedTime(tCode,userDetailsDao.getDealerLastUpdatedTime())
        try {
            val dealerList: List<JUDealer> = result.map { it.data() }
            userDetailsDao.insertDealerMaster(dealerList)
            trySend(ResponseState.Loading())
            trySend(ResponseState.Success(userDetailsDao.getDealerList(tCode)))
        }catch (e:Exception){
            trySend(ResponseState.Error())
        }
        awaitClose {
            channel.close()
        }
    }

    override suspend fun getDealerListByCDO(cdoCode: String): Flow<ResponseState<List<JUDealer>>> = callbackFlow{
        println("CDO_Code: $cdoCode")
        trySend(ResponseState.Loading(true))
        val result = dealerDB.where {
            Constants.FIELD_CDO_CODE equalTo cdoCode
        }.get().documents
        try {
            trySend(ResponseState.Loading())
            trySend(ResponseState.Success(result.map { it.data() }))
        }catch (e:Exception){
            trySend(ResponseState.Error())
        }
        awaitClose {
            channel.close()
        }
    }

    override suspend fun getFinYear(): Flow<ResponseState<List<FinYear>>> = callbackFlow{
        trySend(ResponseState.Loading(true))
        val result = finYearDB.filterUpdatedTime(userDetailsDao.getFinYearLastUpdatedTime())
        try {
            val finYearList: List<FinYear> = result.map { it.data() }
            userDetailsDao.insertFinYear(finYearList)
            trySend(ResponseState.Loading())
            trySend(ResponseState.Success(userDetailsDao.getFinYearList()))
        }catch (e:Exception){
            trySend(ResponseState.Error())
        }
        awaitClose {
            channel.close()
        }
    }

    override suspend fun getFinMonth(startDate: Timestamp,endDate: Timestamp): Flow<ResponseState<List<FinMonth>>> = callbackFlow{
        trySend(ResponseState.Loading(true))
        val result = finMonthDB.filterUpdatedTime(userDetailsDao.getFinMonthLastUpdatedTime())
        try {
            val finMonthList: List<FinMonth> = result.map { it.data() }
            userDetailsDao.insertFinMonth(finMonthList)
            trySend(ResponseState.Loading())
            trySend(ResponseState.Success(userDetailsDao.getFinMonthList(startDate.startTime(),endDate.endTime())))
        }catch (e:Exception){
            trySend(ResponseState.Error())
        }
        awaitClose {
            channel.close()
        }
    }
}