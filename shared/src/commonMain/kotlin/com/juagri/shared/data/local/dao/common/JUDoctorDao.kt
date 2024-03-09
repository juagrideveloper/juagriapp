package com.juagri.shared.data.local.dao.common

import com.juagri.shared.JUDoctorDetailsQueries
import com.juagri.shared.domain.model.doctor.JUDoctorDataItem
import com.juagri.shared.utils.toTimeStamp
import com.juagri.shared.utils.value

class JUDoctorDao(private val queries: JUDoctorDetailsQueries) {

    fun insertJUDoctor(list: List<JUDoctorDataItem>) {
        list.forEach {
            queries.insertJUDoctor(
                id = it.id.value(),
                name = it.name.value(),
                image = it.image.value(),
                type = it.type.value().toLong(),
                parentId = it.parentId.value(),
                has_child = it.hasChild.value(),
                updatedTime = it.updatedTime.value()
            )
        }
    }

    fun getJUDoctor(parentId: String) =
        queries.getJUDoctor(parentId).executeAsList().map {
            JUDoctorDataItem(
                id = it.id.value(),
                name = it.name.value(),
                image = it.image.value(),
                type = it.type.value().toInt(),
                parentId = it.parentId.value(),
                hasChild = it.has_child.value(),
                updatedTime = it.updatedTime.toTimeStamp()
            )
        }

    fun getDoctorLastUpdatedTime() = queries.getJUDoctorLastUpdatedTime().executeAsOne().max.value()

}