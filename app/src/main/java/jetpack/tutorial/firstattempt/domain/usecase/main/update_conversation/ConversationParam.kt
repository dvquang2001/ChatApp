package jetpack.tutorial.firstattempt.domain.usecase.main.update_conversation

import com.google.firebase.Timestamp
import jetpack.tutorial.firstattempt.domain.usecase.main.update_message.MessageParam

data class ConversationParam(
    val id: String? = null,
    val title: String? = null,
    val messages: List<MessageParam>,
    val lastMessage: String? = null,
    val lastMessageTime: Timestamp = Timestamp.now(),
    val users: List<String> = listOf()
)