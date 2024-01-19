package com.juagri.shared.data.local.session.datamanager

import com.juagri.shared.domain.model.menu.SlideMenu
import com.juagri.shared.data.local.session.datamanager.DataStore

class DataManager(private val dataStore: DataStore) {

    fun setMenuItems(menus: List<SlideMenu>) = dataStore.menuItems.apply {
        clear()
        addAll(menus)
    }

    fun menuItems(): List<SlideMenu> = dataStore.menuItems.toList()
}