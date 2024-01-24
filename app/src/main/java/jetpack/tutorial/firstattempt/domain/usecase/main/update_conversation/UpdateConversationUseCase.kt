package jetpack.tutorial.firstattempt.domain.usecase.main.update_conversation

import jetpack.tutorial.firstattempt.core.FlowResultUseCase
import jetpack.tutorial.firstattempt.domain.model.main.ConversationModel

interface UpdateConversationUseCase: FlowResultUseCase<ConversationParam, ConversationModel>