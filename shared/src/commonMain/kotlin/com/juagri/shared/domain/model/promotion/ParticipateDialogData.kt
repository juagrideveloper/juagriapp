package com.juagri.shared.domain.model.promotion

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf

data class ParticipateDialogData(
    val title: String = "",
    val entryId: String = "",
    val showDialog: MutableState<Boolean> = mutableStateOf(false),
    val activityCode: String = ""
)