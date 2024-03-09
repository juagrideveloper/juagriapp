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
  data object Weather: AppScreens()
  data object Profile: AppScreens()
  data object Devices: AppScreens()
  data class JUDoctorCrop(val parentId:String): AppScreens()
  data class JUDoctorManagement(val parentId:String): AppScreens()
  data class JUDoctorChild(val parentId:String): AppScreens()
  data class JUDoctorSolution(val parentId:String): AppScreens()
}

