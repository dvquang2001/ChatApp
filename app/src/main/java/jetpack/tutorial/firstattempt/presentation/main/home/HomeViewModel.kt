package jetpack.tutorial.firstattempt.presentation.main.home

import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import jetpack.tutorial.firstattempt.base.BaseViewEffect
import jetpack.tutorial.firstattempt.base.BaseViewEvent
import jetpack.tutorial.firstattempt.base.BaseViewModel
import jetpack.tutorial.firstattempt.base.BaseViewState
import jetpack.tutorial.firstattempt.core.ResultModel
import jetpack.tutorial.firstattempt.domain.model.main.ConversationModel
import jetpack.tutorial.firstattempt.domain.model.main.UserModel
import jetpack.tutorial.firstattempt.domain.usecase.auth.logout.LogoutUseCase
import jetpack.tutorial.firstattempt.domain.usecase.main.get_all_conversations.GetAllConversationsUseCase
import jetpack.tutorial.firstattempt.domain.usecase.main.get_conversations.GetConversationsUseCase
import jetpack.tutorial.firstattempt.domain.usecase.main.get_current_user.GetCurrentUserUseCase
import jetpack.tutorial.firstattempt.domain.usecase.main.get_users.GetAllUserUseCase
import jetpack.tutorial.firstattempt.domain.usecase.main.get_users.GetUsersParam
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getUserUseCase: GetAllUserUseCase,
    private val getCurrentUserUseCase: GetCurrentUserUseCase,
    private val logoutUseCase: LogoutUseCase,
    private val getAllConversationsUseCase: GetAllConversationsUseCase,
) : BaseViewModel<HomeViewModel.ViewState, HomeViewModel.ViewEvent, HomeViewModel.ViewEffect>(
    ViewState()
) {

    private var getUsersJob: Job? = null
    private var getCurrentUserJob: Job? = null
    private var logoutJob: Job? = null
    private var getConversationJob: Job? = null

    init {
        getCurrentUser()
        getUsers()

    }

    private fun getAllConversation(userId: String) {
        getConversationJob?.cancel()
        getConversationJob = getAllConversationsUseCase.execute(userId)
            .onEach {
                when(it) {
                    is ResultModel.Success -> {
                        setState(
                            currentState.copy(
                                conversations = it.result
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
            .launchIn(viewModelScope)
    }

    private fun getUsers() {
        getUsersJob?.cancel()
        getUsersJob = getUserUseCase.execute(GetUsersParam(userIds = listOf()))
            .onEach {
                if (it is ResultModel.Success) {
                    setState(
                        currentState.copy(
                            users = it.result.filter { user ->
                                user.id != currentState.currentUser?.id
                            }
                        )
                    )
                }
            }
            .launchIn(coroutineScope)
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
                    getAllConversation(it.result.id)
                }
            }
            .launchIn(coroutineScope)
    }

    private fun logout() {
        logoutJob?.cancel()
        onLoading(true)
        logoutJob = logoutUseCase.execute(Any())
            .onEach {
                onLoading(false)
                when (it) {
                    is ResultModel.Success -> {
                        setEffect(ViewEffect.SignOutSuccess)
                    }

                    is ResultModel.Error -> {
                        setEffect(
                            ViewEffect.SignOutFailed(
                                message = it.t.message ?: "Unknown Error"
                            )
                        )
                    }
                }
            }
            .launchIn(coroutineScope)
    }

    override fun onEvent(event: ViewEvent) {
        when (event) {
            is ViewEvent.SignOut -> logout()
        }
    }

    data class ViewState(
        val currentUser: UserModel? = null,
        val users: List<UserModel> = listOf(),
        val conversations: List<ConversationModel> = listOf(),
        val searchText: String = "",
        val isSelectedUserConnected: Boolean = false,
        val error: String? = null
    ) : BaseViewState

    sealed interface ViewEvent : BaseViewEvent {
        data object SignOut : ViewEvent
    }

    sealed interface ViewEffect : BaseViewEffect {

        data object SignOutSuccess : ViewEffect

        data class SignOutFailed(val message: String) : ViewEffect
    }

    override fun onCleared() {
        super.onCleared()
        getUsersJob?.cancel()
        getCurrentUserJob?.cancel()
        logoutJob?.cancel()
        getConversationJob?.cancel()
    }
}