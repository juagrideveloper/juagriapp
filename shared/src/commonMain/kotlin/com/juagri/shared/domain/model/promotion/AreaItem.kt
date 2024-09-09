package com.juagri.shared.domain.model.promotion

import dev.gitlive.firebase.firestore.Timestamp
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class DistrictItem(
    @SerialName("id") override var id: String? = null,
    @SerialName("name") override var name: String? = null,
    @SerialName("st_code") var stCode: String? = null,
    @SerialName("st_name") var stName: String? = null,
    @SerialName("status") override var status: Double? = null,
    @SerialName("updatedTime") override var updatedTime: Timestamp? = null
): AreaItem

@Serializable
data class VillageItem(
    @SerialName("id") override var id: String? = null,
    @SerialName("name") override var name: String? = null,
    @SerialName("district_id") var districtId: String? = null,
    @SerialName("status") override var status: Double? = null,
    @SerialName("updatedTime") override var updatedTime: Timestamp? = null
): AreaItem

interface AreaItem{
    var id: String?
    var name: String?
    var status: Double?
    var updatedTime: Timestamp?
}

@Serializable
data class AreaDropdownItem(
    var districtItem: DistrictItem? = null,
    var villageItem: VillageItem? = null
)