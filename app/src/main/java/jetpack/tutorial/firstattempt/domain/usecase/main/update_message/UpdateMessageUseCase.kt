package jetpack.tutorial.firstattempt.domain.usecase.main.update_message

import jetpack.tutorial.firstattempt.core.FlowResultUseCase
import jetpack.tutorial.firstattempt.domain.model.main.MessageModel

interface UpdateMessageUseCase: FlowResultUseCase<MessageParam, MessageModel>