package com.juagri.shared.ui.doctor

import com.juagri.shared.data.local.session.SessionPreference
import com.juagri.shared.data.local.session.datamanager.DataManager
import com.juagri.shared.domain.model.doctor.JUDoctorDataItem
import com.juagri.shared.domain.usecase.JUDoctorUseCase
import com.juagri.shared.ui.components.base.BaseViewModel
import com.juagri.shared.utils.UIState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class DoctorViewModel(
    session: SessionPreference,
    dataManager: DataManager,
    private val juDoctorUseCase: JUDoctorUseCase
) : BaseViewModel(session, dataManager) {

    private var _juDoctorItems: MutableStateFlow<UIState<List<JUDoctorDataItem>>> =
        MutableStateFlow(UIState.Init)
    val juDoctorItems = _juDoctorItems.asStateFlow()

    fun getJUDoctorDetails(parentId: String){
        backgroundScope {
            juDoctorUseCase.getJUDoctorItems(parentId).collect { response ->
                uiScope(response,_juDoctorItems)
            }
        }
    }
}