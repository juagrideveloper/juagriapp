package com.juagri.shared.ui.participation

import com.juagri.shared.data.local.session.SessionPreference
import com.juagri.shared.data.local.session.datamanager.DataManager
import com.juagri.shared.domain.model.promotion.ParticipationCounts
import com.juagri.shared.domain.usecase.ParticipationUseCase
import com.juagri.shared.ui.components.base.BaseViewModel
import com.juagri.shared.utils.UIState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class ParticipationViewModel(
    dataManager: DataManager,
    session: SessionPreference,
    private val participationUseCase: ParticipationUseCase
) : BaseViewModel(session, dataManager) {

    private var _participationItems: MutableStateFlow<UIState<List<ParticipationCounts>>> =
        MutableStateFlow(UIState.Init)
    val participationItems = _participationItems.asStateFlow()

    fun getParticipationDetails(){
        backgroundScope {
            participationUseCase.getParticipationDetails(getJUEmployee()!!).collect{response->
                uiScope(response, _participationItems)
            }
        }
    }
}