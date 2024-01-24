package jetpack.tutorial.firstattempt.domain.data_source

import jetpack.tutorial.firstattempt.core.ResultModel
import jetpack.tutorial.firstattempt.domain.model.main.ConversationModel
import jetpack.tutorial.firstattempt.domain.model.main.MessageModel
import jetpack.tutorial.firstattempt.domain.model.main.UserModel
import jetpack.tutorial.firstattempt.domain.usecase.main.check_users_pair.UserParam
import jetpack.tutorial.firstattempt.domain.usecase.main.get_users.GetUsersParam
import jetpack.tutorial.firstattempt.domain.usecase.main.update_message.MessageParam
import jetpack.tutorial.firstattempt.domain.usecase.main.update_conversation.ConversationParam
import kotlinx.coroutines.flow.Flow

interface ConversationDataSource {

    fun getAllUsers(param: GetUsersParam): Flow<ResultModel<List<UserModel>>>

    fun getUser(param: String): Flow<ResultModel<UserModel>>

    fun getCurrentUser(): Flow<ResultModel<UserModel>>

    fun updateConversation(param: ConversationParam): Flow<ResultModel<ConversationModel>>

    fun updateMessage(messageParam: MessageParam): Flow<ResultModel<MessageModel>>

    fun getAllConversations(param: String): Flow<ResultModel<List<ConversationModel>>>

    fun getConversation(param: String): Flow<ResultModel<ConversationModel>>

    fun getAllMessages(conversationId: String): Flow<ResultModel<List<MessageModel>>>

    fun getMessage(messageParam: MessageParam): Flow<ResultModel<MessageModel>>

    fun checkTwoUserPair(userParam1: UserParam, userParam2: UserParam): Flow<ResultModel<ConversationModel>>
}