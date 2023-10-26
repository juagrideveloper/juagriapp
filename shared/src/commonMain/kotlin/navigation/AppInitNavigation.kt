package navigation

import androidx.compose.runtime.Composable
import com.arkivanov.decompose.ExperimentalDecomposeApi
import com.arkivanov.decompose.extensions.compose.jetbrains.stack.animation.predictiveBackAnimation
import com.arkivanov.decompose.extensions.compose.jetbrains.stack.animation.slide
import com.arkivanov.decompose.extensions.compose.jetbrains.stack.animation.stackAnimation
import com.arkivanov.decompose.router.stack.pop
import com.arkivanov.decompose.router.stack.push
import io.github.xxfast.decompose.router.LocalRouterContext
import io.github.xxfast.decompose.router.Router
import io.github.xxfast.decompose.router.content.RoutedContent
import io.github.xxfast.decompose.router.rememberRouter
import ui.home.HomeScreen
import ui.login.LoginScreen
import ui.splash.SplashScreen

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
                    router.push(AppInitScreens.Login)
                } else {
                    router.push(AppInitScreens.Home)
                }
            }
            AppInitScreens.Login -> LoginScreen { router.pop() }
            AppInitScreens.Home -> HomeScreen { router.pop() }
        }
    }
}
