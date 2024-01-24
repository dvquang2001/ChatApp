package jetpack.tutorial.firstattempt.presentation.auth

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import jetpack.tutorial.firstattempt.R
import jetpack.tutorial.firstattempt.presentation.ui.theme.InfoSurface
import jetpack.tutorial.firstattempt.presentation.ui.theme.Neutral70
import jetpack.tutorial.firstattempt.presentation.ui.theme.Neutral90


@Composable
fun AuthHeaderUi(
    modifier: Modifier = Modifier,
    title: String = stringResource(id = R.string.app_name),
    subtitle: String = stringResource(id = R.string.description_login),
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .clip(shape = CircleShape)
                .size(40.dp)
                .background(InfoSurface)
        ) {
            Image(
                painter = painterResource(id = R.drawable.ic_launcher),
                contentDescription = "Logo",
                modifier = Modifier
                    .size(24.dp)
                    .align(Alignment.Center),
            )
        }
        Text(
            modifier = Modifier.padding(top = 16.dp),
            text = title,
            style = TextStyle(
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold
            ),
            fontSize = 24.sp,
            color = Neutral90,
        )
        Text(
            modifier = Modifier.padding(top = 24.dp),
            text = subtitle,
            style = TextStyle(
                fontSize = 12.sp
            ),
            color = Neutral70
        )
    }
}