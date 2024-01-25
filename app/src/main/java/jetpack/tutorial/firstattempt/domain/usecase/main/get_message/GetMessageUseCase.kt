package jetpack.tutorial.firstattempt.domain.usecase.main.get_message

import jetpack.tutorial.firstattempt.core.FlowResultUseCase
import jetpack.tutorial.firstattempt.domain.model.main.MessageModel

interface GetMessageUseCase: FlowResultUseCase<GetMessageParam, MessageModel>