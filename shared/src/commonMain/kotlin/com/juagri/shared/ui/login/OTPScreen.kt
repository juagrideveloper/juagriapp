package com.juagri.shared.ui.login

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Snackbar
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.juagri.shared.com.juagri.shared.utils.UIState
import com.juagri.shared.ui.components.fields.ButtonNormal
import com.juagri.shared.ui.components.fields.OTPView
import com.juagri.shared.ui.components.fields.SpaceMedium
import com.juagri.shared.ui.components.fields.SpaceSmall
import com.juagri.shared.ui.components.fields.TextMedium
import com.juagri.shared.ui.components.fields.TextTitle
import com.juagri.shared.ui.components.layouts.LoginLayout
import com.juagri.shared.ui.components.layouts.ScreenLayoutWithoutActionBar
import com.juagri.shared.utils.ResponseState
import com.juagri.shared.utils.value
import moe.tlaster.precompose.koin.koinViewModel
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.painterResource

@OptIn(ExperimentalResourceApi::class)
@Composable
fun OTPScreen(onNext: (validUser: Boolean) -> Unit) {
    val viewModel = koinViewModel(LoginViewModel::class)
    ScreenLayoutWithoutActionBar(title = "Login", onBackPressed = {
        onNext.invoke(false)
    }) {
        val showSnackBar = remember { mutableStateOf(false) }
        val enableLoading = remember { mutableStateOf(false) }
        val otp = remember { mutableStateOf("") }
        val validOTP = remember { mutableStateOf("") }
        when(val result = viewModel.otpResponse.collectAsState().value){
            is UIState.Loading -> enableLoading.value = result.isLoading
            is UIState.Success -> {
                result.data.let {
                    validOTP.value = it.otp
                }
            }
            else -> {}
        }
        val enableResendOTP = remember { mutableStateOf(false) }
        val sendOTP = remember { mutableStateOf(true) }
        if(sendOTP.value) {
            viewModel.sendOTP()
            sendOTP.value = false
        }
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
                        onTextChanged = {
                            otp.value = it
                            showSnackBar.value = false
                        }
                    )
                //}
                /*if(!enableResendOTP.value) {
                    enableResendOTP.value = true
                }*/
                SpaceSmall()
                ButtonNormal("Submit") {
                    if(otp.value == validOTP.value){
                        showSnackBar.value = false
                        viewModel.storeUserDetails()
                        onNext.invoke(true)
                    } else {
                        showSnackBar.value = true
                    }
                }
                SpaceSmall()
                if(enableResendOTP.value) {
                    ButtonNormal("Resend OTP") {
                        sendOTP.value = true
                    }
                }
                if(showSnackBar.value) {
                    Snackbar(
                        containerColor = Color.Red,
                        content = { Text(text = "Please enter OTP!", color = Color.White) },
                        shape = RoundedCornerShape(4.dp)
                    )
                }
            }
        }
    }
}