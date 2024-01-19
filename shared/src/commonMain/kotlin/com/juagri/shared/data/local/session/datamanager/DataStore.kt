package com.juagri.shared.data.local.session.datamanager

import com.juagri.shared.domain.model.menu.SlideMenu

data class DataStore(val menuItems:MutableList<SlideMenu> = mutableListOf())
