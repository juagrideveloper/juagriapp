package com.juagri.shared.ui.login

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.juagri.shared.com.juagri.shared.ui.components.fields.ButtonNormal
import com.juagri.shared.com.juagri.shared.ui.components.fields.OTPView
import com.juagri.shared.com.juagri.shared.ui.components.fields.SpaceMedium
import com.juagri.shared.com.juagri.shared.ui.components.fields.SpaceSmall
import com.juagri.shared.com.juagri.shared.ui.components.fields.TextMedium
import com.juagri.shared.com.juagri.shared.ui.components.fields.TextTitle
import com.juagri.shared.com.juagri.shared.ui.components.layouts.LoginLayout
import com.juagri.shared.com.juagri.shared.ui.components.layouts.ScreenLayoutWithoutActionBar
import moe.tlaster.precompose.koin.koinViewModel
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.painterResource

@OptIn(ExperimentalResourceApi::class)
@Composable
fun OTPScreen(onBack: () -> Unit) {
    val viewModel = koinViewModel(LoginViewModel::class)
    ScreenLayoutWithoutActionBar(title = "Login", onBackPressed = {
        onBack.invoke()
    }) {
        val mobileNo = remember { mutableStateOf("") }
        val enableResendOTP = remember { mutableStateOf(false) }
        val otp = remember { mutableStateOf("") }
        Box {
            LoginLayout()
            Column(
                modifier = Modifier.fillMaxWidth().wrapContentSize()
                    .padding(start = 24.dp, end = 24.dp)
                    .padding(top = 80.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Image(
                    painterResource("icon_mobile.xml"),
                    null,
                    modifier = Modifier.height(120.dp).width(90.dp),
                    contentScale = ContentScale.Fit
                )
                SpaceMedium()
                TextTitle("OTP Verification")
                SpaceMedium()
                TextMedium(
                    textAlign = TextAlign.Center,
                    text = "OTP has been sent to your registered mobile number, Please enter the OTP."
                )
                SpaceMedium()
                /*Row(
                    modifier = Modifier.padding(start = 32.dp, end = 32.dp)
                        .background(Color.Transparent).padding(vertical = 4.dp).border(
                            shape = RectangleShape, border = BorderStroke(
                                (0.5).dp,
                                Color.LightGray
                            )
                        )
                ) {*/
                    OTPView(
                        codeLength = 6,
                        initialCode = otp.value,
                        onTextChanged = { otp.value = it }
                    )
                //}
                SpaceSmall()
                ButtonNormal("Submit") {
                    enableResendOTP.value = !enableResendOTP.value
                }
                SpaceSmall()
                if(enableResendOTP.value) {
                    ButtonNormal("Resend OTP") {}
                }
            }
        }
    }
}