package jetpack.tutorial.firstattempt.presentation.auth.login

import android.util.Patterns
import dagger.hilt.android.lifecycle.HiltViewModel
import jetpack.tutorial.firstattempt.base.BaseViewEffect
import jetpack.tutorial.firstattempt.base.BaseViewEvent
import jetpack.tutorial.firstattempt.base.BaseViewModel
import jetpack.tutorial.firstattempt.base.BaseViewState
import jetpack.tutorial.firstattempt.core.ResultModel
import jetpack.tutorial.firstattempt.domain.data_source.AuthManager
import jetpack.tutorial.firstattempt.domain.model.auth.AuthStateModel
import jetpack.tutorial.firstattempt.domain.usecase.auth.login.LoginParam
import jetpack.tutorial.firstattempt.domain.usecase.auth.login.LoginUseCase
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val loginUseCase: LoginUseCase,
    private val authManager: AuthManager,
): BaseViewModel<LoginViewModel.ViewState, LoginViewModel.ViewEvent, LoginViewModel.ViewEffect>(
    ViewState()
) {

    private var loginJob: Job? = null

    override fun onEvent(event: ViewEvent) {
        when(event) {
            is ViewEvent.CheckLogin -> checkLogin()
            ViewEvent.Submit -> login()
            is ViewEvent.UpdateEmail -> updateEmail(event.email)
            is ViewEvent.UpdatePassword -> updatePassword(event.password)
            ViewEvent.TogglePasswordVisibility -> updatePasswordVisibility()
        }
    }

    private fun login() {
        val email = currentState.email
        val password = currentState.password
        val validateLogin = validateLogin(email, password)
        if (!validateLogin) return
        loginJob?.cancel()
        onLoading(true)
        loginJob = loginUseCase.execute(LoginParam(email, password))
            .onEach {
                onLoading(false)
                when (it) {
                    is ResultModel.Success -> {
                        setEffect(ViewEffect.LoginSuccess)
                    }

                    is ResultModel.Error -> {
                        setEffect(
                            ViewEffect.Error(
                                message = it.t.localizedMessage ?: "Unknown Error"
                            )
                        )
                    }
                }
            }.launchIn(coroutineScope)
    }

    private fun checkLogin() {
        coroutineScope.launch {
            authManager.getAuthState().collectLatest {
                if (it == AuthStateModel.LOGGED_IN)
                    setEffect(ViewEffect.LoginSuccess)
            }
        }
    }

    private fun updateEmail(email: String) {
        setState(currentState.copy(email = email))
    }

    private fun updatePassword(password: String) {
        setState(currentState.copy(password = password))
    }

    private fun updatePasswordVisibility() {
        setState(currentState.copy(showPassword = !currentState.showPassword))
    }


    private fun validateLogin(email: String, password: String): Boolean {
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            setState(
                currentState.copy(
                    emailError = "Invalid email"
                )
            )
            return false
        }
        if (password.trim().isEmpty()) {
            setState(
                currentState.copy(
                    passwordError = "Password cannot be empty"
                )
            )
        }
        if (password.trim().length < 8) {
            setState(
                currentState.copy(
                    passwordError = "Password must be longer than 8"
                )
            )
            return false
        }
        setState(currentState.copy(emailError = null, passwordError = null))
        return true
    }

    data class ViewState(
        val email: String = "",
        val password: String = "",
        val showPassword: Boolean = false,
        val emailError: String? = null,
        val passwordError: String? = null,
        val emailPlaceholder: String = "email@example.com",
        val passwordPlaceholder: String = "********",
    ) : BaseViewState

    sealed interface ViewEvent : BaseViewEvent {
        data object CheckLogin : ViewEvent
        data class UpdateEmail(val email: String) : ViewEvent
        data class UpdatePassword(val password: String) : ViewEvent
        data object TogglePasswordVisibility : ViewEvent
        data object Submit : ViewEvent
    }

    sealed interface ViewEffect : BaseViewEffect {
        data class Error(val message: String) : ViewEffect

        data object LoginSuccess : ViewEffect
    }
}