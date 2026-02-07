package com.juagri.shared.data.remote.starclub

import com.juagri.shared.domain.model.starclub.StarClubCustomer
import com.juagri.shared.domain.repo.starclub.StarClubRepository
import dev.gitlive.firebase.firestore.CollectionReference
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class StarClubRepositoryImpl(
    private val starClubCollection: CollectionReference
) : StarClubRepository {

    override fun getStarClubCustomer(ccode: String): Flow<StarClubCustomer?> = flow {
        try {
            val snap = starClubCollection.document(ccode).get()
            val customer = if (snap.exists) snap.data<StarClubCustomer>() else null
            emit(customer)
        } catch (e: Exception) {
            emit(null)
        }
    }
}
