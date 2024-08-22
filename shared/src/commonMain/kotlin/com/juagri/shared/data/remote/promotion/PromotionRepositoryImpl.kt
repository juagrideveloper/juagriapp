package com.juagri.shared.data.remote.promotion

import Constants
import com.juagri.shared.data.local.dao.cdo.PromotionDao
import com.juagri.shared.domain.model.employee.JUEmployee
import com.juagri.shared.domain.model.promotion.DistrictItem
import com.juagri.shared.domain.model.promotion.PromotionDashboard
import com.juagri.shared.domain.model.promotion.PromotionEventItem
import com.juagri.shared.domain.model.promotion.PromotionField
import com.juagri.shared.domain.model.promotion.VillageItem
import com.juagri.shared.domain.repo.promotion.PromotionRepository
import com.juagri.shared.utils.JUError
import com.juagri.shared.utils.ResponseState
import com.juagri.shared.utils.uploadImages
import com.juagri.shared.utils.filterUpdatedTime
import com.juagri.shared.utils.startTime
import com.juagri.shared.utils.toTimeStamp
import com.juagri.shared.utils.value
import dev.gitlive.firebase.firestore.CollectionReference
import dev.gitlive.firebase.firestore.Timestamp
import dev.gitlive.firebase.firestore.fromMilliseconds
import dev.gitlive.firebase.firestore.toMilliseconds
import dev.gitlive.firebase.firestore.where
import io.ktor.util.date.GMTDate
import io.ktor.util.date.getTimeMillis
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.jsonArray
import kotlinx.serialization.json.jsonObject

