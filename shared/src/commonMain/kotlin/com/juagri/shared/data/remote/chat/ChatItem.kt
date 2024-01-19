package com.juagri.shared.data.remote.chat

enum class MessageType {
    TEXT,
    BUTTON,
    IMAGE,
    DATE,
    LOADING
}
data class ChatTextItem(
    override var message: String,
    override var isFromMe: Boolean = false,
    override val type: MessageType = MessageType.TEXT
) : ChatItem

data class ChatButtonItem(
    override var message: String = "",
    val buttons: List<ChatButtonContent>,
    override var isFromMe: Boolean = false,
    var isNotSelected: Boolean = true,
    override val type: MessageType = MessageType.BUTTON
) : ChatItem
data class ChatButtonContent(
    val id: Int,
    val title: String
)

data class ChatLoadingItem(
    override val type: MessageType = MessageType.LOADING,
    override var message: String = "",
    var isLoading: Boolean = true,
    override var isFromMe: Boolean = false
) : ChatItem

data class ChatImageItem(
    override val type: MessageType = MessageType.IMAGE,
    override var message: String = "",
    val url:String = "",
    override var isFromMe: Boolean = false
) : ChatItem

data class ChatDateItem(
    override val type: MessageType = MessageType.DATE,
    override var message: String = "",
    override var isFromMe: Boolean = false
) : ChatItem

interface ChatItem{
    val type: MessageType
    var message: String
    var isFromMe: Boolean
}