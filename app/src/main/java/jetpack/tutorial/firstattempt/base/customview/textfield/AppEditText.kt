package jetpack.tutorial.firstattempt.base.customview.textfield

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.border
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import jetpack.tutorial.firstattempt.presentation.ui.theme.DangerMain
import jetpack.tutorial.firstattempt.presentation.ui.theme.InfoMain
import jetpack.tutorial.firstattempt.presentation.ui.theme.Neutral70
import jetpack.tutorial.firstattempt.presentation.ui.theme.Neutral80

@Composable
fun AppEditText(
    modifier: Modifier = Modifier,
    label: String?,
    error: String?,
    placeHolder: String = "",
    text: String = "",
    borderColor: Color = if (error != null) {
        DangerMain
    } else {
        InfoMain
    },
    visualTransformation: VisualTransformation = VisualTransformation.None,
    rightImage: @Composable (ColumnScope.() -> Unit)? = null,
    onValueChange: (String) -> Unit,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    cursorBrush: Brush = SolidColor(Color.Black),
    decorationBox: @Composable (innerTextField: @Composable () -> Unit) -> Unit =
        @Composable { innerTextField -> innerTextField() }
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(10.dp),
        horizontalAlignment = Alignment.Start
    ) {
        label?.let {
            Text(text = it, color = Neutral80)
        }
        Row(
            modifier = Modifier
                .border(
                    width = 1.dp,
                    color = borderColor,
                    shape = RoundedCornerShape(8.dp)
                )
                .padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(modifier = Modifier.weight(1f)) {
                val showPlaceHolder = text.isEmpty()
                if (showPlaceHolder) {
                    Text(
                        text = placeHolder,
                        fontWeight = FontWeight.Medium,
                        fontSize = 14.sp,
                        color = Neutral70,
                    )
                }
                BasicTextField(
                    modifier = Modifier.fillMaxWidth(),
                    value = text,
                    onValueChange = onValueChange,
                    textStyle = TextStyle(
                        color = Neutral70,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium,
                    ),
                    visualTransformation = visualTransformation,
                    singleLine = true,
                    interactionSource = interactionSource,
                    cursorBrush = cursorBrush,
                    decorationBox = decorationBox
                )
            }
            rightImage?.invoke(this@Column)
        }
        AnimatedVisibility(visible = error != null) {
            error?.let {
                Text(
                    text = it,
                    style = TextStyle(
                        fontSize = 12.sp
                    ),
                    color = DangerMain
                )
            }
        }
    }
}