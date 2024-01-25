package jetpack.tutorial.firstattempt.presentation.main.chat

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import dagger.hilt.android.lifecycle.HiltViewModel
import jetpack.tutorial.firstattempt.base.BaseViewEffect
import jetpack.tutorial.firstattempt.base.BaseViewEvent
import jetpack.tutorial.firstattempt.base.BaseViewModel
import jetpack.tutorial.firstattempt.base.BaseViewState
import jetpack.tutorial.firstattempt.core.ResultModel
import jetpack.tutorial.firstattempt.domain.model.main.ConversationModel
import jetpack.tutorial.firstattempt.domain.model.main.MessageModel
import jetpack.tutorial.firstattempt.domain.model.main.UserModel
import jetpack.tutorial.firstattempt.domain.usecase.main.check_users_pair.CheckUserPairParam
import jetpack.tutorial.firstattempt.domain.usecase.main.check_users_pair.CheckUsersPairUseCase
import jetpack.tutorial.firstattempt.domain.usecase.main.check_users_pair.toParam
import jetpack.tutorial.firstattempt.domain.usecase.main.get_all_messages.GetAllMessagesParam
import jetpack.tutorial.firstattempt.domain.usecase.main.get_all_messages.GetAllMessagesUseCase
import jetpack.tutorial.firstattempt.domain.usecase.main.get_conversation.GetConversationUseCase
import jetpack.tutorial.firstattempt.domain.usecase.main.get_current_user.GetCurrentUserUseCase
import jetpack.tutorial.firstattempt.domain.usecase.main.get_message.GetMessageParam
import jetpack.tutorial.firstattempt.domain.usecase.main.get_message.GetMessageUseCase
import jetpack.tutorial.firstattempt.domain.usecase.main.get_user.GetUserUseCase
import jetpack.tutorial.firstattempt.domain.usecase.main.get_users.GetAllUserUseCase
import jetpack.tutorial.firstattempt.domain.usecase.main.get_users.GetUsersParam
import jetpack.tutorial.firstattempt.domain.usecase.main.update_conversation.ConversationParam
import jetpack.tutorial.firstattempt.domain.usecase.main.update_conversation.UpdateConversationUseCase
import jetpack.tutorial.firstattempt.domain.usecase.main.update_message.MessageParam
import jetpack.tutorial.firstattempt.domain.usecase.main.update_message.UpdateMessageUseCase
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class ChatViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val updateConversationUseCase: UpdateConversationUseCase,
    private val getCurrentUserUseCase: GetCurrentUserUseCase,
    private val checkUsersPairUseCase: CheckUsersPairUseCase,
    private val getUserUseCase: GetUserUseCase,
    private val getAllUserUseCase: GetAllUserUseCase,
    private val updateMessageUseCase: UpdateMessageUseCase,
    private val getAllMessagesUseCase: GetAllMessagesUseCase,
    private val getConversationUseCase: GetConversationUseCase,
    private val getMessageUseCase: GetMessageUseCase,

) : BaseViewModel<ChatViewModel.ViewState, ChatViewModel.ViewEvent, ChatViewModel.ViewEffect>(
    ViewState()
) {
    private var joinConversationJob: Job? = null
    private var getCurrentUserJob: Job? = null
    private var checkUsersPairJob: Job? = null
    private var getUserJob: Job? = null
    private var getAllUserJob: Job? = null
    private var sendMessageJob: Job? = null
    private var getMessagesJob: Job? = null
    private var getConversationJob: Job? = null
    private var getMessageJob: Job? = null
    private var updateConversationJob: Job? = null

    private var conversationId: String? = null
    private var isUsersPaired: Boolean = false
    private var conversation: ConversationModel? = null

    init {
        val userId = savedStateHandle.get<String>("userId")
        if (userId != null) {
            getUsersInRoom(userId)
            getCurrentUser()
        }

        conversationId = savedStateHandle.get<String>("conversationId")
        if (conversationId != null) {
            Log.d("TAG-ne", "UserDI: $conversationId")
            getConversation()
        }
    }

    private fun getConversation() {
        if (conversationId == null) return
        getConversationJob?.cancel()
        getConversationJob = getConversationUseCase.execute(conversationId!!)
            .onEach {
                when (it) {
                    is ResultModel.Success -> {
                        setState(
                            currentState.copy(
                                error = null
                            )
                        )
                        conversation = it.result
                        getCurrentUser()
                        getUsersInRoom(userIds = conversation!!.users)
                        getMessages()
                    }

                    is ResultModel.Error -> {
                        setState(
                            currentState.copy(
                                error = it.t.message ?: "Unknown error"
                            )
                        )
                    }
                }
            }
            .launchIn(coroutineScope)
    }

    private fun getUsersInRoom(userIds: List<String>) {
        getAllUserJob?.cancel()
        getAllUserJob = getAllUserUseCase.execute(GetUsersParam(userIds))
            .onEach {
                when (it) {
                    is ResultModel.Success -> {
                        setState(
                            currentState.copy(
                                users = it.result.filter { user ->
                                    user.id != currentState.currentUser?.id
                                },
                                error = null
                            )
                        )
                    }

                    is ResultModel.Error -> {
                        setState(
                            currentState.copy(
                                error = it.t.message
                            )
                        )
                    }
                }
            }
            .launchIn(coroutineScope)
    }

    private fun getMessages() {
        conversationId?.let {
            getMessagesJob?.cancel()
            getMessagesJob = getAllMessagesUseCase.execute(
                GetAllMessagesParam(
                    it,
                    currentState.page
                )
            )
                .onEach { result ->
                    when (result) {
                        is ResultModel.Success -> {
                            val messages = currentState.messages.toMutableList()
                            messages.addAll(result.result)
                            currentState = currentState.copy(page = currentState.page + 1)
                            setState(
                                currentState.copy(
                                    messages = messages,
                                    textMessage = "",
                                    error = null
                                )
                            )
                        }

                        is ResultModel.Error -> {
                            setState(
                                currentState.copy(
                                    error = result.t.message ?: "Unknown error"
                                )
                            )
                        }
                    }
                }
                .launchIn(coroutineScope)
        }
    }

    override fun onEvent(event: ViewEvent) {
        when (event) {
            is ViewEvent.OnTextChanged -> {
                setState(
                    currentState.copy(
                        textMessage = event.text
                    )
                )
            }

            is ViewEvent.Send -> sendMessage(event.message)
            is ViewEvent.LoadMoreMessages -> {} //getMessages()
        }
    }

    private fun checkUsersPair(user: UserModel) {
        checkUsersPairJob?.cancel()
        val param = CheckUserPairParam(
            currentState.currentUser!!.toParam(),
            user.toParam()
        )
        checkUsersPairJob = checkUsersPairUseCase.execute(param)
            .onEach {
                when (it) {
                    is ResultModel.Success -> {
                        isUsersPaired = true
                        conversationId = it.result.id
                        setState(
                            currentState.copy(
                                error = null
                            )
                        )
                        joinConversation(user)
                    }

                    is ResultModel.Error -> {
                        isUsersPaired = false
                        setState(
                            currentState.copy(
                                error = it.t.message ?: "Unknown Error"
                            )
                        )
                    }
                }
            }
            .launchIn(coroutineScope)
    }

    private fun joinConversation(user: UserModel) {
        if (!isUsersPaired) {
            val currentUser = currentState.currentUser
            val param = ConversationParam(
                title = user.name,
                messages = listOf(),
                users = listOf(
                    currentUser!!.id,
                    user.id
                )
            )
            joinConversationJob?.cancel()
            joinConversationJob = updateConversationUseCase.execute(param)
                .onEach {
                    when (it) {
                        is ResultModel.Success -> {
                            setState(
                                currentState.copy(
                                    messages = it.result.messages,
                                    error = null
                                )
                            )
                            conversationId = it.result.id
                        }

                        is ResultModel.Error -> {
                            setState(
                                currentState.copy(
                                    error = it.t.message ?: "Unknown Error"
                                )
                            )
                        }
                    }
                }
                .launchIn(coroutineScope)
        } else {
            getMessages()
        }
    }

    private fun getCurrentUser() {
        getCurrentUserJob?.cancel()
        getCurrentUserJob = getCurrentUserUseCase.execute(Any())
            .onEach {
                if (it is ResultModel.Success) {
                    getMessage()
                    setState(
                        currentState.copy(
                            currentUser = it.result
                        )
                    )
                }
            }
            .launchIn(coroutineScope)
    }

    private fun getUsersInRoom(userId: String) {
        getUserJob?.cancel()
        val newUsers = currentState.users.toMutableList()
        getUserJob = getUserUseCase.execute(userId)
            .onEach {
                when (it) {
                    is ResultModel.Success -> {
                        newUsers.add(it.result)
                        setState(
                            currentState.copy(
                                users = newUsers,
                                error = null
                            )
                        )
                        if (newUsers.isNotEmpty()) {
                            checkUsersPair(newUsers[0])
                        }
                    }

                    is ResultModel.Error -> {
                        setState(
                            currentState.copy(
                                error = it.t.message ?: "Unknown error"
                            )
                        )
                    }
                }
            }
            .launchIn(coroutineScope)
    }

    private fun sendMessage(message: String) {
        if (conversationId == null) return
        val currentUser = currentState.currentUser
        currentUser?.let {
            sendMessageJob?.cancel()
            val param = MessageParam(
                content = message,
                senderId = it.id,
                conversationId = conversationId
            )
            sendMessageJob = updateMessageUseCase.execute(param)
                .onEach { result ->
                    when (result) {
                        is ResultModel.Success -> {
                            val messageModel = result.result
                            updateConversation(
                                conversationId = conversationId!!,
                                messageModel = messageModel
                            )
                            setState(
                                currentState.copy(
                                    textMessage = "",
                                    error = null
                                )
                            )
                        }

                        is ResultModel.Error -> {
                            Log.d("TAG", "sendMessage: ${result.t.message}")
                        }
                    }
                }
                .launchIn(coroutineScope)
        }
    }

    private fun updateConversation(conversationId: String, messageModel: MessageModel) {
        updateConversationJob?.cancel()
        updateConversationJob = updateConversationUseCase.execute(
            ConversationParam(
                lastMessage = messageModel.content,
                conversationId = conversationId
            )
        )
            .onEach {
                when(it) {
                    is ResultModel.Success -> {
                        getMessage(messageModel)
                    }

                    is ResultModel.Error -> {
                        Log.d("TAG", "updateConversation: ${it.t.message}")
                    }
                }
            }
            .launchIn(coroutineScope)
    }

    private fun getMessage(messageModel: MessageModel? = null) {
        getMessageJob?.cancel()
        getMessageJob = getMessageUseCase.execute(
            GetMessageParam(
                id = messageModel?.id,
                conversationId = conversationId ?: return
            )
        )
            .onEach {
                when(it) {
                    is ResultModel.Success -> {
                        val newMessages = currentState.messages.toMutableList()
                        if(!newMessages.map { it.id }.contains(it.result.id) && it.result.id.isNotEmpty()) {
                            newMessages.add(0,it.result)
                        }
                        setState(
                            currentState.copy(
                                messages = newMessages
                            )
                        )
                    }

                    is ResultModel.Error -> {

                    }
                }
            }
            .launchIn(coroutineScope)
    }

    data class ViewState(
        val currentUser: UserModel? = null,
        val users: List<UserModel> = listOf(),
        val messages: List<MessageModel> = listOf(),
        val conversation: ConversationModel? = null,
        val textMessage: String = "",
        val error: String? = null,
        val page: Int = 0,
    ) : BaseViewState

    sealed interface ViewEvent : BaseViewEvent {
        data class OnTextChanged(val text: String) : ViewEvent
        data class Send(val message: String) : ViewEvent
        data object LoadMoreMessages : ViewEvent
    }

    class ViewEffect : BaseViewEffect

    override fun onCleared() {
        super.onCleared()
        getCurrentUserJob?.cancel()
        joinConversationJob?.cancel()
        checkUsersPairJob?.cancel()
        getUserJob?.cancel()
        getAllUserJob?.cancel()
        sendMessageJob?.cancel()
        getMessagesJob?.cancel()
        getConversationJob?.cancel()
        getMessagesJob?.cancel()
    }
}