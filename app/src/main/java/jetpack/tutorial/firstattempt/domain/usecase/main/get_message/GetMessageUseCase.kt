package jetpack.tutorial.firstattempt.domain.usecase.main.get_message

import jetpack.tutorial.firstattempt.core.FlowResultUseCase
import jetpack.tutorial.firstattempt.domain.model.main.MessageModel
import jetpack.tutorial.firstattempt.domain.usecase.main.update_message.MessageParam

interface GetMessageUseCase: FlowResultUseCase<MessageParam, MessageModel>