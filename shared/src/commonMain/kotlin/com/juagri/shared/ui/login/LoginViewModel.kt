package com.juagri.shared.ui.login

import com.juagri.shared.data.local.session.SessionPreference
import com.juagri.shared.data.local.session.datamanager.DataManager
import com.juagri.shared.domain.model.employee.JUEmployee
import com.juagri.shared.domain.model.login.OTPResponse
import com.juagri.shared.domain.usecase.EmployeeUseCase
import com.juagri.shared.domain.usecase.OTPUseCase
import com.juagri.shared.ui.components.base.BaseViewModel
import com.juagri.shared.utils.UIState
import com.juagri.shared.utils.value
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class LoginViewModel(
    private val dataManager: DataManager,
    private val session: SessionPreference,
    private val employeeUseCase: EmployeeUseCase,
    private val otpUseCase: OTPUseCase,
) : BaseViewModel(session,dataManager) {
    private var _employee: MutableStateFlow<UIState<JUEmployee>> =
        MutableStateFlow(UIState.Init)
    val employee = _employee.asStateFlow()

    private var _otpResponse: MutableStateFlow<UIState<OTPResponse>> =
        MutableStateFlow(UIState.Init)
    val otpResponse = _otpResponse.asStateFlow()

    fun getEmployeeDetails(mobileNo: String) {
        backgroundScope{
            employeeUseCase.getEmployeeDetails(mobileNo).collect { response ->
                uiScope(response,_employee)
            }
        }
    }

    fun sendOTP() {
        dataManager.getEmployee()?.let {otp->
            backgroundScope{
                otpUseCase.sendOTP(otp).collect { response ->
                    uiScope(response,_otpResponse)
                }
            }
        }
    }

    fun storeUserDetails(){
        dataManager.getEmployee()?.let {emp->
            session.apply {
                setEmpCode(emp.code.value())
                setEmpName(emp.name.value())
                setEmpMobile(emp.mobile.value())
                setEmpRoleId(emp.roleId.value())
                setAlreadyLoggedIn(true)
            }
        }
    }
}