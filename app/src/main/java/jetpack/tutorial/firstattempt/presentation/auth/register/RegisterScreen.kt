package jetpack.tutorial.firstattempt.presentation.auth.register

import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import jetpack.tutorial.firstattempt.R
import jetpack.tutorial.firstattempt.base.customview.textfield.AppEditText
import jetpack.tutorial.firstattempt.base.customview.button.ButtonColor
import jetpack.tutorial.firstattempt.base.customview.button.LargeSolidButton
import jetpack.tutorial.firstattempt.base.customview.textfield.PasswordEditText
import jetpack.tutorial.firstattempt.presentation.auth.AuthHeaderUi
import jetpack.tutorial.firstattempt.presentation.ui.theme.Neutral70

@Composable
fun RegisterScreen(
    onLoginClicked: () -> Unit,
    navigateToMainRoute: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: RegisterViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState(initial = RegisterViewModel.ViewState())
    val effect by viewModel.effect.collectAsState(initial = null)
    val context = LocalContext.current

    LaunchedEffect(effect) {
        when(effect) {
            is RegisterViewModel.ViewEffect.RegisterCompleted -> {
                navigateToMainRoute()
            }
            is RegisterViewModel.ViewEffect.Error -> {
                Toast.makeText(
                    context,
                    (effect as RegisterViewModel.ViewEffect.Error).message,
                    Toast.LENGTH_LONG
                ).show()
            }

            else -> {}
        }
    }

    Column(
        verticalArrangement = Arrangement.SpaceEvenly,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 24.dp)
    ){
        AuthHeaderUi()
        RegisterForm(
            fullName = state.fullName,
            email = state.email,
            password = state.password,
            confirmPassword = state.confirmPassword,
            fullNamePlaceholder = state.fullNamePlaceholder,
            emailPlaceholder = state.emailPlaceholder,
            passwordPlaceholder = state.passwordPlaceholder,
            fullNameError = state.fullNameError,
            emailError = state.emailError,
            passwordError = state.passwordError,
            confirmPasswordError = state.confirmPasswordError,
            showPassword = state.showPassword,
            onLoginClicked = onLoginClicked,
            onRegisterClicked = { viewModel.onEvent(RegisterViewModel.ViewEvent.Submit) },
            onTogglePasswordVisibility = { viewModel.onEvent(RegisterViewModel.ViewEvent.TogglePasswordVisibility) },
            onFullNameChanged = { viewModel.onEvent(RegisterViewModel.ViewEvent.UpdateFullName(it)) },
            onEmailChanged = { viewModel.onEvent(RegisterViewModel.ViewEvent.UpdateEmail(it)) },
            onPasswordChanged = { viewModel.onEvent(RegisterViewModel.ViewEvent.UpdatePassword(it)) },
            onConfirmPasswordChanged = {
                viewModel.onEvent(
                    RegisterViewModel.ViewEvent.UpdateConfirmPassword(
                        it
                    )
                )
            }
        )
    }
}

@Composable
private fun RegisterForm(
    fullName: String,
    email: String,
    password: String,
    confirmPassword: String,
    fullNamePlaceholder: String,
    emailPlaceholder: String,
    passwordPlaceholder: String,
    fullNameError: String?,
    emailError: String?,
    passwordError: String?,
    confirmPasswordError: String?,
    showPassword: Boolean,
    onLoginClicked: () -> Unit,
    onRegisterClicked: () -> Unit,
    onTogglePasswordVisibility: () -> Unit,
    modifier: Modifier = Modifier,
    onFullNameChanged: (String) -> Unit = {},
    onEmailChanged: (String) -> Unit = {},
    onPasswordChanged: (String) -> Unit = {},
    onConfirmPasswordChanged: (String) -> Unit = {},
) {
    Column(
        modifier = modifier
    ) {
        AppEditText(
            label = stringResource(id = R.string.full_name),
            text = fullName,
            error = fullNameError,
            onValueChange = onFullNameChanged,
            placeHolder = fullNamePlaceholder
        )
        AppEditText(
            label = stringResource(id = R.string.email),
            text = email,
            error = emailError,
            onValueChange = onEmailChanged,
            placeHolder = emailPlaceholder,
            modifier = Modifier.padding(top = 16.dp),
        )
        PasswordEditText(
            label = stringResource(id = R.string.password),
            showPassword = showPassword,
            password = password,
            passwordError = passwordError,
            onPasswordChange = onPasswordChanged,
            passwordPlaceholder = passwordPlaceholder,
            onTogglePasswordVisibility = onTogglePasswordVisibility,
            modifier = Modifier.padding(top = 16.dp),
        )
        PasswordEditText(
            label = stringResource(id = R.string.confirm_password),
            showPassword = showPassword,
            password = confirmPassword,
            passwordError = confirmPasswordError,
            onPasswordChange = onConfirmPasswordChanged,
            passwordPlaceholder = passwordPlaceholder,
            onTogglePasswordVisibility = onTogglePasswordVisibility,
            modifier = Modifier.padding(top = 16.dp),
        )
        LargeSolidButton(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 24.dp),
            text = stringResource(id = R.string.register),
            color = ButtonColor.buttonColorBlue(),
            onClick = onRegisterClicked
        )
        Text(
            modifier = modifier
                .clickable(onClick = onLoginClicked)
                .padding(top = 16.dp)
                .align(Alignment.CenterHorizontally),
            text = stringResource(id = R.string.register_navigate_to_login),
            style = TextStyle(
                fontSize = 14.sp
            ),
            color = Neutral70
        )
    }
}