package com.juagri.shared.ui.home

import com.juagri.shared.data.local.session.SessionPreference
import com.juagri.shared.data.local.session.datamanager.DataManager
import com.juagri.shared.domain.model.app.AppConfig
import com.juagri.shared.domain.model.employee.JUEmployee
import com.juagri.shared.domain.usecase.AppConfigUseCase
import com.juagri.shared.domain.usecase.EmployeeUseCase
import com.juagri.shared.ui.components.base.BaseViewModel
import com.juagri.shared.utils.UIState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class HomeViewModel(
    private val dataManager: DataManager,
    private val session: SessionPreference,
    private val employeeUseCase: EmployeeUseCase,
    private val appConfigUseCase: AppConfigUseCase
): BaseViewModel(session,dataManager) {

    private var _appConfig: MutableStateFlow<UIState<AppConfig>> =
        MutableStateFlow(UIState.Init)
    val appConfig = _appConfig.asStateFlow()

    private var _employee: MutableStateFlow<UIState<JUEmployee>> =
        MutableStateFlow(UIState.Init)
    val employee = _employee.asStateFlow()

    fun resetAppUpdate(){
        _appConfig.value = UIState.Init
    }

    fun getAppConfig() {
        backgroundScope {
            appConfigUseCase.getAppConfig().collect { response ->
                uiScope(response,_appConfig)
            }
        }
    }

    fun getEmployeeDetails() {
        backgroundScope {
            employeeUseCase.getEmployeeDetails(session.empMobile(), true).collect { response ->
                uiScope(response,_employee)
            }
        }
    }

}