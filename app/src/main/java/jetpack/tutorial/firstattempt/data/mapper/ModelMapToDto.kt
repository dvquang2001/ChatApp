package jetpack.tutorial.firstattempt.data.mapper

import jetpack.tutorial.firstattempt.data.dto.main.FireStoreUserDto
import jetpack.tutorial.firstattempt.data.dto.main.ReadMessageMarkDto
import jetpack.tutorial.firstattempt.domain.model.main.ReadMessageMark
import jetpack.tutorial.firstattempt.domain.model.main.UserModel

fun UserModel.toDto(): FireStoreUserDto {
    return FireStoreUserDto(
        id = id,
        email = email,
        name = name,
        createdAt = createdAt,
        avatar = avatar,
        readMessageMarkDto = readMessageMarks.map { it.toDto() }
    )
}

fun ReadMessageMark.toDto(): ReadMessageMarkDto {
    return ReadMessageMarkDto(
        conversationId = conversationId,
        timeRead = timeRead
    )
}