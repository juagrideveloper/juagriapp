package com.juagri.shared.ui.splash

import com.juagri.shared.ui.components.base.BaseViewModel
import com.juagri.shared.data.local.session.SessionPreference

class SplashViewModel(private val localSession: SessionPreference): BaseViewModel() {
    fun isAlreadyLoggedIn() = localSession.isAlreadyLoggedIn()
}