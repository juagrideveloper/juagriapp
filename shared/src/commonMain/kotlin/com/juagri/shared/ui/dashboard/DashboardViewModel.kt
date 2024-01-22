package com.juagri.shared.ui.dashboard

import com.juagri.shared.data.local.session.SessionPreference
import com.juagri.shared.data.local.session.datamanager.DataManager
import com.juagri.shared.ui.components.base.BaseViewModel

class DashboardViewModel(
    private val session: SessionPreference,
    private val dataManager: DataManager
) : BaseViewModel(session, dataManager) {

}