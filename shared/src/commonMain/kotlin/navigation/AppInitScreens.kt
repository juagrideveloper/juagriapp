package navigation

import com.arkivanov.essenty.parcelable.Parcelable
import com.arkivanov.essenty.parcelable.Parcelize

@Parcelize
sealed class AppInitScreens(): Parcelable {
  data object Splash: AppInitScreens()
  data object Login: AppInitScreens()
  data object Home: AppInitScreens()
}

