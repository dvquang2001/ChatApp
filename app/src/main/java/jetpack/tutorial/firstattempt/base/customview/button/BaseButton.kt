package jetpack.tutorial.firstattempt.base.customview.button

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import jetpack.tutorial.firstattempt.presentation.ui.theme.InfoMain

@Composable
fun LargeSolidButton(
    modifier: Modifier = Modifier,
    text: String,
    color: ButtonColors,
    enabled: Boolean = true,
    onClick: () -> Unit,
) {
    OutlinedButton(
        modifier = modifier.defaultMinSize(minWidth = 1.dp, minHeight = 1.dp),
        enabled = enabled,
        onClick = onClick,
        colors = color,
        border = null,
        shape = RoundedCornerShape(size = 41.dp),
        contentPadding = PaddingValues(vertical = 16.dp, horizontal = 16.dp)
    ) {
        Text(
            text = text,
            style = TextStyle(
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold,
                ),
            textAlign = TextAlign.Center
        )
    }
}


@Composable
fun LargeOutlinedButton(
    modifier: Modifier = Modifier,
    text: String,
    outlinedColor: ButtonColors,
    enabled: Boolean = true,
    borderColor: Color = InfoMain,
    onClick: () -> Unit,
) {
    OutlinedButton(
        modifier = modifier.defaultMinSize(minWidth = 1.dp, minHeight = 1.dp),
        enabled = enabled,
        onClick = onClick,
        colors = outlinedColor,
        border = BorderStroke(width = 1.dp, color = borderColor),
        shape = RoundedCornerShape(size = 41.dp),
        contentPadding = PaddingValues(vertical = 16.dp, horizontal = 16.dp)
    ) {
        Text(
            text = text,
            style = TextStyle(
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold,
            ),
            textAlign = TextAlign.Center
        )
    }
}
