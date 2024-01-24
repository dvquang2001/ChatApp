package jetpack.tutorial.firstattempt.domain.usecase.main.get_current_user

import jetpack.tutorial.firstattempt.core.FlowResultUseCase
import jetpack.tutorial.firstattempt.domain.model.main.UserModel

interface GetCurrentUserUseCase: FlowResultUseCase<Any, UserModel>