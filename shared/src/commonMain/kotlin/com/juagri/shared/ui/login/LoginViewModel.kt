package com.juagri.shared.ui.login

import com.juagri.shared.JUDatabase
import com.juagri.shared.com.juagri.shared.data.local.dao.AllData
import com.juagri.shared.com.juagri.shared.data.local.dao.getAll
import com.juagri.shared.com.juagri.shared.data.local.dao.getTable1
import com.juagri.shared.com.juagri.shared.data.local.dao.getTable2
import com.juagri.shared.com.juagri.shared.ui.components.base.BaseViewModel
import com.juagri.shared.data.local.session.datamanager.DataManager
import com.juagri.shared.domain.LoginUseCase
import com.juagri.shared.domain.model.employee.JUEmployee
import com.juagri.shared.utils.ResponseState
import com.juagri.shared.utils.value
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import moe.tlaster.precompose.viewmodel.viewModelScope

class LoginViewModel(private val loginUseCase: LoginUseCase, val dataManager: DataManager,val localDatabase: JUDatabase): BaseViewModel() {
    private var _employee = MutableStateFlow(EmployeeUIState())
    val employee = _employee.asStateFlow()
    private var _tableData = MutableStateFlow(TableUIState())
    val tableData = _tableData.asStateFlow()
    init {
        withScope{
           /* localDatabase.deleteTable1()
            localDatabase.deleteTable2()
            localDatabase.setTable1()
            localDatabase.setTable2()*/
        }
    }

    fun getTable1(){
        withScope {
            val value = localDatabase.getTable1().first()
            tableData.value.data1 = value
            writeLog(value)
        }
    }

    fun getTable2(){
        withScope {
            val value = localDatabase.getTable2().first()
            tableData.value.data2 = value
            writeLog(value)
        }
    }

    fun getAll(){
        withScope {
            val value = localDatabase.getAll().first()
            tableData.value.data3 = value
            writeLog(value.title1 +"---"+value.title2)
        }
    }

    fun getEmployeeDetails(mobileNo: String){
        viewModelScope.launch {
            loginUseCase.getEmployeeDetails(mobileNo).collect{response->
                when(response){
                    is ResponseState.Loading -> {
                        writeLog("Loading: "+response.isLoading)
                        _employee.value.isLoading = response.isLoading
                    }
                    is ResponseState.Success -> {
                        writeLog("Employee Name: "+response.data.cname)
                        _employee.value.data = response.data
                    }
                    is ResponseState.Error ->{
                        writeLog("Error: "+response.e?.message)
                        _employee.value.error = response.e?.message
                    }
                }
                println("12312312312: getEmployeeDetails"+ _employee.value)
            }
        }
    }
}

data class EmployeeUIState(
    var isLoading: Boolean = false,
    var data: JUEmployee? = null,
    var error: String? = null
)

data class TableUIState(
    var data1: String? = null,
    var data2: String? = null,
    var data3: AllData? = null,
    var error: String? = null
){
    fun getAllData(): String = data3?.let {
        it.title1.value() +"-"+it.title2.value()
    }?: ""
}