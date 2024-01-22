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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Snackbar
import androidx.compose.material3.Text
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
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.juagri.shared.ui.components.dialogs.ConfirmDialog
import com.juagri.shared.utils.UIState
import com.juagri.shared.ui.components.dialogs.ProgressDialog
import com.juagri.shared.ui.components.fields.ButtonNormal
import com.juagri.shared.ui.components.fields.SpaceMedium
import com.juagri.shared.ui.components.fields.SpaceSmall
import com.juagri.shared.ui.components.fields.TextMedium
import com.juagri.shared.ui.components.fields.TextTitle
import com.juagri.shared.ui.components.layouts.LoginLayout
import com.juagri.shared.ui.components.layouts.ScreenLayoutWithoutActionBar
import com.juagri.shared.utils.value
import moe.tlaster.precompose.koin.koinViewModel
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.painterResource

@OptIn(ExperimentalResourceApi::class)
@Composable
fun LoginScreen(onNext: () -> Unit) {

    val viewModel = koinViewModel(LoginViewModel::class)
    ScreenLayoutWithoutActionBar(title = "Login", onBackPressed = {
        onNext.invoke()
    }) {
        val mobileNo = remember { mutableStateOf("") }
        val showSnackBar = remember { mutableStateOf(false) }
        Box {
            LoginLayout()
            Column(
                modifier = Modifier.fillMaxWidth().wrapContentSize()
                    .padding(start = 24.dp, end = 24.dp)
                    .padding(top = 80.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Image(
                    painterResource("icon_telephone.xml"),
                    null,
                    modifier = Modifier.height(150.dp).width(100.dp)
                )
                SpaceMedium()
                TextTitle("Mobile Number")
                SpaceMedium()
                TextMedium(
                    textAlign = TextAlign.Center,
                    text = "Please enter your registered phone number, you will receive an OTP."
                )
                SpaceMedium()
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
                            if (it.length <= 10) { mobileNo.value = it }
                            showSnackBar.value = false
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
                val enableLoading = remember { mutableStateOf(false) }
                val showConfirmDialog = remember { mutableStateOf(false) }
                val confirmDialogContent = remember { mutableStateOf("") }

                when(val result = viewModel.employee.collectAsState().value){
                    is UIState.Loading -> enableLoading.value = result.isLoading
                    is UIState.Success -> {
                        result.data.let {
                            viewModel.updateEmployee(it)
                            confirmDialogContent.value = "Hi, " + it.name.value() + "!\nShall we proceed with +91" + mobileNo.value + " mobile number?"
                            showConfirmDialog.value = true
                        }
                    }
                    else -> {}
                }

                SpaceSmall()
                ButtonNormal("Submit") {
                    if (mobileNo.value.length == 10) {
                        showSnackBar.value = false
                        viewModel.getEmployeeDetails(mobileNo.value)
                    }else{
                        showSnackBar.value = true
                    }
                }
                ProgressDialog(enableLoading,{})
                ConfirmDialog(showConfirmDialog,"Confirm",confirmDialogContent.value, onClickYes = {
                    showConfirmDialog.value = false
                    onNext.invoke()
                }, onClickNo = {
                    showConfirmDialog.value = false
                })
                if(showSnackBar.value) {
                    Snackbar(
                        containerColor = Color.Red,
                        content = { Text(text = "Please enter valid mobile no!", color = Color.White) },
                        shape = RoundedCornerShape(4.dp)
                    )
                }
            }
        }
    }
}