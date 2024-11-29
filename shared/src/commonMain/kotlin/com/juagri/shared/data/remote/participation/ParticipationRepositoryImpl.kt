package com.juagri.shared.data.remote.participation

import com.juagri.shared.data.local.dao.cdo.PromotionDao
import com.juagri.shared.domain.model.employee.JUEmployee
import com.juagri.shared.domain.model.promotion.ParticipationCounts
import com.juagri.shared.domain.model.promotion.PromotionEntry
import com.juagri.shared.domain.model.promotion.PromotionEventItem
import com.juagri.shared.domain.repo.participation.ParticipationRepository
import com.juagri.shared.utils.Constants
import com.juagri.shared.utils.ResponseState
import com.juagri.shared.utils.filterUpdatedTime
import com.juagri.shared.utils.toDDMMYYYY
import com.juagri.shared.utils.toMMYYYY
import com.juagri.shared.utils.value
import dev.gitlive.firebase.firestore.CollectionReference
import dev.gitlive.firebase.firestore.Timestamp
import dev.gitlive.firebase.firestore.fromMilliseconds
import dev.gitlive.firebase.firestore.where
import io.ktor.util.date.GMTDate
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

class ParticipationRepositoryImpl(
    private val promotionEventDB: CollectionReference,
    private val participationDB: CollectionReference,
    private val promotionCountDB: CollectionReference,
    private val promotionDao: PromotionDao
): ParticipationRepository {
    override suspend fun getParticipationDetails(employee: JUEmployee): Flow<ResponseState<List<ParticipationCounts>>> =
        callbackFlow {
            val finYearStartDate = 1711926000000
            //val finYearStartDate = 1722466800000
            val result = mutableListOf<ParticipationCounts>()
            trySend(ResponseState.Loading(true))
            try {
                println("Step 1")
                val eventList: List<PromotionEventItem> =
                    promotionEventDB.filterUpdatedTime(promotionDao.getPromotionEventLastUpdatedTime())
                        .map { it.data() }
                println("Step 2")
                result.addAll(eventList.map { ParticipationCounts(it.id.value(), it.name.value()) })
                println("Step 3")
                val countsList = getEntries(employee)
                //val countsList = promotionCountDB.document(employee.code.value()).get().reference.get().data<Map<String, String?>>()
                    println("Step 4")
                println("countsList: $countsList")
                participationDB
                    .where { "participant_" + employee.roleId.value() + "_userId" equalTo employee.code.value() }
                    .where { "updated_time" greaterThan Timestamp.fromMilliseconds(finYearStartDate.toDouble()) }
                    .get()
                    .documents.map { doc ->
                        val item = doc.data<PromotionEntry>()
                        println(item)
                        result.forEachIndexed { index, participation ->
                            if (participation.actId == item.actId) {
                                if (item.updatedTime.value() > finYearStartDate) {
                                    result[index].yParticipated++
                                    val today = GMTDate().toMMYYYY()
                                    println("Today: $today Entry Date: ${item.updatedTime.toDDMMYYYY()}")
                                    /*if (today == item.updatedTime.toMMYYYY()) {
                                        result[index].mCount++
                                    }*/
                                }
                            }
                        }
                    }
                result.forEachIndexed { index, item ->
                    countsList.forEach { count->
                        count[item.actId + "_Yr_plan"]?.let { count ->
                            result[index].yPlan += count.toDouble()
                        }
                        count[item.actId + "_Yr_act"]?.let { count ->
                            result[index].yActual += count.toDouble()
                        }
                    }
                    result[index].yTotal = result[index].yActual + result[index].yParticipated
                    println("result $result")
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
            trySend(ResponseState.Loading(false))
            trySend(ResponseState.Success(result))
            awaitClose {
                channel.close()
            }
        }

    private suspend fun getEntries(employee: JUEmployee): List<Map<String,String>>{
        val countsList = mutableListOf<Map<String,String>>()
        countsList.addAll(
            promotionCountDB.where { "cdoid" equalTo employee.code.value() }.get().documents.map { it.data() }
        )
        return countsList
    }
}