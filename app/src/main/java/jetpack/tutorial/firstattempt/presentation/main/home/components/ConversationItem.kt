package jetpack.tutorial.firstattempt.presentation.main.home.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import jetpack.tutorial.firstattempt.base.customview.image.AsyncCircleImage
import jetpack.tutorial.firstattempt.domain.model.main.ConversationModel
import jetpack.tutorial.firstattempt.presentation.util.getShortDate

@Composable
fun ConversationItem(
    onItemClicked: (id: String) -> Unit,
    conversation: ConversationModel,
    modifier: Modifier = Modifier,
) {

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(
                horizontal = 24.dp,
                vertical = 8.dp
            )
            .clickable {
                onItemClicked(conversation.id)
            }
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            AsyncCircleImage(
                model = conversation.users[0],
                userName = conversation.title,
                showText = false
            )
            Spacer(modifier = Modifier.width(12.dp))
            Column(
                verticalArrangement = Arrangement.SpaceEvenly,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(64.dp)
            ) {
                Text(
                    text = conversation.title,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp
                )
                Row {
                    Text(text = conversation.lastMessage)
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(text = conversation.lastMessageTime.getShortDate())
                }
            }
        }
        Spacer(modifier = Modifier.height(8.dp))
        Divider(
            modifier = Modifier
                .fillMaxWidth()
        )
    }
}