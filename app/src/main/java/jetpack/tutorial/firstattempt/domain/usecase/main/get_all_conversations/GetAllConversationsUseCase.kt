package jetpack.tutorial.firstattempt.domain.usecase.main.get_all_conversations

import jetpack.tutorial.firstattempt.core.FlowResultUseCase
import jetpack.tutorial.firstattempt.domain.model.main.ConversationModel

interface GetAllConversationsUseCase: FlowResultUseCase<String, List<ConversationModel>>