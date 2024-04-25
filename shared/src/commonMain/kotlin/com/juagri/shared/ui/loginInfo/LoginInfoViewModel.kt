package com.juagri.shared.ui.loginInfo

import com.juagri.shared.data.local.session.SessionPreference
import com.juagri.shared.data.local.session.datamanager.DataManager
import com.juagri.shared.domain.model.liquidation.DealerLiquidationData
import com.juagri.shared.domain.model.user.LoginInfo
import com.juagri.shared.domain.usecase.DealerLiquidationUseCase
import com.juagri.shared.domain.usecase.LoginInfoUseCase
import com.juagri.shared.ui.components.base.BaseViewModel
import com.juagri.shared.utils.UIState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class LoginInfoViewModel(
    dataManager: DataManager,
    session: SessionPreference,
    private val loginInfoUseCase: LoginInfoUseCase
) : BaseViewModel(session, dataManager) {

    private var _loginInfoItems: MutableStateFlow<UIState<List<LoginInfo>>> =
        MutableStateFlow(UIState.Init)
    val loginInfoItems = _loginInfoItems.asStateFlow()

    fun getLoginInfoDetails(){
        backgroundScope {
            loginInfoUseCase.getLoginInfoDetails(getJUEmployee()!!).collect{response->
                uiScope(response, _loginInfoItems)
            }
        }
    }
}