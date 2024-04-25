package com.juagri.shared.ui.login

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.juagri.shared.ui.components.dialogs.ConfirmDialog
import com.juagri.shared.ui.components.fields.ButtonNormal
import com.juagri.shared.ui.components.fields.RowSpaceMedium
import com.juagri.shared.ui.components.fields.RowSpaceSmall
import com.juagri.shared.ui.components.fields.TextMedium
import com.juagri.shared.ui.components.fields.TextTitle
import com.juagri.shared.ui.components.layouts.LoginLayout
import com.juagri.shared.ui.components.layouts.ScreenLayout
import com.juagri.shared.ui.components.layouts.ScreenLayoutWithoutActionBar
import com.juagri.shared.utils.UIState
import com.juagri.shared.utils.value
import moe.tlaster.precompose.koin.koinViewModel
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.painterResource

@OptIn(ExperimentalResourceApi::class)
@Composable
fun LoginScreen(onNext: (String?) -> Unit) {

    val viewModel = koinViewModel(LoginViewModel::class)
    val showConfirmDialog = remember { mutableStateOf(false) }
    val confirmDialogContent = remember { mutableStateOf("") }
    viewModel.apply {
        ScreenLayoutWithoutActionBar(onBackPressed = {
            onNext.invoke(null)
        }) {
            val focusManager = LocalFocusManager.current
            ScreenLayout(this, false, enableBGColor = false) {
                val mobileNo = remember { mutableStateOf("") }
                Box {
                    LoginLayout()
                    Column(
                        modifier = Modifier.fillMaxWidth().wrapContentSize()
                            .padding(start = 24.dp, end = 24.dp)
                            .padding(top = 80.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Image(
                            painterResource(DrawableResource("icon_telephone.xml")),
                            null,
                            modifier = Modifier.height(150.dp).width(100.dp)
                        )
                        RowSpaceMedium()
                        TextTitle(names().mobileNumber)
                        RowSpaceMedium()
                        TextMedium(
                            textAlign = TextAlign.Center,
                            text = "Please enter your registered phone number, you will receive an OTP."
                        )
                        RowSpaceMedium()
                        Row(
                            modifier = Modifier.padding(start = 24.dp, end = 24.dp)
                                .background(Color.Transparent)
                        ) {

                            TextField(
                                value = "+91",
                                onValueChange = {},
                                modifier = Modifier
                                    .width(65.dp)
                                    .padding(vertical = 4.dp).border(
                                        shape = RectangleShape, border = BorderStroke(
                                            (0.5).dp,
                                            Color.LightGray
                                        )
                                    ),
                                colors = TextFieldDefaults.colors(
                                    focusedContainerColor = Color.White,
                                    unfocusedContainerColor = Color.White,
                                    focusedIndicatorColor = Color.Transparent,
                                    disabledContainerColor = Color.White,
                                    unfocusedIndicatorColor = Color.Transparent,
                                    disabledTextColor = Color.Black
                                ),
                                enabled = false
                            )
                            TextField(
                                value = mobileNo.value,
                                onValueChange = {
                                    if (it.length <= 10) {
                                        mobileNo.value = it
                                    } else {
                                        showConfirmDialog.value = false
                                    }
                                },
                                keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 4.dp).border(
                                        shape = RectangleShape, border = BorderStroke(
                                            1.dp,
                                            Color.LightGray
                                        )
                                    ),

                                colors = TextFieldDefaults.colors(
                                    focusedContainerColor = Color.White,
                                    unfocusedContainerColor = Color.White,
                                    focusedIndicatorColor = Color.Transparent,
                                )
                            )

                        }


                        when (val result = employee.collectAsState().value) {
                            is UIState.Success -> {
                                result.data.let {
                                    setJUEmployee(it)
                                    confirmDialogContent.value =
                                        "Hi, " + it.name.value() + "!\nShall we proceed with +91" + mobileNo.value + " mobile number?"
                                    showConfirmDialog.value = true
                                    reset()
                                }
                            }

                            else -> {}
                        }

                        RowSpaceSmall()
                        ButtonNormal("Submit") {
                            focusManager.clearFocus(true)
                            if (mobileNo.value.length == 10) {
                                getEmployeeDetails(mobileNo.value)
                            } else {
                                showErrorMessage("Please enter valid mobile no!")
                            }
                        }
                        when (val result = otpResponse.collectAsState().value) {
                            is UIState.Success -> {
                                result.data.let {
                                    onNext.invoke(it.otp)
                                }
                            }

                            else -> {}
                        }
                        ConfirmDialog(
                            showConfirmDialog,
                            "Confirm",
                            confirmDialogContent.value
                        ) {
                            showConfirmDialog.value = false
                            sendOTP()
                            //storeUserDetails()
                            //onNext.invoke("00000")
                        }
                    }
                }
            }

        }
    }
}