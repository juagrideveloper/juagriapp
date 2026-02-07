package com.juagri.shared.ui.navigation

import com.arkivanov.essenty.parcelable.Parcelable
import com.arkivanov.essenty.parcelable.Parcelize

@Parcelize
sealed class AppScreens: Parcelable {
  data object DummyScreen: AppScreens()
  data object Dashboard: AppScreens()
  data object Ledger: AppScreens()
  data object OnlineOrder: AppScreens()
  data object YourOrders: AppScreens()
  data object WeatherScreen: AppScreens()
  data object Profile: AppScreens()
  data object Devices: AppScreens()
  data object PromotionEntry: AppScreens()
  data object PromotionEntriesList: AppScreens()
  data object CDOFocusProduct: AppScreens()
  data object CDOLiquidation: AppScreens()
  data object LoginInfoScreen: AppScreens()
  data class JUDoctorCrop(val parentId:String): AppScreens()
  data class JUDoctorManagement(val parentId:String): AppScreens()
  data class JUDoctorChild(val parentId:String): AppScreens()
  data class JUDoctorSolution(val parentId:String): AppScreens()
  data object Participation: AppScreens()
  data object SendNotification: AppScreens()
  data object NotificationList: AppScreens()
  data object DealerLedgerOld: AppScreens()
  data object DealerLedgerOldExportToPDF: AppScreens()
  data class NotificationDetails(
    val id: String = "",
    val regCode: String = "",
    val regName: String = "",
    val roleId: String = "",
    val roleName: String = "",
    val title: String = "",
    val content: String = "",
    var filename: String = "",
    var unread: Int = 0,
    var updatedTime: Double? = null
  ): AppScreens()
}

