package com.juagri.shared.domain.repo.notification

import com.juagri.shared.domain.model.employee.JUEmployee
import com.juagri.shared.domain.model.notification.NotificationAttachment
import com.juagri.shared.domain.model.notification.NotificationItem
import com.juagri.shared.utils.ResponseState
import kotlinx.coroutines.flow.Flow

interface NotificationRepository {

    fun getUnreadNotificationsCount(): Flow<ResponseState<Int>>

    fun getNotificationList(): Flow<ResponseState<List<NotificationItem>>>

    fun sendNotification(notification: NotificationItem, attachment: NotificationAttachment<Any>?): Flow<ResponseState<Boolean>>

    fun setNotificationListener(emp: JUEmployee): Flow<ResponseState<Int>>

    fun updateNotificationStatus(id: String): Flow<ResponseState<Int>>
}