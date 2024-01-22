package com.juagri.shared.com.juagri.shared.ui.navigation

import com.arkivanov.essenty.parcelable.Parcelable
import com.arkivanov.essenty.parcelable.Parcelize

@Parcelize
sealed class AppScreens(): Parcelable {
  data object Dashboard: AppScreens()
  data object Ledger: AppScreens()
  data object Weather: AppScreens()
  data object Profile: AppScreens()
}

