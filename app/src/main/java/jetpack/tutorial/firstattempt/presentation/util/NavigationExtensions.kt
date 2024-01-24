package jetpack.tutorial.firstattempt.presentation.util

import androidx.navigation.NavHostController

fun NavHostController.navigateWithPopUp(
    toRoute: String,  // route name where you want to navigate
    fromRoute: String // route you want from popUpTo.
) {
    this.navigate(toRoute) {
        popUpTo(fromRoute) {
            inclusive = true // It can be changed to false if you
            // want to keep your fromRoute exclusive
        }
    }
}