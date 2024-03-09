package com.juagri.shared.ui.profile

import com.juagri.shared.data.local.session.SessionPreference
import com.juagri.shared.data.local.session.datamanager.DataManager
import com.juagri.shared.ui.components.base.BaseViewModel

class ProfileViewModel(
    session: SessionPreference,
    dataManager: DataManager,
): BaseViewModel(session,dataManager)