package com.juagri.shared.ui.notifications

import androidx.compose.runtime.MutableState
import com.juagri.shared.data.local.session.SessionPreference
import com.juagri.shared.data.local.session.datamanager.DataManager
import com.juagri.shared.domain.model.filter.FilterType
import com.juagri.shared.domain.model.notification.NotificationAttachment
import com.juagri.shared.domain.model.notification.NotificationItem
import com.juagri.shared.domain.model.user.JURegion
import com.juagri.shared.domain.usecase.NotificationUseCase
import com.juagri.shared.domain.usecase.UserDetailsUseCase
import com.juagri.shared.ui.components.base.BaseViewModel
import com.juagri.shared.utils.ResponseState
import com.juagri.shared.utils.UIState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class NotificationViewModel(
    private val userDetailsUseCase: UserDetailsUseCase,
    private val notificationUseCase: NotificationUseCase,
    dataManager: DataManager,
    session: SessionPreference
) : BaseViewModel(session, dataManager) {

    private var _notificationItems: MutableStateFlow<UIState<List<NotificationItem>>> =
        MutableStateFlow(UIState.Init)
    val notificationItems = _notificationItems.asStateFlow()

    private var _sendNotificationItem: MutableStateFlow<UIState<Boolean>> =
        MutableStateFlow(UIState.Init)
    val sendNotificationItem = _sendNotificationItem.asStateFlow()

    fun getRegionList(){
        backgroundScope{
            userDetailsUseCase.getRegionList().collect{ response ->
                uiScopeFilter(response, FilterType.REGION(JURegion()), true)
            }
        }
    }

    fun getNotifications() {
        backgroundScope {
            notificationUseCase.getNotificationList().collect { result ->
                uiScope(result, _notificationItems)
            }
        }
    }

    fun sendNotification(notificationItem: NotificationItem, attachment: NotificationAttachment<Any>?) {
        backgroundScope {
            notificationUseCase.sendNotification(notificationItem, attachment).collect { result ->
                uiScope(result, _sendNotificationItem)
            }
        }
    }

    fun updateNotificationStatus(id: String, notificationCount: MutableState<Int>) {
        backgroundScope {
            println("NotificationRepositoryImpl updateNotificationStatus")
            notificationUseCase.updateNotificationStatus(id).collect { result ->
                if(result is ResponseState.Success){
                    println("NotificationRepositoryImpl updateNotificationStatus ResponseState.Success (${result.data})")
                    notificationCount.value = result.data
                }
            }
        }
    }

    fun reset(){
        _sendNotificationItem.value = UIState.Init
    }
}