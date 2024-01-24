package jetpack.tutorial.firstattempt.data.dto.main

import com.google.firebase.Timestamp

data class FireStoreConversationDto(
    val id: String? = null,
    val title: String? = null,
    val lastMessage: String? = null,
    val lastMessageTime: Timestamp = Timestamp.now(),
    val users: List<String> = listOf()
)