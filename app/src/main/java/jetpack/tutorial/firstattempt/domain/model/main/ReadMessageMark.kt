package jetpack.tutorial.firstattempt.domain.model.main

import com.google.firebase.Timestamp

data class ReadMessageMark(
    val conversationId: String,
    val timeRead: Timestamp,
)
