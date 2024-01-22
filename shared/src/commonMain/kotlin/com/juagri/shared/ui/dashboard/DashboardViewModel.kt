package com.juagri.shared.com.juagri.shared.ui.dashboard

import com.juagri.shared.data.local.session.SessionPreference
import com.juagri.shared.ui.components.base.BaseViewModel

class DashboardViewModel(private val session: SessionPreference):BaseViewModel() {
    fun getRoleID() = session.empRoleId()
}