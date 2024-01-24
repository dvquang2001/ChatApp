package jetpack.tutorial.firstattempt.domain.model.main

import com.google.firebase.Timestamp

data class UserModel(
    val id: String,
    val email: String,
    val name: String,
    val createdAt: Timestamp,
    val avatar: String,
    val readMessageMarks: List<ReadMessageMark>,
)
