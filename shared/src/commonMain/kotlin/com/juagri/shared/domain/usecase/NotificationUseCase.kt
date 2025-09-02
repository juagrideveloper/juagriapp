package com.juagri.shared.domain.usecase

import com.juagri.shared.domain.model.employee.JUEmployee
import com.juagri.shared.domain.model.notification.NotificationAttachment
import com.juagri.shared.domain.model.notification.NotificationItem
import com.juagri.shared.domain.repo.notification.NotificationRepository

class NotificationUseCase(private val repository: NotificationRepository) {

    fun getUnreadNotificationsCount() = repository.getUnreadNotificationsCount()

    fun sendNotification(notification: NotificationItem, attachment: NotificationAttachment<Any>?) = repository.sendNotification(notification, attachment)

    fun getNotificationList() = repository.getNotificationList()

    fun setNotificationListener(emp: JUEmployee) = repository.setNotificationListener(emp)

    fun updateNotificationStatus(id: String) = repository.updateNotificationStatus(id)
}