package com.juagri.shared.domain.model.employee

import com.juagri.shared.domain.model.menu.HeaderSlideMenu
import com.juagri.shared.domain.model.user.JURegion
import com.juagri.shared.domain.model.user.JUTerritory
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class JUEmployee(
    @SerialName("active") var active:Boolean = false,
    @SerialName("mgmt") var mgmt:Boolean = false,
    @SerialName("admin") var admin:Boolean = false,
    @SerialName("code") var code:String? = null,
    @SerialName("hrCode") var hrCode:String? = null,
    @SerialName("mobile") var mobile:String? = null,
    @SerialName("mailId") var mailId:String? = null,
    @SerialName("name") var name:String? = null,
    @SerialName("role") var role:String? = null,
    @SerialName("roleId") var roleId:String? = null,
    @SerialName("fcmid") var fcmid:String? = null,
    @SerialName("menuId") var menuId:String? = null,
    @SerialName("regionCode") var regionCode:String? = null,
    @SerialName("territoryCode") var territoryCode:String? = null,
    @SerialName("stateCode") var stateCode:String? = null,
    var menuItems:HashMap<String,HeaderSlideMenu>? = null,
    val regionList: MutableList<JURegion> = mutableListOf(),
    val territoryList: MutableList<JUTerritory> = mutableListOf()
)