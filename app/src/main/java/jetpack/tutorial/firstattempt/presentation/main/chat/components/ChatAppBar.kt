package jetpack.tutorial.firstattempt.presentation.main.chat.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import jetpack.tutorial.firstattempt.R
import jetpack.tutorial.firstattempt.base.customview.image.AsyncCircleImage
import jetpack.tutorial.firstattempt.domain.model.main.UserModel

@Composable
fun ChatAppBar(
    onBackClicked: () -> Unit,
    user: UserModel,
    modifier: Modifier = Modifier,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .fillMaxWidth()
            .padding(
              horizontal = 24.dp
            )
    ) {
        Image(
            painter = painterResource(id = R.drawable.ic_blue_back),
            contentDescription = stringResource(id = R.string.back),
            modifier = modifier
                .clickable {
                    onBackClicked()
                }
        )
        Spacer(modifier = Modifier.width(32.dp))
        AsyncCircleImage(
            model = user.avatar,
            userName = user.name,
            size = 52.dp
        )
        Spacer(modifier = Modifier.width(12.dp))
        Column {
            Text(
                text = user.name,
                style = TextStyle(
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
            )
        }
    }
}