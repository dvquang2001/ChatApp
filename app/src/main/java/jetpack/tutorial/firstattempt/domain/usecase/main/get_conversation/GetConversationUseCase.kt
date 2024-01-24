package jetpack.tutorial.firstattempt.domain.usecase.main.get_conversation

import jetpack.tutorial.firstattempt.core.FlowResultUseCase
import jetpack.tutorial.firstattempt.domain.model.main.ConversationModel
import jetpack.tutorial.firstattempt.domain.usecase.main.update_conversation.ConversationParam

interface GetConversationUseCase: FlowResultUseCase<String, ConversationModel>