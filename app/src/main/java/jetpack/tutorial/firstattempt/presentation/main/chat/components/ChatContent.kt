package jetpack.tutorial.firstattempt.presentation.main.chat.components

import android.util.Log
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import jetpack.tutorial.firstattempt.domain.model.main.MessageModel
import jetpack.tutorial.firstattempt.domain.model.main.UserModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlin.math.log

@Composable
fun ChatContent(
    onLoadMoreMessages: (loadCount: Int) -> Unit,
    users: List<UserModel>,
    currentUser: UserModel,
    messages: List<MessageModel>,
    modifier: Modifier = Modifier,
) {
    val lazyListState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()
    var count by remember {
        mutableIntStateOf(0)
    }
    if(messages.isNotEmpty()) {
        LaunchedEffect(messages.size) {
            if(count == 0) {
                coroutineScope.launch {
                    lazyListState.animateScrollToItem(messages.size - 1)
                }
            }
        }
        LaunchedEffect(lazyListState) {
            snapshotFlow { lazyListState.firstVisibleItemIndex }
                .collectLatest { index ->
                    if(index == 0) {
                        if(count > 0) {
                            onLoadMoreMessages(count)
                        }
                        count++
                    }
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