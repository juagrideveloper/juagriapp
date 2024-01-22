package com.juagri.shared.ui.components.base

import kotlinx.coroutines.launch
import moe.tlaster.precompose.viewmodel.ViewModel
import moe.tlaster.precompose.viewmodel.viewModelScope

open class BaseViewModel: ViewModel() {

    fun writeLog(msg: String){
        println("12312312312: $msg")
    }

    fun withScope(block:suspend ()->Unit){
        viewModelScope.launch {
            block.invoke()
        }
    }
}