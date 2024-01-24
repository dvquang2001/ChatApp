package jetpack.tutorial.firstattempt

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.compose.rememberNavController
import dagger.hilt.android.AndroidEntryPoint
import jetpack.tutorial.firstattempt.presentation.auth.login.LoginScreen
import jetpack.tutorial.firstattempt.presentation.auth.register.RegisterScreen
import jetpack.tutorial.firstattempt.presentation.auth.splash.SplashScreen
import jetpack.tutorial.firstattempt.presentation.main.chat.ChatScreen
import jetpack.tutorial.firstattempt.presentation.main.home.HomeScreen
import jetpack.tutorial.firstattempt.presentation.ui.theme.FirstAttemptTheme
import jetpack.tutorial.firstattempt.presentation.util.Screen
import jetpack.tutorial.firstattempt.presentation.util.navigateWithPopUp

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            FirstAttemptTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    AppContent()
                }
            }
        }
    }
}

@Composable
fun AppContent() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Screen.Splash.route
    ) {
        composable(Screen.Splash.route) {
            SplashScreen(
                onNavigateToMainNav = {
                    navController.navigateWithPopUp(
                        Screen.MainRoute.route,
                        Screen.Splash.route
                    )
                },
                onNavigateToAuthNav = {
                    navController.navigateWithPopUp(
                        Screen.Auth.route,
                        Screen.Splash.route
                    )
                })
        }
        navigation(startDestination = Screen.Login.route, route = Screen.Auth.route) {
            composable(Screen.Login.route) {
                LoginScreen(
                    onRegisterClicked = {
                        navController.navigateWithPopUp(
                            Screen.Register.route,
                            Screen.Login.route
                        )
                    },
                    onForgotPasswordClicked = {
                        //todo:
                    },
                    navigateToMainRoute = {
                        navController.navigateWithPopUp(
                            Screen.MainRoute.route,
                            Screen.Login.route
                        )
                    }
                )
            }
            composable(Screen.Register.route) {
                RegisterScreen(
                    onLoginClicked = {
                        navController.navigateWithPopUp(
                            Screen.Login.route,
                            Screen.Register.route
                        )
                    },
                    navigateToMainRoute = {
                        navController.navigateWithPopUp(
                            Screen.MainRoute.route,
                            Screen.Register.route
                        )
                    }
                )
            }
        }
        navigation(startDestination = Screen.Home.route, route = Screen.MainRoute.route) {
            composable(Screen.Home.route) {
                HomeScreen(
                    navigateToAuthScreen = {
                        navController.navigateWithPopUp(
                            Screen.Auth.route,
                            Screen.MainRoute.route
                        )
                    },
                    onItemUserOnlineClicked = { userId ->
                        navController.navigate("${Screen.Chat.route}/$userId/${null}")
                    },
                    onItemConversationClicked = { conversationId ->
                        navController.navigate("${Screen.Chat.route}/${null}/$conversationId")
                    }
                )
            }
            composable("${Screen.Chat.route}/{userId}/{conversationId}") { backStackEntry ->
                ChatScreen(
                    userId = backStackEntry.arguments?.getString("userId"),
                    conversationId = backStackEntry.arguments?.getString("conversationId"),
                    onBackClicked = {
                        navController.navigateUp()
                    }
                )
            }
        }
    }
}