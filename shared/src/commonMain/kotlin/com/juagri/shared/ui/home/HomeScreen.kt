package com.juagri.shared.ui.home

import Constants
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalUriHandler
import com.arkivanov.decompose.ExperimentalDecomposeApi
import com.juagri.shared.domain.model.employee.JUEmployee
import com.juagri.shared.ui.components.dialogs.ProgressDialog
import com.juagri.shared.ui.components.dialogs.SuccessDialog
import com.juagri.shared.ui.components.layouts.ScreenLayoutWithMenuActionBar
import com.juagri.shared.ui.dashboard.cdo.CDODashboardScreen
import com.juagri.shared.ui.dashboard.dealer.DealerDashboardScreen
import com.juagri.shared.ui.doctor.DoctorCropScreen
import com.juagri.shared.ui.focusProduct.CDOFocusProductSummary
import com.juagri.shared.ui.ledger.LedgerScreen
import com.juagri.shared.ui.liquidation.LiquidationScreen
import com.juagri.shared.ui.loginInfo.LoginInfoScreen
import com.juagri.shared.ui.navigation.AppScreens
import com.juagri.shared.ui.participation.ParticipationScreen
import com.juagri.shared.ui.profile.ProfileScreen
import com.juagri.shared.ui.promotion.PromotionEntryScreen
import com.juagri.shared.ui.promotionEntries.PromotionEntriesScreen
import com.juagri.shared.ui.weather.WeatherScreen
import com.juagri.shared.utils.AppUtils
import com.juagri.shared.utils.UIState
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
    val showAppUpdateDialog = mutableStateOf(false)
    var initCallNotDone = true
    ScreenLayoutWithMenuActionBar(title = viewModel.getScreenTitle(),router=router, employee = employee, viewModel = viewModel) {
        ProgressDialog(viewModel.z0001)
        when (val result = viewModel.employee.collectAsState().value) {
            is UIState.Success -> {
                viewModel.setJUEmployee(result.data)
                employee.value = result.data
                initScreen(router, viewModel)
            }
            else -> {}
        }
        when (val result = viewModel.appConfig.collectAsState().value) {
            is UIState.Success -> {
                if(result.data.versionCode != AppUtils.getAppVersion()){
                    showAppUpdateDialog.value = true
                }
            }
            else -> {}
        }
        if (initCallNotDone) {
            viewModel.getAppConfig()
            viewModel.getEmployeeDetails()
            initCallNotDone = false
        }
        val urlHandler= LocalUriHandler.current
        SuccessDialog(
            showAppUpdateDialog,
            "Update App!",
            "Please update the App from the Google Play Store!",
            false
        ) {
            showAppUpdateDialog.value = false
            viewModel.resetAppUpdate()
            urlHandler.openUri("https://play.google.com/store/apps/details?id=com.juagri.jucdo&hl=en")
            AppUtils.logout()
        }
    }
}

@OptIn(ExperimentalDecomposeApi::class)
@Composable
private fun initScreen(router: Router<AppScreens>,viewModel: HomeViewModel){
    RoutedContent(
        router = router,
        /*animation = predictiveBackAnimation(animation = stackAnimation(slide()),
            onBack = {
                //router.pop()
                     },
            backHandler = LocalRouterContext.current.backHandler
        )*/
    ) { screen ->
        when (screen) {
            AppScreens.Dashboard -> {
                when (Constants.CURRENT_APP_MODE) {
                    Constants.APP_MODE_CDO -> CDODashboardScreen()
                    Constants.APP_MODE_DEALER -> DealerDashboardScreen()
                    else -> ProfileScreen()
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
            AppScreens.WeatherScreen -> WeatherScreen()
            AppScreens.Profile -> ProfileScreen()
            AppScreens.PromotionEntry -> PromotionEntryScreen()
            AppScreens.PromotionEntriesList -> PromotionEntriesScreen()
            AppScreens.CDOFocusProduct -> CDOFocusProductSummary()
            AppScreens.CDOLiquidation -> LiquidationScreen()
            AppScreens.LoginInfoScreen -> LoginInfoScreen()
            AppScreens.Participation -> ParticipationScreen()
            else -> DummyScreen()
        }
    }
}

@Composable
private fun DummyScreen(){

}