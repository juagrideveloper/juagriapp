package com.juagri.shared.ui.ledger

import com.juagri.shared.data.local.session.SessionPreference
import com.juagri.shared.data.local.session.datamanager.DataManager
import com.juagri.shared.domain.model.ledger.OsConfirmConfig
import com.juagri.shared.domain.model.ledger.OsConfirmCustomer
import com.juagri.shared.domain.usecase.OsConfirmUseCase
import com.juagri.shared.ui.components.base.BaseViewModel
import com.juagri.shared.utils.UIState
import com.juagri.shared.utils.value
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class LedgerConfirmationViewModel(
    private val session: SessionPreference,
    dataManager: DataManager,
    private val osConfirmUseCase: OsConfirmUseCase
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

    fun updateOsConfirmStatus(status: Int) {
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
            osConfirmUseCase.updateOsConfirmStatus(ccode, status, employee).collect { response ->
                uiScope(response, _osConfirmUpdate)
                if (response is com.juagri.shared.utils.ResponseState.Success) {
                    showSuccessMessage("Status updated.")
                }
            }
        }
    }

    private fun empCode() = "CAP-0065" //session.empCode()
}
