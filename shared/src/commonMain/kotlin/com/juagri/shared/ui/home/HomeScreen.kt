package com.juagri.shared.ui.home

import androidx.compose.runtime.Composable
import com.arkivanov.decompose.ExperimentalDecomposeApi
import com.arkivanov.decompose.extensions.compose.jetbrains.stack.animation.predictiveBackAnimation
import com.arkivanov.decompose.extensions.compose.jetbrains.stack.animation.slide
import com.arkivanov.decompose.extensions.compose.jetbrains.stack.animation.stackAnimation
import com.arkivanov.decompose.router.stack.pop
import com.juagri.shared.com.juagri.shared.ui.dashboard.DashboardScreen
import com.juagri.shared.com.juagri.shared.ui.ledger.LedgerScreen
import com.juagri.shared.com.juagri.shared.ui.navigation.AppScreens
import com.juagri.shared.com.juagri.shared.ui.profile.ProfileScreen
import com.juagri.shared.com.juagri.shared.ui.weather.WeatherScreen
import com.juagri.shared.ui.components.layouts.ScreenLayoutWithMenuActionBar
import io.github.xxfast.decompose.router.LocalRouterContext
import io.github.xxfast.decompose.router.Router
import io.github.xxfast.decompose.router.content.RoutedContent
import io.github.xxfast.decompose.router.rememberRouter
import moe.tlaster.precompose.koin.koinViewModel

@OptIn(ExperimentalDecomposeApi::class)
@Composable
fun HomeScreen(onBack: () -> Unit) {
    val router: Router<AppScreens> =
        rememberRouter(AppScreens::class) { listOf(AppScreens.Dashboard) }
    val viewModel = koinViewModel(HomeViewModel::class)
    ScreenLayoutWithMenuActionBar(title = "Dashboard",router=router) {

        RoutedContent(
            router = router,
            animation = predictiveBackAnimation(
                animation = stackAnimation(slide()),
                onBack = { router.pop() },
                backHandler = LocalRouterContext.current.backHandler
            )
        ) { screen ->
            when (screen) {
                AppScreens.Dashboard -> DashboardScreen()
                AppScreens.Ledger -> LedgerScreen()
                AppScreens.Weather -> WeatherScreen()
                AppScreens.Profile -> ProfileScreen()
            }
        }
    }
}