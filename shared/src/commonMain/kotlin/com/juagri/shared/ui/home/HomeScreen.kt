package com.juagri.shared.ui.home

import Constants
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import com.arkivanov.decompose.ExperimentalDecomposeApi
import com.arkivanov.decompose.extensions.compose.jetbrains.stack.animation.predictiveBackAnimation
import com.arkivanov.decompose.extensions.compose.jetbrains.stack.animation.slide
import com.arkivanov.decompose.extensions.compose.jetbrains.stack.animation.stackAnimation
import com.arkivanov.decompose.router.stack.pop
import com.juagri.shared.ui.doctor.DoctorCropScreen
import com.juagri.shared.domain.model.employee.JUEmployee
import com.juagri.shared.ui.components.dialogs.ProgressDialog
import com.juagri.shared.ui.components.layouts.ScreenLayoutWithMenuActionBar
import com.juagri.shared.ui.dashboard.cdo.DashboardScreen
import com.juagri.shared.ui.dashboard.dealer.DealerDashboardScreen
import com.juagri.shared.ui.ledger.LedgerScreen
import com.juagri.shared.ui.navigation.AppScreens
import com.juagri.shared.ui.profile.ProfileScreen
import com.juagri.shared.ui.weather.WeatherScreen
import com.juagri.shared.utils.UIState
import io.github.xxfast.decompose.router.LocalRouterContext
import io.github.xxfast.decompose.router.Router
import io.github.xxfast.decompose.router.content.RoutedContent
import io.github.xxfast.decompose.router.rememberRouter
import moe.tlaster.precompose.koin.koinViewModel
import moe.tlaster.precompose.navigation.BackHandler

@Composable
fun HomeScreen(onBack: () -> Unit) {
    val router: Router<AppScreens> =
        rememberRouter(AppScreens::class) { listOf(AppScreens.Profile) }
    val viewModel = koinViewModel(HomeViewModel::class)
    viewModel.setDemoUser()
    val employee = remember { mutableStateOf(JUEmployee()) }
    var initCallNotDone = true
    BackHandler {
        viewModel.writeLog("BackStack: "+ router.stack.value.backStack.size)
        if(router.stack.value.backStack.isNotEmpty()) {
            router.pop()
        }else{
            onBack.invoke()
        }
    }
    ScreenLayoutWithMenuActionBar(title = viewModel.getScreenTitle(),router=router, employee = employee, viewModel = viewModel) {
        ProgressDialog(viewModel.z0001)
        when (val result = viewModel.employee.collectAsState().value) {
            is UIState.Success -> {
                viewModel.setJUEmployee(result.data)
                employee.value = result.data
                viewModel.setDemoUser()
                initScreen(router, viewModel)
            }
            else -> {}
        }
        if (initCallNotDone) {
            initCallNotDone = false
            viewModel.getEmployeeDetails()
        }
    }
}
@OptIn(ExperimentalDecomposeApi::class)
@Composable
private fun initScreen(router: Router<AppScreens>,viewModel: HomeViewModel){
    RoutedContent(
        router = router,
        animation = predictiveBackAnimation(
            animation = stackAnimation(slide()),
            onBack = { router.pop() },
            backHandler = LocalRouterContext.current.backHandler
        )
    ) { screen ->
        when (screen) {
            AppScreens.Dashboard -> {
                when (viewModel.getRoleID()) {
                    Constants.EMP_ROLE_SO -> DashboardScreen()
                    Constants.EMP_ROLE_CDO -> DashboardScreen()
                    Constants.EMP_ROLE_DL -> DealerDashboardScreen()
                }
            }
            AppScreens.Ledger -> LedgerScreen()
            is AppScreens.JUDoctorCrop -> DoctorCropScreen(router, screen.parentId)
            is AppScreens.JUDoctorManagement -> DoctorCropScreen(router, screen.parentId)
            is AppScreens.JUDoctorChild -> DoctorCropScreen(router, screen.parentId)
            is AppScreens.JUDoctorSolution -> DoctorCropScreen(router, screen.parentId)
            AppScreens.OnlineOrder -> WeatherScreen()
            AppScreens.YourOrders -> WeatherScreen()
            AppScreens.Devices -> WeatherScreen()
            AppScreens.Weather -> WeatherScreen()
            AppScreens.Profile -> ProfileScreen()
            else -> DummyScreen()
        }
    }
}

@Composable
private fun DummyScreen(){

}