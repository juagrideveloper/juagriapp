package com.juagri.shared.ui.participation

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import com.juagri.shared.data.local.session.SessionPreference
import com.juagri.shared.data.local.session.datamanager.DataManager
import com.juagri.shared.domain.model.employee.JUEmployee
import com.juagri.shared.domain.model.filter.FilterDataItem
import com.juagri.shared.domain.model.filter.FilterType
import com.juagri.shared.domain.model.promotion.ParticipationCounts
import com.juagri.shared.domain.usecase.ParticipationUseCase
import com.juagri.shared.domain.usecase.UserDetailsUseCase
import com.juagri.shared.ui.components.base.BaseViewModel
import com.juagri.shared.utils.ResponseState
import com.juagri.shared.utils.UIState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class ParticipationViewModel(
    dataManager: DataManager,
    session: SessionPreference,
    private val userDetailsUseCase: UserDetailsUseCase,
    private val participationUseCase: ParticipationUseCase
) : BaseViewModel(session, dataManager) {

    val selectedUser: MutableState<JUEmployee?> = mutableStateOf(null)

    private var _participationItems: MutableStateFlow<UIState<List<ParticipationCounts>>> =
        MutableStateFlow(UIState.Init)
    val participationItems = _participationItems.asStateFlow()

    fun getParticipationDetails(employee: JUEmployee){
        println("Selected User: "+ employee.name)
        backgroundScope {
            participationUseCase.getParticipationDetails(employee).collect{response->
                uiScope(response, _participationItems)
            }
        }
    }

    fun getUserList(){
        backgroundScope {
            userDetailsUseCase.getUserList(getJUEmployee()!!).collect { response ->
                uiScopeFilter(response, FilterType.USER(JUEmployee()))
                if(response is ResponseState.Success){
                    val filterDataItem = showDialog.value
                    showDialog.value = FilterDataItem(
                        title = filterDataItem.title,
                        filterItems = filterDataItem.filterItems.filter { (it.data as FilterType.USER).data.roleId != "CDO" },
                        isEnabled = filterDataItem.isEnabled
                    )
                }
            }
        }
    }
}