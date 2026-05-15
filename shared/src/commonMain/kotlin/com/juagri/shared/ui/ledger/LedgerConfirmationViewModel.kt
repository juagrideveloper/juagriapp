package com.juagri.shared.ui.ledger

import com.juagri.shared.data.local.session.SessionPreference
import com.juagri.shared.data.local.session.datamanager.DataManager
import com.juagri.shared.domain.model.ledger.OsConfirmConfig
import com.juagri.shared.domain.model.ledger.OsConfirmCustomer
import com.juagri.shared.domain.model.login.OTPResponse
import com.juagri.shared.domain.usecase.OTPUseCase
import com.juagri.shared.domain.usecase.OsConfirmUseCase
import com.juagri.shared.ui.components.base.BaseViewModel
import com.juagri.shared.utils.UIState
import com.juagri.shared.utils.toDDMMYYYY
import com.juagri.shared.utils.value
import io.ktor.util.date.GMTDate
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class LedgerConfirmationViewModel(
    private val session: SessionPreference,
    dataManager: DataManager,
    private val osConfirmUseCase: OsConfirmUseCase,
    private val otpUseCase: OTPUseCase
) : BaseViewModel(session, dataManager) {
    private val _osConfirmConfig: MutableStateFlow<UIState<OsConfirmConfig?>> =
        MutableStateFlow(UIState.Init)
    val osConfirmConfig = _osConfirmConfig.asStateFlow()

    private val _osConfirmCustomer: MutableStateFlow<UIState<OsConfirmCustomer?>> =
        MutableStateFlow(UIState.Init)
    val osConfirmCustomer = _osConfirmCustomer.asStateFlow()

    private val _osConfirmUpdate: MutableStateFlow<UIState<Boolean>> =
        MutableStateFlow(UIState.Init)
    val osConfirmUpdate = _osConfirmUpdate.asStateFlow()

    private val _otpResponse: MutableStateFlow<UIState<OTPResponse>> =
        MutableStateFlow(UIState.Init)
    val otpResponse = _otpResponse.asStateFlow()

    fun loadOsConfirmData() {
        backgroundScope {
            osConfirmUseCase.getOsConfirmConfig().collect { response ->
                uiScope(response, _osConfirmConfig)
            }
        }
        backgroundScope {
            val ccode = empCode()
            if (ccode.isNotBlank()) {
                osConfirmUseCase.getOsConfirmCustomer(ccode).collect { response ->
                    uiScope(response, _osConfirmCustomer)
                }
            } else {
                _osConfirmCustomer.value = UIState.Success(OsConfirmCustomer())
            }
        }
    }

    fun updateOsConfirmStatus(status: Int, comments: String) {
        val employee = getJUEmployee()
        if (employee == null) {
            showErrorMessage("Employee details not found.")
            return
        }
        val ccode = (osConfirmCustomer.value as? UIState.Success)?.data?.ccode.value()
            .ifBlank { empCode() }
        if (ccode.isBlank()) {
            showErrorMessage("Customer code not found.")
            return
        }
        backgroundScope {
            osConfirmUseCase.updateOsConfirmStatus(ccode, status, employee, comments).collect { response ->
                uiScope(response, _osConfirmUpdate)
                if (response is com.juagri.shared.utils.ResponseState.Success) {
                    showSuccessMessage("Status updated.")
                }
            }
        }
    }

    fun sendOtp() {
        val emp = getJUEmployee() ?: return
        val otpMobile = "${emp.mobile.value()}_" + GMTDate().toDDMMYYYY()
        if (session.isEligibleForSendOTP(otpMobile)) {
            session.setOTPCount(otpMobile)
            backgroundScope {
                otpUseCase.sendOTP(emp).collect { response ->
                    uiScope(response, _otpResponse)
                }
            }
        } else {
            _otpResponse.value = UIState.Error("OTP limit reached (5/day). Try again tomorrow or contact support.")
        }
    }

    fun resetOtp() {
        _otpResponse.value = UIState.Init
    }

    private fun empCode() = getJUEmployee()?.code.value()
}
