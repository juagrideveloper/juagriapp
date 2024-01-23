package com.juagri.shared.ui.home

import com.juagri.shared.domain.usecase.EmployeeUseCase
import com.juagri.shared.utils.UIState
import com.juagri.shared.data.local.session.SessionPreference
import com.juagri.shared.ui.components.base.BaseViewModel
import com.juagri.shared.data.local.session.datamanager.DataManager
import com.juagri.shared.domain.model.employee.JUEmployee
import com.juagri.shared.utils.ResponseState
import com.juagri.shared.utils.value
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import moe.tlaster.precompose.viewmodel.viewModelScope

class HomeViewModel(
    private val dataManager: DataManager,
    private val session: SessionPreference,
    private val employeeUseCase: EmployeeUseCase
): BaseViewModel(session,dataManager) {
    private var _employee: MutableStateFlow<UIState<JUEmployee>> =
        MutableStateFlow(UIState.Loading())
    val employee = _employee.asStateFlow()
    fun getEmployeeDetails() {
        viewModelScope.launch {
            employeeUseCase.getEmployeeDetails(session.empMobile()).collect { response ->
                when(response) {
                    is ResponseState.Loading -> _employee.value = UIState.Loading(response.isLoading)
                    is ResponseState.Success -> _employee.value = UIState.Success(response.data)
                    is ResponseState.Error -> _employee.value = UIState.Error(response.e?.message.value())
                }
            }
        }
    }
}