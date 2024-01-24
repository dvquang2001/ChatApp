package jetpack.tutorial.firstattempt.data.dto.main

import com.google.firebase.Timestamp

data class MessageDto(
    val id: String? = null,
    val type: String? = null,
    val content: String? = null,
    val timeSent: Timestamp? = null,
    val senderId: String? = null
)
