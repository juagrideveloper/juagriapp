package com.juagri.shared.data.remote.doctor

import com.juagri.shared.data.local.dao.common.JUDoctorDao
import com.juagri.shared.domain.model.doctor.JUDoctorDataItem
import com.juagri.shared.domain.repo.doctor.JUDoctorRepository
import com.juagri.shared.utils.ResponseState
import com.juagri.shared.utils.filterUpdatedTime
import dev.gitlive.firebase.firestore.CollectionReference
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

class JUDoctorRepositoryImpl(
    private val doctorDB: CollectionReference,
    private val doctorDao: JUDoctorDao
): JUDoctorRepository {
    override suspend fun getJUDoctorItems(parentId: String): Flow<ResponseState<List<JUDoctorDataItem>>> = callbackFlow{
        trySend(ResponseState.Loading(true))
        val result = doctorDB.filterUpdatedTime(doctorDao.getDoctorLastUpdatedTime())
        trySend(ResponseState.Loading())
        try {
            if(result.isNotEmpty()){
                doctorDao.insertJUDoctor(result.map { it.data() })
            }
            trySend(ResponseState.Success(doctorDao.getJUDoctor(parentId)))
        }catch (e:Exception){
            trySend(ResponseState.Error(e))
        }
        awaitClose {
            channel.close()
        }
    }
}