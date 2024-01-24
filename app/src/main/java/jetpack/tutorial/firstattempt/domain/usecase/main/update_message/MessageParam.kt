package jetpack.tutorial.firstattempt.domain.usecase.main.update_message

import com.google.firebase.Timestamp
import jetpack.tutorial.firstattempt.domain.model.main.MessageType

data class MessageParam(
    val id: String? = null,
    val type: MessageType = MessageType.TEXT,
    val content: String,
    val timeSent: Timestamp = Timestamp.now(),
    val senderId: String,
    val conversationId: String? = null,
)
