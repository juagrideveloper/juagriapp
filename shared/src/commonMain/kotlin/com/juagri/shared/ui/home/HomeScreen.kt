package com.juagri.shared.ui.home

import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import com.arkivanov.decompose.ExperimentalDecomposeApi
import com.arkivanov.decompose.extensions.compose.jetbrains.stack.animation.predictiveBackAnimation
import com.arkivanov.decompose.extensions.compose.jetbrains.stack.animation.slide
import com.arkivanov.decompose.extensions.compose.jetbrains.stack.animation.stackAnimation
import com.arkivanov.decompose.router.stack.pop
import com.juagri.shared.ui.dashboard.DashboardScreen
import com.juagri.shared.ui.ledger.LedgerScreen
import com.juagri.shared.ui.navigation.AppScreens
import com.juagri.shared.ui.profile.ProfileScreen
import com.juagri.shared.ui.weather.WeatherScreen
import com.juagri.shared.utils.UIState
import com.juagri.shared.domain.model.employee.JUEmployee
import com.juagri.shared.ui.components.dialogs.ProgressDialog
import com.juagri.shared.ui.components.layouts.ScreenLayoutWithMenuActionBar
import io.github.xxfast.decompose.router.LocalRouterContext
import io.github.xxfast.decompose.router.Router
import io.github.xxfast.decompose.router.content.RoutedContent
import io.github.xxfast.decompose.router.rememberRouter
import moe.tlaster.precompose.koin.koinViewModel

@Composable
fun HomeScreen(onBack: () -> Unit) {
    val router: Router<AppScreens> =
        rememberRouter(AppScreens::class) { listOf(AppScreens.Dashboard) }
    val viewModel = koinViewModel(HomeViewModel::class)
    val employee = remember { mutableStateOf(JUEmployee()) }
    var initCallNotDone = true
    ScreenLayoutWithMenuActionBar(title = "Dashboard",router=router, employee = employee) {
        val enableLoading = remember { mutableStateOf(false) }
        when (val result = viewModel.employee.collectAsState().value) {
            is UIState.Loading -> enableLoading.value = result.isLoading
            is UIState.Success -> {
                employee.value = result.data
                initScreen(router)
            }
            else -> {}
        }
        if(initCallNotDone){
            initCallNotDone = false
            viewModel.getEmployeeDetails()
        }
        ProgressDialog(enableLoading,{})
    }
}
@OptIn(ExperimentalDecomposeApi::class)
@Composable
private fun initScreen(router: Router<AppScreens>){
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