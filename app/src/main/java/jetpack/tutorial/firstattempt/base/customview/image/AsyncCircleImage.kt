package jetpack.tutorial.firstattempt.base.customview.image

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import jetpack.tutorial.firstattempt.R

@Composable
fun AsyncCircleImage(
    model: String,
    userName: String,
    modifier: Modifier = Modifier,
    size: Dp = 64.dp,
    showText: Boolean = false,
) {
    Column {
        AsyncImage(
            model = model,
            contentDescription = "avatar",
            contentScale = ContentScale.Crop,
            error = painterResource(id = R.drawable.ic_launcher),
            modifier = modifier
                .size(size)
                .clip(CircleShape)  // clip to the circle shape
                .border(
                    width = 1.dp,
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            Color.Green,
                            Color.Green,
                        )
                    ),
                    shape = CircleShape
                )
        )
        if(showText) {
            Text(
                text = userName,
                style = TextStyle(
                    fontSize = 12.sp
                ),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}