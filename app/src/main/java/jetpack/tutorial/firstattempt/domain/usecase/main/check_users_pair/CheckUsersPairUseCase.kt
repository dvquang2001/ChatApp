package jetpack.tutorial.firstattempt.domain.usecase.main.check_users_pair

import jetpack.tutorial.firstattempt.core.FlowResultUseCase
import jetpack.tutorial.firstattempt.domain.model.main.ConversationModel

interface CheckUsersPairUseCase: FlowResultUseCase<CheckUserPairParam, ConversationModel>