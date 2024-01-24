package jetpack.tutorial.firstattempt.domain.usecase.main.get_users

import jetpack.tutorial.firstattempt.core.FlowResultUseCase
import jetpack.tutorial.firstattempt.domain.model.main.UserModel

interface GetAllUserUseCase: FlowResultUseCase<GetUsersParam, List<UserModel>>