package jetpack.tutorial.firstattempt.presentation.main.home.components

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import jetpack.tutorial.firstattempt.domain.model.main.ConversationModel

@Composable
fun ListConversation(
    onItemConversationClicked: (id: String) -> Unit,
    conversations: List<ConversationModel>,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier
    ) {
        items(conversations) { conversation ->
            ConversationItem(
                conversation = conversation,
                onItemClicked = onItemConversationClicked
            )
        }
    }
}