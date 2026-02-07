package com.juagri.shared.ui.starclub

import com.juagri.shared.data.local.session.SessionPreference
import com.juagri.shared.data.local.session.datamanager.DataManager
import com.juagri.shared.ui.components.base.BaseViewModel
import com.juagri.shared.utils.Constants

class StarClubViewModel(
    session: SessionPreference,
    dataManager: DataManager
) : BaseViewModel(session, dataManager) {

    fun initScreen() {
        setScreenId(Constants.SCREEN_STAR_CLUB)
    }
}
