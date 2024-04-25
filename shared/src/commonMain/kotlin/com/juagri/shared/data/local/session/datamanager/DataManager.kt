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

    fun setScreenId(screen: String){
        dataStore.screenId.value = screen
        setScreenTitle(dataStore.screenId.value)
    }

    fun getScreenId() = dataStore.screenId.value

    private fun setScreenTitle(screenId: String){
        dataStore.screenTitle.value = getScreenName(screenId)
    }

    fun getScreenTitle() = dataStore.screenTitle

    fun getScreenName(screenId: String) =
        when(screenId){
            Constants.HEADING_MENU_0001-> names.value.menu
            Constants.HEADING_MENU_0002-> names.value.services
            Constants.HEADING_MENU_0003-> names.value.user
            Constants.SCREEN_DASHBOARD-> names.value.dashboard
            Constants.SCREEN_LEDGER-> names.value.dealerLedger
            Constants.SCREEN_ONLINE_ORDER-> names.value.onlineOrder
            Constants.SCREEN_YOUR_ORDERS-> names.value.yourOrders
            Constants.SCREEN_JU_Doctor-> names.value.juDoctor
            Constants.SCREEN_WEATHER-> names.value.weather
            Constants.SCREEN_PROFILE-> names.value.profile
            Constants.SCREEN_DEVICES-> names.value.devices
            Constants.SCREEN_PROMOTION_ENTRY-> names.value.promotionActivity
            Constants.SCREEN_PROMOTION_ENTRIES_LIST-> names.value.promotionEntriesList
            Constants.SCREEN_CDO_FOCUS_PRODUCT->names.value.focusProduct
            Constants.SCREEN_CDO_LIQUIDATION->names.value.liquidation
            Constants.SCREEN_LOGIN_INFO ->names.value.loginInfo
            else -> ""
        }
}