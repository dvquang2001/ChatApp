package jetpack.tutorial.firstattempt.presentation.main.home.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import jetpack.tutorial.firstattempt.R
import jetpack.tutorial.firstattempt.base.customview.image.ImageCircleShape
import jetpack.tutorial.firstattempt.presentation.ui.theme.Neutral20

@Composable
fun HomeAppBar(
    openDrawerClicked: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        ImageCircleShape(
            imageRes = R.drawable.ic_menu,
            backgroundColor = Neutral20,
            modifier = Modifier
                .clickable {
                    openDrawerClicked()
                }
        )
        Spacer(modifier = Modifier.width(24.dp))
        Text(
            text = stringResource(id = R.string.chats),
            style = TextStyle(
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold
            ),
        )
        Spacer(modifier = Modifier.weight(1f))
        ImageCircleShape(
            imageRes = R.drawable.ic_note,
            backgroundColor = Neutral20
        )
    }
}