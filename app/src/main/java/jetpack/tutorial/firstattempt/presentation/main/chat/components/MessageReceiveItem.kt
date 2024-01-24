package jetpack.tutorial.firstattempt.presentation.main.chat.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import jetpack.tutorial.firstattempt.presentation.ui.theme.AbsBlack

@Composable
fun MessageReceiveItem(
    message: String,
    modifier: Modifier = Modifier,
    textColor: Color = AbsBlack,
    bgColor: Color = Color.LightGray
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp, horizontal = 16.dp)
    ) {
        Row(
            horizontalArrangement = Arrangement.Start,
            modifier = Modifier
                .weight(3f)
        ) {
            Column(
                modifier = modifier
                    .background(
                        color = bgColor,
                        shape = RoundedCornerShape(size = 24.dp)
                    )
            ) {
                Text(
                    text = message,
                    color = textColor,
                    modifier = Modifier
                        .padding(
                            horizontal = 16.dp,
                            vertical = 8.dp
                        )
                )
            }
        }
        Spacer(modifier = Modifier.weight(1f))
    }
}