package com.juagri.shared.data.local.dao.common

import com.juagri.shared.NotificationDetailsQueries
import com.juagri.shared.domain.model.notification.NotificationItem
import com.juagri.shared.utils.toTimeStamp
import com.juagri.shared.utils.value


class NotificationsDao(private val queries: NotificationDetailsQueries) {

    fun insertNotifications(list: List<NotificationItem>) {
        list.forEach {
            queries.insertNotifications(
                id = it.id.value(),
                regCode = it.regCode.value(),
                regName = it.regName.value(),
                roleId = it.roleId.value(),
                roleName = it.roleName.value(),
                title = it.title.value(),
                content = it.content.value(),
                filename = it.filename.value(),
                unread = 0.0,
                updatedTime = it.updatedTime.value()
            )
        }
    }

    fun getNotifications() =
        queries.getNotifications().executeAsList().map {
            println("NotificationRepositoryImpl getNotifications ${it.id.value()} ")
            NotificationItem(
                id = it.id.value(),
                regCode = it.regCode.value(),
                regName = it.regName.value(),
                roleId = it.roleId.value(),
                roleName = it.roleName.value(),
                title = it.title.value(),
                content = it.content.value(),
                filename = it.filename.value(),
                unread = it.unread.value().toInt(),
                updatedTime = it.updatedTime.toTimeStamp()
            )
        }

    fun getUnreadNotificationsCount() = queries.getUnreadNotificationsCount().executeAsOne().toInt()

    fun getNotificationsLastUpdatedTime() = queries.getNotificationsLastUpdatedTime().executeAsOne().max.value()

    fun updateNotifications(notificationId: String) = queries.updateNotifications(notificationId)
}