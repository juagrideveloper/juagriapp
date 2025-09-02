package com.juagri.shared.data.remote.notification

import com.juagri.shared.data.local.dao.common.NotificationsDao
import com.juagri.shared.domain.model.employee.JUEmployee
import com.juagri.shared.domain.model.notification.NotificationAttachment
import com.juagri.shared.domain.model.notification.NotificationItem
import com.juagri.shared.domain.repo.notification.NotificationRepository
import com.juagri.shared.utils.Constants
import com.juagri.shared.utils.JUError
import com.juagri.shared.utils.ResponseState
import com.juagri.shared.utils.uploadFiles
import com.juagri.shared.utils.value
import dev.gitlive.firebase.firestore.ChangeType
import dev.gitlive.firebase.firestore.CollectionReference
import dev.gitlive.firebase.firestore.Direction
import dev.gitlive.firebase.firestore.Timestamp
import dev.gitlive.firebase.firestore.fromMilliseconds
import dev.gitlive.firebase.firestore.orderBy
import dev.gitlive.firebase.firestore.where
import io.ktor.util.date.GMTDate
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flow

class NotificationRepositoryImpl(
    private val notificationDB: CollectionReference,
    private val notificationsDao: NotificationsDao
): NotificationRepository {
    override fun getUnreadNotificationsCount(): Flow<ResponseState<Int>> = flow {
        emit(ResponseState.Success(getUnreadCount()))
    }

    override fun getNotificationList(): Flow<ResponseState<List<NotificationItem>>> = flow {
        emit(ResponseState.Success(notificationsDao.getNotifications()))
    }

    override fun setNotificationListener(emp: JUEmployee): Flow<ResponseState<Int>> = flow {
        val regionCodes = emp.regionCode.value().split(",").toMutableList()
        regionCodes.add("All")
        println("NotificationRepositoryImpl: setNotificationListener")
        val roleIds = arrayListOf(emp.roleId.value(), "All")
        try {
            notificationDB
                .where { Constants.FIELD_REG_CODE inArray regionCodes }
                .where { Constants.FIELD_ROLE_ID inArray roleIds }
                .where { Constants.FIELD_UPDATED_TIME  greaterThan Timestamp.fromMilliseconds(notificationsDao.getNotificationsLastUpdatedTime() + 10000) }
                .orderBy(Constants.FIELD_UPDATED_TIME, Direction.DESCENDING )
                .snapshots
                .collect({ snapshots ->
                    val items = mutableListOf<NotificationItem>()
                    snapshots.documentChanges.forEach { doc ->
                        if(doc.type == ChangeType.ADDED){
                            items.add(doc.document.data<NotificationItem>())
                            println("NotificationRepositoryImpl Doc ID: " + doc.type.name)
                        }
                    }
                    if(items.isNotEmpty()) {
                        notificationsDao.insertNotifications(items)
                        emit(ResponseState.Success(getUnreadCount()))
                        println("NotificationRepositoryImpl: Success ${items.size}")
                    }
                })
            emit(ResponseState.Success(getUnreadCount()))
        } catch (e: Exception) {
            println("NotificationRepositoryImpl: " + e.message)
            e.printStackTrace()
            emit(ResponseState.Error())
        }
    }

    override fun updateNotificationStatus(id: String): Flow<ResponseState<Int>> = flow{
        println("NotificationRepositoryImpl: updateNotificationStatus id $id")
        notificationsDao.updateNotifications(id)
        emit(ResponseState.Success(getUnreadCount()))
    }

    override fun sendNotification(notification: NotificationItem, attachment: NotificationAttachment<Any>?): Flow<ResponseState<Boolean>> = callbackFlow {
        trySend(ResponseState.Loading(true))
        try {
            val notificationId =
                notification.regCode + "-" + notification.roleId + "-" + GMTDate().timestamp
            notification.apply {
                filename = attachment?.filename?.let { "$notificationId-$it" } ?: ""
                updatedTime = Timestamp.now()
                id = notificationId
            }
            notificationDB.document(notificationId).set(notification)
            attachment?.let { attachment ->
                uploadFiles(
                    Constants.FOLDER_NOTIFICATION_FILES,
                    listOf(attachment.content as ByteArray),
                    listOf(notification.filename)
                ) {
                    trySend(ResponseState.Loading())
                    trySend(ResponseState.Success(true))
                }
            } ?: run {
                trySend(ResponseState.Loading())
                trySend(ResponseState.Success(true))
            }
        }catch (e: Exception){
            trySend(ResponseState.Loading())
            trySend(ResponseState.Error(JUError.GeneralError))
        }
        awaitClose {
            channel.close()
        }
    }

    private fun getUnreadCount(): Int {
        val count = notificationsDao.getUnreadNotificationsCount()
        println("NotificationRepositoryImpl: getUnreadNotificationsCount $count")
        return count
    }

}