package jetpack.tutorial.firstattempt.data.mapper

import jetpack.tutorial.firstattempt.data.dto.main.MessageDto
import jetpack.tutorial.firstattempt.domain.model.main.MessageType
import jetpack.tutorial.firstattempt.domain.usecase.main.update_message.MessageParam

fun MessageParam.toDto(): MessageDto {
    val messageType: String = when (type) {
        MessageType.TEXT -> "TEXT"
        MessageType.PHOTO ->  "PHOTO"
    }
    return MessageDto(
        id = id,
        type = messageType,
        content = content,
        timeSent = timeSent,
        senderId = senderId
    )
}