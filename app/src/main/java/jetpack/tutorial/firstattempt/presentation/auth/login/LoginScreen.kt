package jetpack.tutorial.firstattempt.presentation.auth.login

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
import androidx.compose.ui.text.font.FontWeight
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
import jetpack.tutorial.firstattempt.presentation.ui.theme.Neutral90

@Composable
fun LoginScreen(
    onRegisterClicked: () -> Unit,
    onForgotPasswordClicked: () -> Unit,
    navigateToMainRoute: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: LoginViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState(initial = LoginViewModel.ViewState())
    val effect by viewModel.effect.collectAsState(initial = null)
    val context = LocalContext.current

    LaunchedEffect(effect) {
        when(effect) {
            is LoginViewModel.ViewEffect.LoginSuccess -> {
                navigateToMainRoute()
            }
            is LoginViewModel.ViewEffect.Error -> {
                Toast.makeText(
                    context,
                    (effect as LoginViewModel.ViewEffect.Error).message,
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
    ) {
        AuthHeaderUi()
        LoginForm(
            email = state.email,
            password = state.password,
            emailError = state.emailError,
            passwordError = state.passwordError,
            showPassword = state.showPassword,
            emailPlaceholder = state.emailPlaceholder,
            passwordPlaceholder = state.passwordPlaceholder,
            onEmailChange = { viewModel.onEvent(LoginViewModel.ViewEvent.UpdateEmail(it)) },
            onPasswordChange = { viewModel.onEvent(LoginViewModel.ViewEvent.UpdatePassword(it)) },
            onLoginClicked = { viewModel.onEvent(LoginViewModel.ViewEvent.Submit) },
            onRegisterClicked = onRegisterClicked,
            onForgotPasswordClicked = onForgotPasswordClicked,
            onTogglePasswordVisibility = { viewModel.onEvent(LoginViewModel.ViewEvent.TogglePasswordVisibility) }
        )
    }
}


@Composable
private fun LoginForm(
    email: String,
    password: String,
    emailError: String?,
    passwordError: String?,
    emailPlaceholder: String,
    passwordPlaceholder: String,
    onEmailChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onLoginClicked: () -> Unit,
    onRegisterClicked: () -> Unit,
    onForgotPasswordClicked: () -> Unit,
    onTogglePasswordVisibility: () -> Unit,
    modifier: Modifier = Modifier,
    showPassword: Boolean = false,
) {
    Column(modifier) {
        AppEditText(
            label = stringResource(id = R.string.email),
            text = email,
            error = emailError,
            onValueChange = onEmailChange,
            placeHolder = emailPlaceholder
        )
        PasswordEditText(
            label = stringResource(id = R.string.password),
            showPassword = showPassword,
            password = password,
            passwordError = passwordError,
            onPasswordChange = onPasswordChange,
            passwordPlaceholder = passwordPlaceholder,
            onTogglePasswordVisibility = onTogglePasswordVisibility,
            modifier = Modifier.padding(top = 24.dp),
        )
        Text(
            modifier = Modifier
                .padding(top = 8.dp)
                .clickable(onClick = onForgotPasswordClicked)
                .align(Alignment.End),
            text = stringResource(id = R.string.forgot_password),
            style = TextStyle(
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold
            ),
            color = Neutral90,
        )
        LargeSolidButton(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 32.dp),
            text = stringResource(id = R.string.login),
            color = ButtonColor.buttonColorBlue(),
            onClick = onLoginClicked
        )
        Text(
            modifier = modifier
                .clickable(onClick = onRegisterClicked)
                .padding(top = 24.dp)
                .align(Alignment.CenterHorizontally),
            text = stringResource(id = R.string.navigate_to_register),
            style = TextStyle(
                fontSize = 14.sp
            ),
            color = Neutral70
        )
    }
}