package jetpack.tutorial.firstattempt.domain.usecase.main.get_conversations

import jetpack.tutorial.firstattempt.core.FlowResultUseCase
import jetpack.tutorial.firstattempt.domain.model.main.ConversationModel

interface GetConversationsUseCase: FlowResultUseCase<String, List<ConversationModel>>
