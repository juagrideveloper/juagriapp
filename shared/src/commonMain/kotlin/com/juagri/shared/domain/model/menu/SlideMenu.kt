package com.juagri.shared.domain.model.menu

data class SlideMenu(
    val index: Int,
    val menuID: String,
    val menuName: String,
    val imageId: Int,
    val isHeading: Boolean,
    val isActive: Boolean
)