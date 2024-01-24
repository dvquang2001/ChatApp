package jetpack.tutorial.firstattempt.data.dto.main

import com.google.firebase.Timestamp

data class ReadMessageMarkDto(
    val conversationId: String? = null,
    val timeRead: Timestamp? = null
)
