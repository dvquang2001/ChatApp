package jetpack.tutorial.firstattempt.data.mapper

import com.google.firebase.Timestamp
import jetpack.tutorial.firstattempt.data.dto.auth.AuthState
import jetpack.tutorial.firstattempt.data.dto.auth.LoginDto
import jetpack.tutorial.firstattempt.data.dto.auth.RegisterDto
import jetpack.tutorial.firstattempt.data.dto.main.FireStoreConversationDto
import jetpack.tutorial.firstattempt.data.dto.main.FireStoreUserDto
import jetpack.tutorial.firstattempt.data.dto.main.MessageDto
import jetpack.tutorial.firstattempt.data.dto.main.ReadMessageMarkDto
import jetpack.tutorial.firstattempt.domain.model.auth.AuthStateModel
import jetpack.tutorial.firstattempt.domain.model.auth.LoginModel
import jetpack.tutorial.firstattempt.domain.model.auth.RegisterModel
import jetpack.tutorial.firstattempt.domain.model.main.ConversationModel
import jetpack.tutorial.firstattempt.domain.model.main.MessageModel
import jetpack.tutorial.firstattempt.domain.model.main.MessageType
import jetpack.tutorial.firstattempt.domain.model.main.ReadMessageMark
import jetpack.tutorial.firstattempt.domain.model.main.UserModel

fun LoginDto.toModel(): LoginModel {
    return LoginModel(
        userId = userId ?: "",
        email = email ?: ""
    )
}

fun RegisterDto.toModel(): RegisterModel {
    return RegisterModel(
        userId = userId ?: "",
        email = email ?: "",
        fullName = fullName ?: ""
    )
}

fun AuthState?.toModel(): AuthStateModel {
    return when (this) {
        AuthState.LOGGED_IN -> AuthStateModel.LOGGED_IN
        AuthState.LOGGED_OUT -> AuthStateModel.LOGGED_OUT
        else -> AuthStateModel.LOGGED_OUT
    }
}

fun FireStoreUserDto.toModel(): UserModel {
    return UserModel(
        id = id ?: "",
        email = email ?: "",
        name = name ?: "",
        createdAt = createdAt ?: Timestamp.now(),
        avatar = avatar ?: "",
        readMessageMarks = readMessageMarkDto.map {
            it.toModel()
        }
    )
}

fun ReadMessageMarkDto.toModel(): ReadMessageMark {
    return ReadMessageMark(
        conversationId = conversationId ?: "",
        timeRead = timeRead ?: Timestamp.now(),
    )
}

fun FireStoreConversationDto.toModel(messages: List<MessageDto>): ConversationModel {
    return ConversationModel(
        id = id ?: "",
        title = title ?: "Conversation",
        messages = messages.map {
            it.toModel()
        },
        lastMessage = lastMessage ?: "",
        lastMessageTime = lastMessageTime,
        users = users
    )
}

fun MessageDto.toModel(): MessageModel {
    val messageType = when (type) {
        "TEXT" -> MessageType.TEXT
        "PHOTO" -> MessageType.PHOTO
        else -> MessageType.TEXT
    }
    return MessageModel(
        id = id ?: "",
        type = messageType,
        content = content ?: "",
        timeSent ?: Timestamp.now(),
        senderId ?: ""
    )
}