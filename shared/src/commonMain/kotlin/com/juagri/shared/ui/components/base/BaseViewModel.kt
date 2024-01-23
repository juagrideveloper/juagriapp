package com.juagri.shared.ui.components.base

import com.juagri.shared.data.local.session.SessionPreference
import com.juagri.shared.data.local.session.datamanager.DataManager
import kotlinx.coroutines.launch
import moe.tlaster.precompose.viewmodel.ViewModel
import moe.tlaster.precompose.viewmodel.viewModelScope

open class BaseViewModel(private val session: SessionPreference,private val dataManager: DataManager): ViewModel() {

    fun writeLog(msg: String){
        println("MyAppTestLogs: $msg")
    }

    fun withScope(block:suspend ()->Unit){
        viewModelScope.launch {
            block.invoke()
        }
    }

    fun isAlreadyLoggedIn() = session.isAlreadyLoggedIn()

    fun getRoleID() = session.empRoleId()
}