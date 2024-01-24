package jetpack.tutorial.firstattempt.presentation.main.chat.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import jetpack.tutorial.firstattempt.R
import jetpack.tutorial.firstattempt.base.customview.textfield.AppEditText
import jetpack.tutorial.firstattempt.presentation.ui.theme.FirstAttemptTheme

@Composable
fun ChatToolBar(
    onTextChange: (value: String) -> Unit,
    onSendClicked: (text: String) -> Unit,
    messageText: String,
    modifier: Modifier = Modifier,
) {
    val focusRequester = remember {
        FocusRequester()
    }
    val focusManager = LocalFocusManager.current

    Row(
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .fillMaxWidth()
            .padding(
                horizontal = 16.dp,
                vertical = 8.dp
            )
    ) {
        AppEditText(
            label = null,
            text = messageText,
            error = null,
            onValueChange = {
                onTextChange(it)
            },
            modifier = Modifier
                .weight(1f)
                .focusRequester(focusRequester)
        )
        Image(
            painter = painterResource(id = R.drawable.ic_blue_send),
            contentDescription = stringResource(
                id = R.string.send
            ),
            modifier = Modifier
                .clickable {
                    focusManager.clearFocus()
                    onSendClicked(messageText)
                }
        )
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewChatToolBar() {
    FirstAttemptTheme {
        ChatToolBar(
            onTextChange = {},
            messageText = "",
            onSendClicked = {}
          )
    }
}