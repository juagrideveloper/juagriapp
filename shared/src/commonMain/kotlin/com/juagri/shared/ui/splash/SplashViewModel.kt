package com.juagri.shared.ui.splash

import com.juagri.shared.ui.components.base.BaseViewModel
import com.juagri.shared.data.local.session.SessionPreference
import com.juagri.shared.data.local.session.datamanager.DataManager

class SplashViewModel(
    dataManager: DataManager,
    session: SessionPreference
) : BaseViewModel(session, dataManager) {
}