class PromotionRepositoryImpl(
    private val promotionEventDB: CollectionReference,
    private val promotionFieldDB: CollectionReference,
    private val districtDB: CollectionReference,
    private val villageDB: CollectionReference,
    private val promotionEntryDB: CollectionReference,
    private val promotionCountDB: CollectionReference,
    private val promotionMobileNumbersDB: CollectionReference,
    private val promotionDao: PromotionDao
) : PromotionRepository {
    override suspend fun getPromotionEventList(): Flow<ResponseState<List<PromotionEventItem>>> =
        callbackFlow {
            trySend(ResponseState.Loading(true))
            val result =
                promotionEventDB.filterUpdatedTime(promotionDao.getPromotionEventLastUpdatedTime())
            try {
                val regionList: List<PromotionEventItem> = result.map { it.data() }
                promotionDao.insertPromotionEventList(regionList)
                trySend(ResponseState.Loading())
                trySend(ResponseState.Success(promotionDao.getPromotionEventList()))
            } catch (e: Exception) {
                trySend(ResponseState.Error())
            }
            awaitClose {
                channel.close()
            }
        }

    override suspend fun getPromotionEventFieldsList(actId: String): Flow<ResponseState<List<PromotionField>>> =
        callbackFlow {
            trySend(ResponseState.Loading(true))
            val result = promotionFieldDB.filterUpdatedTime(promotionDao.getPromotionEventFieldLastUpdatedTime())
            try {
                val regionList: List<PromotionField> = result.map { it.data() }
                promotionDao.insertPromotionEventFields(regionList)
                trySend(ResponseState.Loading())
                trySend(ResponseState.Success(promotionDao.getPromotionEventFields(actId)))
            } catch (e: Exception) {
                e.printStackTrace()
                trySend(ResponseState.Error())
            }
            awaitClose {
                channel.close()
            }
        }

    override suspend fun getDistrictList(stCode: String): Flow<ResponseState<List<DistrictItem>>> =
        callbackFlow {
            trySend(ResponseState.Loading(true))
            val result = districtDB.where {
                Constants.FIELD_UPDATED_TIME greaterThan Timestamp.fromMilliseconds(promotionDao.getDistrictListLastUpdatedTime())
            }.where {
                Constants.FIELD_STATE_CODE equalTo stCode
            }.get().documents
            try {
                val list: List<DistrictItem> = result.map { it.data() }
                promotionDao.insertDistrictList(list)
                trySend(ResponseState.Loading())
                trySend(ResponseState.Success(promotionDao.getDistrictList()))
            } catch (e: Exception) {
                trySend(ResponseState.Error())
            }
            awaitClose {
                channel.close()
            }
        }

    override suspend fun getVillageList(districtId: String): Flow<ResponseState<List<VillageItem>>> =
        callbackFlow {
            trySend(ResponseState.Loading(true))
            val list = villageDB.where {
                Constants.FIELD_DISTRICT_CODE equalTo districtId
            }.where {
                Constants.FIELD_UPDATED_TIME greaterThan Timestamp.fromMilliseconds(promotionDao.getVillageListLastUpdatedTime(districtId))
            }.get().documents
            try {
                val regionList: List<VillageItem> = list.map { it.data() }
                promotionDao.insertVillageList(regionList)
                trySend(ResponseState.Loading())
                trySend(ResponseState.Success(promotionDao.getVillageList(districtId)))
            } catch (e: Exception) {
                trySend(ResponseState.Error())
            }
            awaitClose {
                channel.close()
            }
        }

    override suspend fun setPromotionEntry(
        entryItems: MutableMap<String, Any>,
        files: List<ByteArray>
    ): Flow<ResponseState<Boolean>> =
    callbackFlow {
        trySend(ResponseState.Loading(true))
        try {
            val phoneNo = if(entryItems.containsKey("farmermobile")){
                entryItems["farmermobile"].toString()
            }else if(entryItems.containsKey("farmerphone")){
                entryItems["farmerphone"].toString()
            }else if(entryItems.containsKey("distributorretailerphone")){
                entryItems["distributorretailerphone"].toString()
            }else ""

            var isMobileNumberUnique = true
            if(phoneNo.isNotEmpty() && entryItems["activity_code"]?.toString().value() != "PM_DFD"){
                isMobileNumberUnique = !promotionMobileNumbersDB.document(phoneNo).get().exists
            }

            if(isMobileNumberUnique) {
                var farmerCountNotMet = true

                if(entryItems["activity_code"]?.toString().value() == "PM_FM"){
                    val count = promotionEntryDB
                        .where { "activity_code" equalTo "PM_FM" }
                        .where { "updated_empcode" equalTo entryItems["updated_empcode"]?.toString().value() }
                        .where { "updated_time" greaterThan Timestamp.now().startTime().toTimeStamp() }.get().documents.size
                    farmerCountNotMet = (count < 3)
                }
                if(farmerCountNotMet) {
                    val entryId =
                        entryItems["updated_empcode"].toString() + "-" + GMTDate().timestamp
                    entryItems["entryId"] = entryId
                    println("JUAgriAppTestLogs: PromotionRepositoryImpl setPromotionEntry")
                    promotionEntryDB.document(entryId).set(entryItems)
                    val filenames = List(files.size) { index -> "${entryId}_" + index + ".jpg" }
                    if (files.isNotEmpty()) {
                        promotionEntryDB.document(entryId)
                            .update(mapOf("filenames" to filenames.joinToString(",")))
                        uploadImages(files, filenames) {
                            trySend(ResponseState.Loading())
                            trySend(ResponseState.Success(true))
                        }
                    } else {
                        trySend(ResponseState.Loading())
                        trySend(ResponseState.Success(true))
                    }
                }else{
                    trySend(ResponseState.Loading())
                    trySend(ResponseState.Error(JUError.CustomError("Former meeting allowed only 3!")))
                }
            }else{
                trySend(ResponseState.Loading())
                trySend(ResponseState.Error(JUError.CustomError("Mobile number already exist!")))
            }
        } catch (e: Exception) {
            e.printStackTrace()
            trySend(ResponseState.Error())
        }
        awaitClose {
            channel.close()
        }
    }

    override suspend fun getDashboard(employee: JUEmployee): Flow<ResponseState<List<PromotionDashboard>>>  =
        callbackFlow {
            trySend(ResponseState.Loading(true))
            val result = mutableListOf<PromotionDashboard>()
            try {
                val eventList: List<PromotionEventItem> = promotionEventDB.filterUpdatedTime(promotionDao.getPromotionEventLastUpdatedTime()).map { it.data() }
                result.addAll(eventList.map { PromotionDashboard(it.id.value(),it.name.value()) })
                //val entriesCount = promotionCountDB.document(employee.code.value()).get().reference.get().data<Map<String,String>>()
                val entries = getEntries(employee)
                entries.forEach {entriesCount->
                    result.map {
                        entriesCount[it.actId + "_Mon_act"]?.let { count ->
                            it.mActual += count.toDouble()
                        }
                        entriesCount[it.actId + "_Mon_plan"]?.let { count ->
                            it.mPlan += count.toDouble()
                        }
                        entriesCount[it.actId + "_Yr_act"]?.let { count ->
                            it.yActual += count.toDouble()
                        }
                        entriesCount[it.actId + "_Yr_plan"]?.let { count ->
                            it.yPlan += count.toDouble()
                        }
                    }
                }
                trySend(ResponseState.Loading(false))
                trySend(ResponseState.Success(result))
            } catch (e: Exception) {
                e.printStackTrace()
                trySend(ResponseState.Loading(false))
                trySend(ResponseState.Success(result))
            }
            awaitClose {
                channel.close()
            }
        }

    private suspend fun getEntries(employee: JUEmployee): List<Map<String,String>>{
        val countsList = mutableListOf<Map<String,String>>()
        when(employee.roleId.value()){
            Constants.EMP_ROLE_RM, Constants.EMP_ROLE_DM -> {
                countsList.addAll(
                    promotionCountDB.where { Constants.FIELD_REG_CODE inArray  employee.regionCode.value().split(",") }.get().documents.map { it.data() }
                )
            }
            Constants.EMP_ROLE_SO -> {
                countsList.addAll(
                    promotionCountDB.where { Constants.FIELD_T_CODE inArray  employee.territoryCode.value().split(",") }.get().documents.map { it.data() }
                )
            }
            Constants.EMP_ROLE_CDO -> {
                countsList.add(promotionCountDB.document(employee.code.value()).get().reference.get()
                    .data<Map<String, String>>())
            }
        }
        return countsList
    }
}