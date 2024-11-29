package com.juagri.shared.ui.dashboard.cdo

import com.juagri.shared.utils.Constants
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import com.juagri.shared.data.local.session.SessionPreference
import com.juagri.shared.data.local.session.datamanager.DataManager
import com.juagri.shared.domain.model.employee.JUEmployee
import com.juagri.shared.domain.model.filter.FilterType
import com.juagri.shared.domain.model.promotion.PromotionDashboard
import com.juagri.shared.domain.usecase.PromotionUseCase
import com.juagri.shared.domain.usecase.UserDetailsUseCase
import com.juagri.shared.ui.components.base.BaseViewModel
import com.juagri.shared.utils.UIState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class CDODashboardViewModel(
    dataManager: DataManager,
    session: SessionPreference,
    private val userDetailsUseCase: UserDetailsUseCase,
    private val promotionUseCase: PromotionUseCase
) : BaseViewModel(session, dataManager) {


    var showUserList: Boolean = getJUEmployee()?.roleId != Constants.EMP_ROLE_CDO
    val selectedUser: MutableState<JUEmployee?> = mutableStateOf(null)

    private var _promotionDashboardItems: MutableStateFlow<UIState<List<PromotionDashboard>>> =
        MutableStateFlow(UIState.Init)
    val promotionDashboardItems = _promotionDashboardItems.asStateFlow()

    fun resetScreen(){
        _promotionDashboardItems.value = UIState.Init
    }
    fun getDashboard(){
        getJUEmployee()?.let {employee->
            backgroundScope {
                promotionUseCase.getDashboard(employee).collect { response ->
                    uiScope(response, _promotionDashboardItems)
                }
            }
        }
    }

    fun getDashboardByUserId(userId: String) {
        backgroundScope {
            promotionUseCase.getDashboardByEmployeeId(userId).collect { response ->
                uiScope(response, _promotionDashboardItems)
            }
        }
    }

    fun getUserList(){
        backgroundScope {
            userDetailsUseCase.getUserList(getJUEmployee()!!).collect { response ->
                uiScopeFilter(response, FilterType.USER(JUEmployee()))
            }
        }
    }
}