package com.juagri.shared.domain.model.menu

import dev.gitlive.firebase.firestore.Timestamp
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class HeaderSlideMenu(
    @SerialName("id") var id: String? = null,
    @SerialName("index") var index: Int? =null,
    @SerialName("menu_name") var menuName: String? = null,
    @SerialName("is_active") var isActive: Boolean = false,
    @SerialName("child_menus") var childMenus: List<ChildSlideMenu> = listOf(),
    @SerialName("updatedTime") var updatedTime: Timestamp? = null
)

@Serializable
data class ChildSlideMenu(
    @SerialName("id") var id: String? = null,
    @SerialName("index") var index: Int? =null,
    @SerialName("menu_name") var menuName: String? = null,
    @SerialName("is_active") var isActive: Boolean = false
)