package com.juagri.shared.ui.ledger

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.juagri.shared.ui.components.dialogs.SuccessDialog
import com.juagri.shared.ui.components.fields.ButtonNormal
import com.juagri.shared.ui.components.fields.ColumnSpaceSmall
import com.juagri.shared.ui.components.fields.OTPView
import com.juagri.shared.ui.components.fields.RowSpaceSmall
import com.juagri.shared.ui.components.fields.TextDropdown
import com.juagri.shared.ui.components.fields.TextMedium
import com.juagri.shared.ui.components.fields.TextSmall
import com.juagri.shared.ui.components.layouts.CardLayout
import com.juagri.shared.ui.components.layouts.ScreenLayout
import com.juagri.shared.ui.components.layouts.ScreenLayoutWithoutActionBar
import com.juagri.shared.utils.Constants
import com.juagri.shared.utils.UIState
import com.juagri.shared.utils.getButtonGradient
import com.juagri.shared.utils.getColors
import com.juagri.shared.utils.getIndianCurrencyFormat
import moe.tlaster.precompose.koin.koinViewModel

@Composable
fun LedgerConfirmationScreen(onBack: ()-> Unit) {
    val viewModel = koinViewModel(LedgerConfirmationViewModel::class)
    viewModel.setScreenId(Constants.SCREEN_LEDGER_CONFIRMATION)
    val showSuccessDialog = remember { mutableStateOf(false) }
    val financialPeriod = remember { mutableStateOf("-") }
    val osAmount = remember { mutableStateOf("0") }
    val comments = remember { mutableStateOf("") }
    val notes = remember { mutableStateOf("") }
    val osMonthAsOn = remember { mutableStateOf("") }
    val isEditable = remember { mutableStateOf(true) }
    var successDialogMessage = "Your OS related comments has been updated successfully..."

    val configState = viewModel.osConfirmConfig.collectAsState().value
    val customerState = viewModel.osConfirmCustomer.collectAsState().value
    val osConfirmUpdate = viewModel.osConfirmUpdate.collectAsState().value
    val otpState = viewModel.otpResponse.collectAsState().value

    val showOtpDialog = remember { mutableStateOf(false) }
    val otpInput = remember { mutableStateOf("") }
    val validOtp = remember { mutableStateOf("") }
    val pendingStatus = remember { mutableStateOf<Int?>(null) }

    LaunchedEffect(Unit) {
        viewModel.resetOtp()
        viewModel.loadOsConfirmData()
    }

    LaunchedEffect(configState) {
        if (configState is UIState.Success) {
            configState.data?.let { config ->
                financialPeriod.value = config.finPeriod.ifBlank { "-" }
                notes.value = config.notes
                isEditable.value = config.editable
            }
        }
    }

    LaunchedEffect(customerState) {
        if (customerState is UIState.Success) {
            if (customerState.data?.status == 0 || customerState.data?.status == 1) {
                showSuccessDialog.value = true
                successDialogMessage = "Already you have submitted your OS related comments..."
            }
            customerState.data?.let { customer ->
                osAmount.value = if (customer.totalos % 1.0 == 0.0) {
                    customer.totalos.toInt().toString()
                } else {
                    customer.totalos.toString()
                }
                osMonthAsOn.value = if (customer.osmonth.isNotBlank() || customer.osdate.isNotBlank()) {
                    "${customer.osmonth} as on (${customer.osdate})"
                } else ""
            }
        }
    }

    LaunchedEffect(osConfirmUpdate) {
        if (osConfirmUpdate is UIState.Success) {
            showSuccessDialog.value = true
        }
    }

    LaunchedEffect(otpState) {
        when (otpState) {
            is UIState.Success -> {
                validOtp.value = otpState.data.otp
            }
            is UIState.Error -> {
                viewModel.showErrorMessage(otpState.error)
                showOtpDialog.value = false
            }
            else -> {}
        }
    }

    ScreenLayoutWithoutActionBar {
        ScreenLayout(viewModel) {
            Column(modifier = Modifier.fillMaxSize()) {
                CardLayout {
                    // Financial Period & OS Amount table
                    Row(modifier = Modifier.fillMaxWidth()) {
                        TextDropdown(
                            text = financialPeriod.value,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.weight(0.6f)
                                .fillMaxWidth()
                                .wrapContentHeight()
                                .border(
                                    BorderStroke(0.1.dp, getColors().onBackground),
                                    RoundedCornerShape(0.dp)
                                )
                                .padding(8.dp)
                        )
                        TextDropdown(
                            text = "OS Amount",
                            textAlign = TextAlign.Center,
                            modifier = Modifier.weight(0.4f)
                                .fillMaxWidth()
                                .wrapContentHeight()
                                .border(
                                    BorderStroke(0.1.dp, getColors().onBackground),
                                    RoundedCornerShape(0.dp)
                                )
                                .padding(8.dp)
                        )
                    }
                    Row(modifier = Modifier.fillMaxWidth()) {
                        TextDropdown(
                            text = osMonthAsOn.value,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.weight(0.6f)
                                .fillMaxWidth()
                                .wrapContentHeight()
                                .border(
                                    BorderStroke(0.1.dp, getColors().onBackground),
                                    RoundedCornerShape(0.dp)
                                )
                                .padding(16.dp)
                        )
                        TextDropdown(
                            text = getIndianCurrencyFormat(osAmount.value),
                            textAlign = TextAlign.Center,
                            modifier = Modifier.weight(0.4f)
                                .fillMaxWidth()
                                .wrapContentHeight()
                                .border(
                                    BorderStroke(0.1.dp, getColors().onBackground),
                                    RoundedCornerShape(0.dp)
                                )
                                .padding(16.dp)
                        )
                    }
                }

                ColumnSpaceSmall()

                // Comments
                CardLayout {
                    OutlinedTextField(
                        value = comments.value,
                        onValueChange = { comments.value = it },
                        label = { TextDropdown("Comments") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(100.dp),
                        enabled = isEditable.value,
                        minLines = 4,
                        maxLines = 6,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = getColors().primary,
                            unfocusedBorderColor = getColors().primary,
                            focusedLabelColor = getColors().onBackground,
                            unfocusedLabelColor = getColors().onBackground,
                            cursorColor = getColors().primary,
                            focusedTextColor = getColors().onBackground,
                            unfocusedTextColor = getColors().onBackground
                        )
                    )
                }

                ColumnSpaceSmall()

                // CONFIRM and NOT CONFIRM buttons
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Button(
                        onClick = {
                            if (comments.value.isNotEmpty()) {
                                pendingStatus.value = 1
                                otpInput.value = ""
                                validOtp.value = ""
                                showOtpDialog.value = true
                                viewModel.sendOtp()
                            } else {
                                viewModel.showErrorMessage("Please enter valid comments!")
                            }
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
                        contentPadding = PaddingValues(),
                        modifier = Modifier.weight(1f)
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(getButtonGradient(true), RoundedCornerShape(5.dp))
                                .border(1.dp, getColors().primary, RoundedCornerShape(5.dp))
                                .padding(horizontal = 24.dp, vertical = 12.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            TextMedium(text = "CONFIRM", color = getColors().background)
                        }
                    }
                    RowSpaceSmall()
                    Button(
                        onClick = {
                            if (comments.value.isNotEmpty()) {
                                pendingStatus.value = 0
                                otpInput.value = ""
                                validOtp.value = ""
                                showOtpDialog.value = true
                                viewModel.sendOtp()
                            } else {
                                viewModel.showErrorMessage("Please enter valid comments!")
                            }
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
                        contentPadding = PaddingValues(),
                        modifier = Modifier.weight(1f)
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(Color.Gray, RoundedCornerShape(5.dp))
                                .border(1.dp, getColors().primary, RoundedCornerShape(5.dp))
                                .padding(horizontal = 24.dp, vertical = 12.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            TextMedium(text = "NOT CONFIRM", color = Color.White)
                        }
                    }
                }

                ColumnSpaceSmall()

                // Large empty bordered box (placeholder for list/details)
                CardLayout (fullHeight = true) {
                    if (notes.value.isNotBlank()) {
                        TextMedium(
                            text = notes.value,
                            modifier = Modifier.fillMaxWidth()
                                .fillMaxHeight(),
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }
        }
    }

    SuccessDialog(
        showSuccessDialog,
        title = "Success",
        successDialogMessage
    ) {
        onBack()
    }

    if (showOtpDialog.value) {
        Dialog(
            onDismissRequest = {},
            properties = DialogProperties(usePlatformDefaultWidth = false)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color.White, RoundedCornerShape(16.dp))
                        .padding(16.dp)
                ) {
                    TextMedium("OTP Verification", textAlign = TextAlign.Center)
                    ColumnSpaceSmall()
                    TextSmall(
                        "OTP has been sent to your registered mobile number. Please enter the OTP.",
                        textAlign = TextAlign.Start
                    )
                    ColumnSpaceSmall()
                    OTPView(codeLength = 6, initialCode = otpInput.value) {
                        otpInput.value = it
                    }
                    ColumnSpaceSmall()
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        ButtonNormal("Cancel") {
                            showOtpDialog.value = false
                        }
                        ButtonNormal("Verify") {
                            if (otpInput.value == validOtp.value && otpInput.value.isNotBlank()) {
                                showOtpDialog.value = false
                                pendingStatus.value?.let { viewModel.updateOsConfirmStatus(it, comments.value) }
                            } else {
                                viewModel.showErrorMessage("Please enter valid OTP!")
                            }
                        }
                    }
                }
            }
        }
    }
}
