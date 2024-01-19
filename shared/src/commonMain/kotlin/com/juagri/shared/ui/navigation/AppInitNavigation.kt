package com.juagri.shared.com.juagri.shared.ui.navigation

import androidx.compose.runtime.Composable
import com.arkivanov.decompose.ExperimentalDecomposeApi
import com.arkivanov.decompose.extensions.compose.jetbrains.stack.animation.predictiveBackAnimation
import com.arkivanov.decompose.extensions.compose.jetbrains.stack.animation.slide
import com.arkivanov.decompose.extensions.compose.jetbrains.stack.animation.stackAnimation
import com.arkivanov.decompose.router.stack.pop
import com.arkivanov.decompose.router.stack.replaceCurrent
import io.github.xxfast.decompose.router.LocalRouterContext
import io.github.xxfast.decompose.router.Router
import io.github.xxfast.decompose.router.content.RoutedContent
import io.github.xxfast.decompose.router.rememberRouter
import com.juagri.shared.ui.home.HomeScreen
import com.juagri.shared.ui.login.LoginScreen
import com.juagri.shared.ui.login.OTPScreen
import com.juagri.shared.ui.splash.SplashScreen

@OptIn(ExperimentalDecomposeApi::class)
@Composable
fun AppInitNav() {
    val router: Router<AppInitScreens> =
        rememberRouter(AppInitScreens::class) { listOf(AppInitScreens.Splash) }

    RoutedContent(
        router = router,
        animation = predictiveBackAnimation(
            animation = stackAnimation(slide()),
            onBack = { router.pop() },
            backHandler = LocalRouterContext.current.backHandler
        )
    ) { screen ->
        when (screen) {
            AppInitScreens.Splash -> SplashScreen { mode ->
                if (mode == 1) {
                    router.replaceCurrent(AppInitScreens.Login)
                } else {
                    router.replaceCurrent(AppInitScreens.Home)
                }
            }
            AppInitScreens.Login -> LoginScreen { router.replaceCurrent(AppInitScreens.Home) }
            AppInitScreens.OTP -> OTPScreen { router.pop() }
            AppInitScreens.Home -> HomeScreen { router.pop() }
        }
    }
}
