package com.juagri.shared.ui.profile

import com.juagri.shared.data.local.session.SessionPreference
import com.juagri.shared.data.local.session.datamanager.DataManager
import com.juagri.shared.ui.components.base.BaseViewModel
import com.juagri.shared.utils.AppUtils
import kotlinx.coroutines.delay

class ProfileViewModel(
    private val session: SessionPreference,
    dataManager: DataManager,
): BaseViewModel(session,dataManager){

    fun logout() {
        session.apply {
            setAlreadyLoggedIn(false)
            setEmpMobile("")
            setEmpCode("")
            setEmpRoleId("")
            clearAll()
        }

        backgroundScope {
            delay(1500)
            AppUtils.logout()
        }
    }
}