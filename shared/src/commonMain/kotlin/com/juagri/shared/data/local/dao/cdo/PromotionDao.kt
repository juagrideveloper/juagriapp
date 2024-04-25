package com.juagri.shared.data.local.dao.cdo

import com.juagri.shared.PromotionDetailsQueries
import com.juagri.shared.domain.model.promotion.DistrictItem
import com.juagri.shared.domain.model.promotion.PromotionEventItem
import com.juagri.shared.domain.model.promotion.PromotionField
import com.juagri.shared.domain.model.promotion.VillageItem
import com.juagri.shared.utils.isDeleted
import com.juagri.shared.utils.toTimeStamp
import com.juagri.shared.utils.value

class PromotionDao(private val queries: PromotionDetailsQueries) {

    fun insertPromotionEventList(list: List<PromotionEventItem>) {
        list.forEach {
            if (it.status.isDeleted()) {
                queries.deletePromotionEventList(it.id.value())
            } else {
                queries.insertPromotionEventListMaster(
                    id = it.id.value(),
                    name = it.name.value(),
                    order_by = it.orderBy.value(),
                    updatedTime = it.updatedTime.value()
                )
            }
        }
    }

    fun getPromotionEventList(): List<PromotionEventItem> {
        return queries.getPromotionEventList().executeAsList().map {
            PromotionEventItem(
                id = it.id.value(),
                name = it.name.value(),
                orderBy = it.order_by.value()
            )
        }
    }

    fun getPromotionEventLastUpdatedTime(): Double= queries.getPromotionEventListLastUpdatedTime().executeAsOne().max.value()

    fun insertPromotionEventFields(list: List<PromotionField>) {
        list.forEach {
            if (it.status.isDeleted()) {
                queries.deletePromotionEventFields(it.fieldId.value())
            } else {
                queries.insertPromotionEventFieldMaster(
                    id = it.fieldId.value(),
                    data_ = it.data.value(),
                    editable = it.editable.value(),
                    ismandatory = it.isMandatory.value(),
                    length = it.length.value(),
                    maxlength = it.maxlength.value(),
                    actId = it.actId.value(),
                    name = it.name.value(),
                    param_ = it.param.value(),
                    parentid = it.parentId.value(),
                    slno = it.slNo.value(),
                    type = it.type.value(),
                    validation = it.validation.value(),
                    value_ = it.value.value(),
                    updatedTime = it.updatedTime.value()
                )
            }
        }
    }

    fun getPromotionEventFields(actId: String): List<PromotionField> {
        return queries.getPromotionEventFields(actId).executeAsList().map {
            PromotionField(
                fieldId = it.id.value(),
                data = it.data_.value(),
                editable = it.editable.value(),
                isMandatory = it.ismandatory.value(),
                length = it.length.value(),
                maxlength = it.maxlength.value(),
                actId = it.actId.value(),
                name = it.name.value(),
                param = it.param_.value(),
                parentId = it.parentid.value(),
                slNo = it.slno.value(),
                type = it.type.value(),
                validation = it.validation.value(),
                value = it.value_.value(),
                updatedTime = it.updatedTime.toTimeStamp()
            )
        }
    }

    fun getPromotionEventFieldLastUpdatedTime(): Double= queries.getPromotionEventFieldsLastUpdatedTime().executeAsOne().max.value()

    fun insertDistrictList(list: List<DistrictItem>) {
        list.forEach {
            if (it.status.isDeleted()) {
                queries.deleteDistrictMaster(it.id.value())
            } else {
                queries.insertDistrictMaster(
                    id = it.id.value(),
                    name = it.name.value(),
                    st_code = it.stCode.value(),
                    st_name = it.stName.value(),
                    updatedTime = it.updatedTime.value()
                )
            }
        }
    }

    fun getDistrictList(): List<DistrictItem> {
        return queries.getDistrictMaster().executeAsList().map {
            DistrictItem(
                id = it.id.value(),
                name = it.name.value(),
                stCode = it.st_code.value(),
                stName = it.st_name.value(),
                updatedTime = it.updatedTime.toTimeStamp()
            )
        }
    }

    fun getDistrictListLastUpdatedTime(): Double= queries.getDistrictMasterLastUpdatedTime().executeAsOne().max.value()

    fun insertVillageList(list: List<VillageItem>) {
        list.forEach {
            if (it.status.isDeleted()) {
                queries.deleteVillageMaster(it.id.value())
            } else {
                queries.insertVillageMaster(
                    id = it.id.value(),
                    name = it.name.value(),
                    district_id = it.districtId.value(),
                    updatedTime = it.updatedTime.value()
                )
            }
        }
    }

    fun getVillageList(districtId: String): List<VillageItem> {
        return queries.getVillageMaster(districtId).executeAsList().map {
            VillageItem(
                id = it.id.value(),
                name = it.name.value(),
                districtId = it.district_id.value(),
                updatedTime = it.updatedTime.toTimeStamp()
            )
        }
    }

    fun getVillageListLastUpdatedTime(districtId: String): Double= queries.getVillageMasterLastUpdatedTime(districtId).executeAsOne().max.value()
}