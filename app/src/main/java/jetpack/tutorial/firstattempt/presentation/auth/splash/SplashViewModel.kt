package jetpack.tutorial.firstattempt.presentation.auth.splash

import dagger.hilt.android.lifecycle.HiltViewModel
import jetpack.tutorial.firstattempt.base.BaseViewEffect
import jetpack.tutorial.firstattempt.base.BaseViewEvent
import jetpack.tutorial.firstattempt.base.BaseViewModel
import jetpack.tutorial.firstattempt.base.BaseViewState
import jetpack.tutorial.firstattempt.core.ResultModel
import jetpack.tutorial.firstattempt.domain.model.auth.AuthStateModel
import jetpack.tutorial.firstattempt.domain.usecase.auth.auth_state.AuthStateUseCase
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class SplashViewModel @Inject constructor(
    private val authStateUseCase: AuthStateUseCase,
    ) :
    BaseViewModel<SplashViewModel.ViewState, SplashViewModel.ViewEvent, SplashViewModel.ViewEffect>(
        ViewState()
    ) {

    private var authStateJob: Job? = null

    init {
        checkAuthState()
    }

    private fun checkAuthState() {
        authStateJob?.cancel()
        authStateJob = coroutineScope.launch {
            authStateUseCase.execute(Any()).collectLatest {
                if (it is ResultModel.Success) {
                    setState(currentState.copy(authState = it.result))
                }
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        authStateJob?.cancel()
    }

    override fun onEvent(viewEvent: ViewEvent) {

    }

    data class ViewState(
        val authState: AuthStateModel? = null
    ) : BaseViewState

    sealed interface ViewEvent : BaseViewEvent

    sealed interface ViewEffect : BaseViewEffect
}
