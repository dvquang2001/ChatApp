package jetpack.tutorial.firstattempt.presentation.main.chat.components

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import jetpack.tutorial.firstattempt.domain.model.main.MessageModel
import jetpack.tutorial.firstattempt.domain.model.main.UserModel
import kotlinx.coroutines.launch

@Composable
fun ChatContent(
    users: List<UserModel>,
    currentUser: UserModel,
    messages: List<MessageModel>,
    modifier: Modifier = Modifier,
) {
    val lazyListState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()
    if(messages.isNotEmpty()) {
        LaunchedEffect(messages.size) {
            coroutineScope.launch {
                lazyListState.animateScrollToItem(messages.size - 1)
            }
        }
    }
    LazyColumn(
        state = lazyListState,
        modifier = modifier
    ) {
        items(messages) { message ->
            if(users.isNotEmpty()) {
               if(message.senderId == currentUser.id) {
                   MessageSendItem(message = message.content)
               } else {
                   MessageReceiveItem(message = message.content)
               }
            }
        }
    }
}