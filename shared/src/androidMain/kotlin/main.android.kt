import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import com.juagri.shared.JUAgriAppContent
import com.juagri.shared.data.local.database.DriverFactory
import com.juagri.shared.data.local.session.SessionContext
import com.juagri.shared.data.local.session.SessionPreference
import com.juagri.shared.di.initKoin

//@Composable fun MainView(kamelConfig: KamelConfig) = JUAgriAppContent()

@Composable fun MainView() {
    val context = LocalContext.current.applicationContext
    initKoin(
        SessionPreference(context as SessionContext),
        DriverFactory(context).createDriver()
    )
    JUAgriAppContent()
}
