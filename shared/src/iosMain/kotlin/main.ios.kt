import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.compose.ui.window.ComposeUIViewController
import com.arkivanov.decompose.ExperimentalDecomposeApi
import com.arkivanov.decompose.extensions.compose.jetbrains.PredictiveBackGestureIcon
import com.arkivanov.decompose.extensions.compose.jetbrains.PredictiveBackGestureOverlay
import com.arkivanov.essenty.backhandler.BackDispatcher
import com.juagri.shared.JUAgriAppContent
import com.juagri.shared.data.local.database.DriverFactory
import com.juagri.shared.data.local.session.SessionPreference
import com.juagri.shared.di.initKoin
import io.github.xxfast.decompose.router.LocalRouterContext
import io.github.xxfast.decompose.router.RouterContext
import kotlinx.cinterop.BetaInteropApi
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.autoreleasepool
import kotlinx.cinterop.cstr
import kotlinx.cinterop.memScoped
import kotlinx.cinterop.toCValues
import moe.tlaster.precompose.PreComposeApplication
import platform.Foundation.NSStringFromClass
import platform.UIKit.UIApplicationMain
import platform.UIKit.UIViewController
import platform.darwin.NSObject

fun MainViewController() = ComposeUIViewController { JUAgriAppContent() }

@OptIn(ExperimentalForeignApi::class, BetaInteropApi::class)
fun main() {
    val args = emptyArray<String>()
    memScoped {
        val argc = args.size + 1
        val argv = (arrayOf("skikoApp") + args).map { it.cstr.ptr }.toCValues()
        autoreleasepool {
            UIApplicationMain(argc, argv, null, NSStringFromClass(AppDelegate))
        }
    }
}

@OptIn(ExperimentalDecomposeApi::class)
fun MainUIController(routerContext: RouterContext): UIViewController = PreComposeApplication {
    CompositionLocalProvider(
        LocalRouterContext provides routerContext,
    ) {
        initKoin(SessionPreference(context = NSObject()), DriverFactory().createDriver())
        PredictiveBackGestureOverlay(
            backDispatcher = routerContext.backHandler as BackDispatcher, // Use the same BackDispatcher as above
            backIcon = { progress, _ ->
                PredictiveBackGestureIcon(
                    imageVector = Icons.Default.ArrowBack,
                    progress = progress,
                )
            },
            modifier = Modifier.fillMaxSize(),
        ) {
            JUAgriAppContent()
        }
    }
}