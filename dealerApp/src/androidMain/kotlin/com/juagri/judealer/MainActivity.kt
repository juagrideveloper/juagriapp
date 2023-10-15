package com.juagri.judealer

import AppTheme
import MainView
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.Surface
import androidx.compose.runtime.CompositionLocalProvider
import androidx.core.view.WindowCompat
import io.github.xxfast.decompose.router.LocalRouterContext
import io.github.xxfast.decompose.router.RouterContext
import io.github.xxfast.decompose.router.defaultRouterContext

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        WindowCompat.setDecorFitsSystemWindows(window, false)
        val rootRouterContext: RouterContext = defaultRouterContext()

        setContent {
            Surface {
                CompositionLocalProvider(LocalRouterContext provides rootRouterContext) {
                    AppTheme {
                        MainView()
                    }
                }
            }
        }
    }
}