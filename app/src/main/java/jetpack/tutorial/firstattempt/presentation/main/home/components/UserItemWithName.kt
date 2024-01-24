package jetpack.tutorial.firstattempt.presentation.main.home.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import jetpack.tutorial.firstattempt.base.customview.image.AsyncCircleImage
import jetpack.tutorial.firstattempt.domain.model.main.UserModel

@Composable
fun UserItemWithName(
    onItemClicked: (user: UserModel) -> Unit,
    user: UserModel,
    modifier: Modifier = Modifier,
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp),
        modifier = modifier
            .padding(horizontal = 8.dp)
            .width(90.dp)
            .clickable {
                onItemClicked(user)
            }
    ) {
       AsyncCircleImage(
           model = user.avatar,
           userName = user.name,
           showText = true
       )
    }
}