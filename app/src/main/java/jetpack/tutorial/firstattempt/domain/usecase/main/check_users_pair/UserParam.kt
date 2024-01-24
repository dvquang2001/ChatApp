package jetpack.tutorial.firstattempt.domain.usecase.main.check_users_pair

import com.google.firebase.Timestamp
import jetpack.tutorial.firstattempt.domain.model.main.ReadMessageMark
import jetpack.tutorial.firstattempt.domain.model.main.UserModel

data class UserParam(
    val id: String,
    val email: String,
    val name: String,
    val createdAt: Timestamp,
    val avatar: String,
    val readMessageMarks: List<ReadMessageMark>,
)

fun UserModel.toParam(): UserParam {
    return UserParam(
        id = id,
        email = email,
        name = name,
        createdAt = createdAt,
        avatar = avatar,
        readMessageMarks = readMessageMarks
    )
}