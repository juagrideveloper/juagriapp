package com.juagri.shared.ui.components.base

import Constants
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import com.juagri.shared.data.local.session.SessionPreference
import com.juagri.shared.data.local.session.datamanager.DataManager
import com.juagri.shared.domain.model.employee.JUEmployee
import com.juagri.shared.domain.model.filter.FilterDataItem
import com.juagri.shared.domain.model.filter.FilterItem
import com.juagri.shared.domain.model.filter.FilterType
import com.juagri.shared.domain.model.user.FinMonth
import com.juagri.shared.domain.model.user.FinYear
import com.juagri.shared.domain.model.user.JUDealer
import com.juagri.shared.domain.model.user.JURegion
import com.juagri.shared.domain.model.user.JUTerritory
import com.juagri.shared.ui.components.layouts.MessageData
import com.juagri.shared.ui.navigation.AppScreens
import com.juagri.shared.utils.ResponseState
import com.juagri.shared.utils.UIState
import com.juagri.shared.utils.strings.AppLanguage
import com.juagri.shared.utils.value
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import moe.tlaster.precompose.viewmodel.ViewModel
import moe.tlaster.precompose.viewmodel.viewModelScope

open class BaseViewModel(private val session: SessionPreference,private val dataManager: DataManager): ViewModel() {

    val z0001 = mutableStateOf(false)
    val z0002 = mutableStateOf(MessageData())
    val z0003 = mutableStateOf(MessageData())
    val z0004 = mutableStateOf(MessageData())
    val z0005 = mutableStateOf(MessageData())

    val showDialog = mutableStateOf(FilterDataItem())
    fun names() = dataManager.names.value

    fun setScreenId(screenId: String) = dataManager.setScreenId(screenId)
    fun getScreenTitle() = dataManager.getScreenTitle()

    fun getScreenName(screenId: String) = dataManager.getScreenName(screenId)

    fun getScreenMode(screenId: String): AppScreens =
        when(screenId){
            Constants.SCREEN_DASHBOARD-> AppScreens.Dashboard
            Constants.SCREEN_LEDGER-> AppScreens.Ledger
            Constants.SCREEN_ONLINE_ORDER-> AppScreens.OnlineOrder
            Constants.SCREEN_YOUR_ORDERS-> AppScreens.YourOrders
            Constants.SCREEN_JU_Doctor-> AppScreens.JUDoctorCrop("0")
            Constants.SCREEN_WEATHER-> AppScreens.Weather
            Constants.SCREEN_PROFILE-> AppScreens.Profile
            Constants.SCREEN_DEVICES-> AppScreens.Devices
            else -> AppScreens.DummyScreen
        }

    fun setJUEmployee(employee: JUEmployee) {
        dataManager.setEmployee(employee)
    }

    fun getJUEmployee(): JUEmployee? = dataManager.getEmployee()

    fun changeLanguage(language: AppLanguage){
        dataManager.setLanguage(language)
    }

    fun writeLog(msg: String){
        println("JUAgriAppTestLogs: $msg")
    }

    fun backgroundScope(block:suspend ()->Unit){
        viewModelScope.launch(Dispatchers.IO) {
            block.invoke()
        }
    }

    suspend fun <T> uiScope(response: ResponseState<T>,mutableState: MutableStateFlow<UIState<T>>){
        withContext(Dispatchers.Main){
            when(response) {
                is ResponseState.Loading -> showProgressBar(response.isLoading)
                is ResponseState.Success -> mutableState.value = UIState.Success(response.data)
                is ResponseState.Error -> {
                    showProgressBar(false)
                    processError(response.e)
                }
            }
        }
    }

    suspend fun <T> uiScopeFilter(response: ResponseState<T>,filterType: FilterType){
        withContext(Dispatchers.Main){
            when(response) {
                is ResponseState.Loading -> showProgressBar(response.isLoading)
                is ResponseState.Success -> {
                    showDialog.value = when(filterType){
                        is FilterType.REGION -> {
                            FilterDataItem(
                                names().selectRegion,
                                (response.data as List<JURegion>) .map {
                                    FilterItem(
                                        it.regCode.value(),
                                        it.regName.value(),
                                        FilterType.REGION(it)
                                    )
                                },
                                mutableStateOf(true)
                            )
                        }
                        is FilterType.TERRITORY -> {
                                FilterDataItem(
                                names().selectTerritory,
                                (response.data as List<JUTerritory>) .map {
                                    FilterItem(
                                        it.tCode.value(),
                                        it.tName.value(),
                                        FilterType.TERRITORY(it)
                                    )
                                },
                                mutableStateOf(true)
                            )
                        }
                        is FilterType.DEALER -> {
                            FilterDataItem(
                                names().selectDealer,
                                (response.data as List<JUDealer>) .map {
                                    FilterItem(
                                        it.cCode.value(),
                                        it.cName.value(),
                                        FilterType.DEALER(it)
                                    )
                                },
                                mutableStateOf(true)
                            )
                        }
                        is FilterType.FIN_YEAR -> {
                            FilterDataItem(
                                names().selectTerritory,
                                (response.data as List<FinYear>) .map {
                                    FilterItem(
                                        it.fYear.value(),
                                        it.fYear.value(),
                                        FilterType.FIN_YEAR(it)
                                    )
                                },
                                mutableStateOf(true)
                            )
                        }
                        is FilterType.FIN_MONTH -> {
                            val fMonthList = mutableListOf<FilterItem>()
                            fMonthList.add(FilterItem(
                                names().all,
                                names().all,
                                FilterType.FIN_MONTH(FinMonth(fMonth = names().all))
                            ))
                            fMonthList.addAll((response.data as List<FinMonth>) .map {
                                FilterItem(
                                    it.fMonth.value(),
                                    it.fMonth.value(),
                                    FilterType.FIN_MONTH(it)
                                )
                            })
                            FilterDataItem(
                                names().selectTerritory,
                                fMonthList,
                                mutableStateOf(true)
                            )
                        }
                    }
                }
                is ResponseState.Error -> processError(response.e)
            }
        }
    }

    suspend fun uiScope(block:suspend ()->Unit){
        withContext(Dispatchers.Main){
            block.invoke()
        }
    }

    fun isAlreadyLoggedIn() = session.isAlreadyLoggedIn()

    fun getRoleID() = session.empRoleId()

    private fun showProgressBar(boolean: Boolean){
        z0001.value = boolean
    }
    fun showSuccessMessage(message: String){
        z0002.value = MessageData(message = message, enable = true)
        resetMessage(z0002)
    }

    fun showErrorMessage(message: String){
        z0003.value = MessageData(message = message, enable = true)
        resetMessage(z0003)
    }

    fun showAlertMessage(message: String){
        z0004.value = MessageData(message = message, enable = true)
        resetMessage(z0004)
    }

    fun showNormalMessage(message: String){
        z0005.value = MessageData(message = message, enable = true)
        resetMessage(z0005)
    }

    private fun processError(e: Exception?) = showErrorMessage(e?.message.value())

    private fun resetMessage(item: MutableState<MessageData>){
        backgroundScope {
            delay(1500)
            uiScope {
                item.value = MessageData()
            }
        }
    }
    fun setDemoUser(){
        session.apply {
            setEmpCode("CAP-0015")
            setEmpName("AP SRI VENKATESWARA TRADERS - SIRIVELLA")
            setEmpMobile("9578080988")
            setEmpRoleId("DL")
            setAlreadyLoggedIn(true)
        }
    }
}