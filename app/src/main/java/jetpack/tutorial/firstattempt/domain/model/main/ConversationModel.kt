package jetpack.tutorial.firstattempt.domain.model.main

import com.google.firebase.Timestamp

data class ConversationModel(
    val id: String,
    val title: String,
    val messages: List<MessageModel>,
    val lastMessage: String,
    val lastMessageTime: Timestamp,
    val users: List<String> // contains all userID
)