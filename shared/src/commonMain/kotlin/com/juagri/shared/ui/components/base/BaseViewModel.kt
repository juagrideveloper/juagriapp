package com.juagri.shared.ui.components.base

import androidx.compose.runtime.mutableStateOf
import com.juagri.shared.com.juagri.shared.ui.components.layouts.MessageData
import com.juagri.shared.data.local.session.SessionPreference
import com.juagri.shared.data.local.session.datamanager.DataManager
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import moe.tlaster.precompose.viewmodel.ViewModel
import moe.tlaster.precompose.viewmodel.viewModelScope

open class BaseViewModel(private val session: SessionPreference,private val dataManager: DataManager): ViewModel() {

    val z0001 = mutableStateOf(false)
    val z0002 = mutableStateOf(MessageData())
    val z0003 = mutableStateOf(MessageData())
    val z0004 = mutableStateOf(MessageData())
    val z0005 = mutableStateOf(MessageData())

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

    fun showProgressBar(boolean: Boolean){
        z0001.value = boolean
    }
    fun showSuccessMessage(message: String){
        z0002.value = MessageData(message = message, enable = true)
        withScope {
            delay(1500)
            z0002.value = MessageData()
        }
    }

    fun showErrorMessage(message: String){
        z0003.value = MessageData(message = message, enable = true)
        withScope {
            delay(1500)
            z0003.value = MessageData()
        }
    }

    fun showAlertMessage(message: String){
        z0004.value = MessageData(message = message, enable = true)
        withScope {
            delay(1500)
            z0004.value = MessageData()
        }
    }

    fun showNormalMessage(message: String){
        z0005.value = MessageData(message = message, enable = true)
        withScope {
            delay(1500)
            z0005.value = MessageData()
        }
    }
}