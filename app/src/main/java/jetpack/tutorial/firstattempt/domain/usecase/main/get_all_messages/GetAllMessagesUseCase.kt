package jetpack.tutorial.firstattempt.domain.usecase.main.get_all_messages

import jetpack.tutorial.firstattempt.core.FlowResultUseCase
import jetpack.tutorial.firstattempt.domain.model.main.MessageModel

interface GetAllMessagesUseCase: FlowResultUseCase<String, List<MessageModel>>