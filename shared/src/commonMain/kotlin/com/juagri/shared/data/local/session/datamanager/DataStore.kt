package com.juagri.shared.data.local.session.datamanager

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import com.juagri.shared.domain.model.employee.JUEmployee
import com.juagri.shared.domain.model.menu.HeaderSlideMenu
import com.juagri.shared.utils.strings.EN_Names
import com.juagri.shared.utils.strings.Names

data class DataStore(
    val menuItems: MutableState<MutableList<HeaderSlideMenu>> = mutableStateOf(mutableListOf()),
    var employee: JUEmployee? = null,
    val labels: MutableState<Names> = mutableStateOf(
        EN_Names
    )
)
