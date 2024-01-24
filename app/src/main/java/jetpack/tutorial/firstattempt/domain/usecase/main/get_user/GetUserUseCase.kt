package jetpack.tutorial.firstattempt.domain.usecase.main.get_user

import jetpack.tutorial.firstattempt.core.FlowResultUseCase
import jetpack.tutorial.firstattempt.domain.model.main.UserModel

interface GetUserUseCase: FlowResultUseCase<String, UserModel>