package jetpack.tutorial.firstattempt.base.customview.image

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import jetpack.tutorial.firstattempt.presentation.ui.theme.InfoSurface

@Composable
fun ImageCircleShape(
    @DrawableRes imageRes: Int,
    backgroundColor: Color,
    modifier: Modifier = Modifier,
    boxSize: Dp = 40.dp,
    imageSize: Dp = 24.dp,
) {
    Box(
        modifier = modifier
            .clip(shape = CircleShape)
            .size(boxSize)
            .background(backgroundColor)
    ) {
        Image(
            painter = painterResource(imageRes),
            contentDescription = "Logo",
            modifier = Modifier
                .size(imageSize)
                .align(Alignment.Center)
        )
    }
}