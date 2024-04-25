package com.juagri.shared.ui.navigation

import androidx.compose.runtime.Composable
import com.arkivanov.decompose.ExperimentalDecomposeApi
import com.arkivanov.decompose.extensions.compose.jetbrains.stack.animation.predictiveBackAnimation
import com.arkivanov.decompose.extensions.compose.jetbrains.stack.animation.slide
import com.arkivanov.decompose.extensions.compose.jetbrains.stack.animation.stackAnimation
import com.arkivanov.decompose.router.stack.pop
import com.arkivanov.decompose.router.stack.push
import com.arkivanov.decompose.router.stack.replaceAll
import com.arkivanov.decompose.router.stack.replaceCurrent
import com.juagri.shared.ui.home.HomeScreen
import com.juagri.shared.ui.login.LoginScreen
import com.juagri.shared.ui.login.OTPScreen
import com.juagri.shared.ui.splash.SplashScreen
import io.github.xxfast.decompose.router.LocalRouterContext
import io.github.xxfast.decompose.router.Router
import io.github.xxfast.decompose.router.content.RoutedContent
import io.github.xxfast.decompose.router.rememberRouter

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
            AppInitScreens.Splash -> SplashScreen { isAlreadyLoggedIn ->
                if (isAlreadyLoggedIn) {
                    router.replaceAll(AppInitScreens.Home)
                } else {
                    router.replaceCurrent(AppInitScreens.Login)
                }
            }
            AppInitScreens.Login -> LoginScreen { otp->
                otp?.let {
                    if(it == "00000"){
                        router.replaceAll(AppInitScreens.Home)
                    }else {
                        router.push(AppInitScreens.OTP(it))
                    }
                }?: router.pop()
            }
            is AppInitScreens.OTP -> OTPScreen(screen.otp) { validUser ->
                if (validUser) {
                    router.replaceAll(AppInitScreens.Home)
                } else {
                    router.pop()
                }
            }
            AppInitScreens.Home -> HomeScreen {
                router.pop()
                println("MyAppTestLogs: AppInitScreens.Home")
            }
        }
    }
}
