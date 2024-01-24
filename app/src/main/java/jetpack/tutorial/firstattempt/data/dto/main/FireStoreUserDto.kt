package jetpack.tutorial.firstattempt.data.dto.main

import com.google.firebase.Timestamp

data class FireStoreUserDto(
    val id: String? = null,
    val email: String? = null,
    val name: String? = null,
    val createdAt: Timestamp? = null,
    val avatar: String? = null,
    val readMessageMarkDto: List<ReadMessageMarkDto> = listOf(
        ReadMessageMarkDto(
            conversationId = "",
            timeRead = Timestamp.now(),
        )
    )
)