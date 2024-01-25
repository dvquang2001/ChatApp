package jetpack.tutorial.firstattempt.presentation.main.chat.components

import android.util.Log
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import jetpack.tutorial.firstattempt.domain.model.main.MessageModel
import jetpack.tutorial.firstattempt.domain.model.main.UserModel

@Composable
fun ChatContent(
    onLoadMoreMessages: () -> Unit,
    users: List<UserModel>,
    currentUser: UserModel,
    messages: List<MessageModel>,
    modifier: Modifier = Modifier,
) {
    val lazyListState = rememberLazyListState()
    val isAtBottom = lazyListState.canScrollForward
    if(messages.isNotEmpty()) {
        LaunchedEffect(isAtBottom) {
            if(isAtBottom) {
                Log.d("TAG-BOOL", "ChatContent: loadmore")
                onLoadMoreMessages()
            }
        }
    }
    LazyColumn(
        reverseLayout = true,
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