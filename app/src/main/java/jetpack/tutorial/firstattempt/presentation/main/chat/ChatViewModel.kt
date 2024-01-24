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
import jetpack.tutorial.firstattempt.domain.usecase.main.get_all_messages.GetAllMessagesUseCase
import jetpack.tutorial.firstattempt.domain.usecase.main.get_conversation.GetConversationUseCase
import jetpack.tutorial.firstattempt.domain.usecase.main.get_current_user.GetCurrentUserUseCase
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
import kotlin.math.log

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


    private var conversationId: String? = null
    private var isUsersPaired: Boolean = false

    init {
        val userId = savedStateHandle.get<String>("userId")
        Log.d("TAG", "UserDI: $userId")
        if(userId != null) {
            getUsersInRoom(userId)
            getCurrentUser()
        } else {
            conversationId = savedStateHandle.get<String>("conversationId")
            getCurrentUser()
            getConversation()
        }
    }

    private fun getConversation() {
        if(conversationId == null) return
        getConversationJob?.cancel()
        getConversationJob = getConversationUseCase.execute(conversationId!!)
            .onEach {
                when(it) {
                    is ResultModel.Success -> {
                        setState(
                            currentState.copy(
                                error = null
                            )
                        )
                        val conversation = it.result
                        getUsersInRoom(userIds = conversation.users)
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
                when(it) {
                    is ResultModel.Success -> {
                        setState(
                            currentState.copy(
                                users = it.result,
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
            getMessagesJob = getAllMessagesUseCase.execute(it)
                .onEach { result ->
                    when (result) {
                        is ResultModel.Success -> {
                            setState(
                                currentState.copy(
                                    messages = result.result.sortedBy { message ->
                                        message.timeSent
                                    },
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
                                    messages = it.result.messages.sortedBy { message ->
                                        message.timeSent
                                    },
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
                        checkUsersPair(currentState.users[0])
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
                            getMessages()
                        }

                        is ResultModel.Error -> {
                            Log.d("TAG", "sendMessage: ${result.t.message}")
                        }
                    }
                }
                .launchIn(coroutineScope)
        }
    }

    data class ViewState(
        val currentUser: UserModel? = null,
        val users: List<UserModel> = listOf(),
        val messages: List<MessageModel> = listOf(),
        val conversation: ConversationModel? = null,
        val textMessage: String = "",
        val error: String? = null,
    ) : BaseViewState

    sealed interface ViewEvent : BaseViewEvent {
        data class OnTextChanged(val text: String) : ViewEvent
        data class Send(val message: String) : ViewEvent
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
    }
}