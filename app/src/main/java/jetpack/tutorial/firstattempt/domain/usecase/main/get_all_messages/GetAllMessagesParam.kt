package jetpack.tutorial.firstattempt.domain.usecase.main.get_all_messages

data class GetAllMessagesParam(
    val conversationId: String,
    val loadCount: Int,
)
