package jetpack.tutorial.firstattempt.presentation.main.home.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import jetpack.tutorial.firstattempt.domain.model.main.UserModel

@Composable
fun ListUserOnline(
    onItemClicked: (user: UserModel) -> Unit,
    users: List<UserModel>,
    modifier: Modifier = Modifier,
) {
   LazyRow(
       modifier = modifier
           .fillMaxWidth()
   ) {
       items(users) { user ->
           UserItemWithName(
               user = user,
               onItemClicked = onItemClicked
           )
       }
   }
}