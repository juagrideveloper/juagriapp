package com.juagri.shared.ui.focusProduct

import com.juagri.shared.data.local.session.SessionPreference
import com.juagri.shared.data.local.session.datamanager.DataManager
import com.juagri.shared.domain.model.focusProduct.CDOFocusProductItem
import com.juagri.shared.domain.usecase.CDOFocusProductUseCase
import com.juagri.shared.ui.components.base.BaseViewModel
import com.juagri.shared.utils.UIState
import com.juagri.shared.utils.value
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow


class CDOFocusProductSummaryViewModel(
    dataManager: DataManager,
    session: SessionPreference,
    private val cdoFocusProductUseCase: CDOFocusProductUseCase
) : BaseViewModel(session, dataManager) {

    private var _cdoFocusProduct: MutableStateFlow<UIState<CDOFocusProductItem>> =
        MutableStateFlow(UIState.Init)
    val cdoFocusProduct = _cdoFocusProduct.asStateFlow()

    fun getCDOFocusProduct() {
        backgroundScope {
            cdoFocusProductUseCase.getCDOFocusProductItems(getJUEmployee()?.code.value()).collect { response ->
                uiScope(response, _cdoFocusProduct)
            }
        }
    }
}