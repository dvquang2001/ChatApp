package jetpack.tutorial.firstattempt.domain.model.main

import com.google.firebase.Timestamp

data class MessageModel(
    val id: String,
    val type: MessageType = MessageType.TEXT,
    val content: String,
    val timeSent: Timestamp,
    val senderId: String,
)

enum class MessageType {
    TEXT, PHOTO
}