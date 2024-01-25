package com.juagri.shared.com.juagri.shared.ui

import com.juagri.shared.data.local.session.SessionPreference
import com.juagri.shared.data.local.session.datamanager.DataManager
import com.juagri.shared.ui.components.base.BaseViewModel

class TestScreenViewModel(
    private val dataManager: DataManager,
    private val session: SessionPreference
) : BaseViewModel(session, dataManager) {

}