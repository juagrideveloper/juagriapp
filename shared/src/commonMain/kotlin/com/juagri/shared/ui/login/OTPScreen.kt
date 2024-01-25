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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.juagri.shared.com.juagri.shared.ui.components.layouts.ScreenLayout
import com.juagri.shared.ui.components.fields.ButtonNormal
import com.juagri.shared.ui.components.fields.OTPView
import com.juagri.shared.ui.components.fields.SpaceMedium
import com.juagri.shared.ui.components.fields.SpaceSmall
import com.juagri.shared.ui.components.fields.TextMedium
import com.juagri.shared.ui.components.fields.TextTitle
import com.juagri.shared.ui.components.layouts.LoginLayout
import com.juagri.shared.ui.components.layouts.ScreenLayoutWithoutActionBar
import com.juagri.shared.utils.UIState
import com.juagri.shared.utils.value
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import moe.tlaster.precompose.koin.koinViewModel
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.painterResource

@OptIn(ExperimentalResourceApi::class)
@Composable
fun OTPScreen(otpValue: String,onNext: (validUser: Boolean) -> Unit) {
    val viewModel = koinViewModel(LoginViewModel::class)
    ScreenLayoutWithoutActionBar(title = "Login", onBackPressed = {
        onNext.invoke(false)
    }) {
        ScreenLayout(viewModel) {
            val otp = remember { mutableStateOf("") }
            val validOTP = remember { mutableStateOf(otpValue) }
            when (val result = viewModel.otpResponse.collectAsState().value) {
                is UIState.Success -> {
                    result.data.let {
                        validOTP.value = it.otp
                    }
                }
                else -> {}
            }
            val showTimerText = remember { mutableStateOf(-1) }
            val enableResendOTP = remember { mutableStateOf(false) }
            val sendOTP = remember { mutableStateOf(false) }
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
                    OTPView(
                        codeLength = 6,
                        initialCode = otp.value,
                        onTextChanged = {
                            otp.value = it
                        }
                    )
                    SpaceSmall()
                    ButtonNormal("Submit") {
                        if (otp.value == validOTP.value) {
                            viewModel.storeUserDetails()
                            onNext.invoke(true)
                        } else {
                            viewModel.showErrorMessage("Please enter valid OTP!")
                        }
                    }
                    SpaceSmall()
                    if(!enableResendOTP.value) {
                        val coroutineScope = rememberCoroutineScope()
                        coroutineScope.launch {
                            for (i in 0..30) {
                                delay(1000)
                                if (i > 19) {
                                    showTimerText.value = 30 - i
                                }
                            }
                            showTimerText.value = -1
                            enableResendOTP.value = true
                        }
                    }
                    if (showTimerText.value>-1 && showTimerText.value < 11){
                        TextMedium("Resend OTP will be enabled in ${showTimerText.value} seconds")
                    }
                    if (enableResendOTP.value) {
                        ButtonNormal("Resend OTP") {
                            sendOTP.value = true
                            enableResendOTP.value = false
                        }
                    }
                }
            }
            if (sendOTP.value) {
                sendOTP.value = false
                viewModel.sendOTP()
            }
        }
    }
}
