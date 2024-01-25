package jetpack.tutorial.firstattempt.presentation.main.chat

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import jetpack.tutorial.firstattempt.presentation.main.chat.components.ChatAppBar
import jetpack.tutorial.firstattempt.presentation.main.chat.components.ChatContent
import jetpack.tutorial.firstattempt.presentation.main.chat.components.ChatToolBar

@Composable
fun ChatScreen(
    userId: String?,
    conversationId: String?,
    onBackClicked: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: ChatViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsState(initial = ChatViewModel.ViewState())

    if (state.error != null) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = modifier
                .fillMaxSize()
        ) {
            Text(text = state.error!!)
        }
    } else {
        Column(
            verticalArrangement = Arrangement.spacedBy(24.dp),
            modifier = modifier
                .fillMaxSize()
                .padding(vertical = 16.dp)
        ) {
            state.currentUser?.let { user ->
                if (state.users.isNotEmpty()) {
                    ChatAppBar(
                        user = state.users[0],
                        onBackClicked = onBackClicked
                    )
                }
                ChatContent(
                    users = state.users,
                    currentUser = user,
                    messages = state.messages,
                    onLoadMoreMessages = {
                        viewModel.onEvent(ChatViewModel.ViewEvent.LoadMoreMessages)
                    },
                    modifier = Modifier
                        .weight(1f)
                )
                ChatToolBar(
                    onTextChange = {
                        viewModel.onEvent(ChatViewModel.ViewEvent.OnTextChanged(it))
                    },
                    messageText = state.textMessage,
                    onSendClicked = {
                        viewModel.onEvent(ChatViewModel.ViewEvent.Send(it))
                    }
                )
            }
        }
    }
}