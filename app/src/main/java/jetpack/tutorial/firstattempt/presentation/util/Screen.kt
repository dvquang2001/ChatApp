package jetpack.tutorial.firstattempt.presentation.util

sealed class Screen(val route: String) {

    // auth routes
    data object Auth: Screen("auth")
    data object Splash: Screen("splash")
    data object Login: Screen("login")
    data object Register: Screen("register")

    // main routes
    data object MainRoute: Screen("main_route")
    data object Home: Screen("home")
    data object Chat: Screen("chat")
}