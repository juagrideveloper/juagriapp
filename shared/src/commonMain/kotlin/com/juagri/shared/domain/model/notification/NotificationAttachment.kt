package com.juagri.shared.domain.model.notification

data class NotificationAttachment<T>(
    val content: T,
    val filename: String,
    val attachmentType: AttachmentType
)

enum class AttachmentType{
    UNKNOWN,
    IMAGE,
    PDF,
    TXT,
    VIDEO
}

enum class FileType{
    jpg,
    jpeg,
    png,
    pdf,
    mp4,
    txt
}
