package com.juagri.shared.data.local.session.datamanager

import com.juagri.shared.domain.model.employee.JUEmployee
import com.juagri.shared.domain.model.menu.HeaderSlideMenu
import com.juagri.shared.utils.strings.AppLanguage
import com.juagri.shared.utils.strings.EN_Names
import com.juagri.shared.utils.strings.HI_Names
import com.juagri.shared.utils.strings.TN_Names

class DataManager(private val dataStore: DataStore) {

    val names = dataStore.labels

    fun setLanguage(language: AppLanguage){
        dataStore.labels.value = when(language){
            is AppLanguage.English ->  EN_Names
            is AppLanguage.Tamil ->  TN_Names
            is AppLanguage.Hindi ->  HI_Names
        }
    }
    fun setMenuItems(menus: List<HeaderSlideMenu>) = dataStore.menuItems.value.apply {
        clear()
        addAll(menus)
    }

    fun menuItems(): List<HeaderSlideMenu> = dataStore.menuItems.value.toList()

    fun setEmployee(employee: JUEmployee){
        dataStore.employee = employee
    }

    fun getEmployee() = dataStore.employee

}