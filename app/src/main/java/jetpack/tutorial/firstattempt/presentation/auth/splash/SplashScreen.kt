package jetpack.tutorial.firstattempt.presentation.auth.splash

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.width
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import jetpack.tutorial.firstattempt.domain.model.auth.AuthStateModel

@Composable
fun SplashScreen(
    onNavigateToMainNav: () -> Unit,
    onNavigateToAuthNav: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: SplashViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState(initial = SplashViewModel.ViewState())
    LaunchedEffect(state) {
        state.authState?.let {
            when(it) {
                AuthStateModel.LOGGED_IN -> { onNavigateToMainNav() }
                AuthStateModel.LOGGED_OUT -> { onNavigateToAuthNav() }
            }
        }
    }
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .fillMaxSize()
    ) {
        CircularProgressIndicator(
            modifier = Modifier.width(64.dp)
        )
    }
}