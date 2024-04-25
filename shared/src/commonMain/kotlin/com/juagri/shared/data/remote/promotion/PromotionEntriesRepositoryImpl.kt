package com.juagri.shared.data.remote.promotion

import com.juagri.shared.domain.model.promotion.ParticipationEntry
import com.juagri.shared.domain.repo.promotion.PromotionEntriesRepository
import com.juagri.shared.utils.ResponseState
import com.juagri.shared.utils.uploadImages
import dev.gitlive.firebase.firestore.CollectionReference
import dev.gitlive.firebase.firestore.Timestamp
import dev.gitlive.firebase.firestore.fromMilliseconds
import dev.gitlive.firebase.firestore.toMilliseconds
import dev.gitlive.firebase.firestore.where
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

class PromotionEntriesRepositoryImpl(
    private val promotionEntryDB: CollectionReference,
): PromotionEntriesRepository {
    override fun getRecentPromotionEntries(tCode: String, roleId: String): Flow<ResponseState<List<Map<String, String>>>> =
        callbackFlow {
            trySend(ResponseState.Loading(true))
            val whereCode = if (tCode.lowercase().startsWith("rgn")) {
                "updated_regcode"
            } else {
                "updated_terrcode"
            }

            val result =
                promotionEntryDB.where {
                    whereCode equalTo tCode
                }.where {
                    "updated_time" greaterThan Timestamp.fromMilliseconds(
                        Timestamp.now().toMilliseconds() - 86400000
                    )
                }.where {
                    "participant_" + roleId + "_userId" equalTo ""
                }.get().documents
            try {
                val entries: List<Map<String, String>> = result.map { it.reference.get().data() }
                trySend(ResponseState.Loading())
                trySend(ResponseState.Success(entries))
            } catch (e: Exception) {
                e.printStackTrace()
                trySend(ResponseState.Error())
            }
            awaitClose {
                channel.close()
            }
        }

    override fun setPromotionParticipation(
        entry: ParticipationEntry,
        updatedDetails: Map<String, Any>,
        roleId: String
    ): Flow<ResponseState<String>> =
        callbackFlow {
            trySend(ResponseState.Loading(true))
            try {
                val participantUpdatedDetails = updatedDetails.toMutableMap()
                val filenames = List(entry.images.size) { index -> "${entry.entryId}_${roleId}_sub_"+index+".jpg" }
                participantUpdatedDetails["participant_"+roleId+"_filenames"] = filenames.joinToString(",")
                promotionEntryDB.document(entry.entryId)
                    .update(participantUpdatedDetails)
                uploadImages(entry.images, filenames) {
                    trySend(ResponseState.Loading())
                    trySend(ResponseState.Success(entry.entryId))
                }
            } catch (e: Exception) {
                e.printStackTrace()
                trySend(ResponseState.Error())
            }
            awaitClose {
                channel.close()
            }
        }
}