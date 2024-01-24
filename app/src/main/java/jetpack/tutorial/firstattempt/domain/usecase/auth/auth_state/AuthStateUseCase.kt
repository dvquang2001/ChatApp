package jetpack.tutorial.firstattempt.domain.usecase.auth.auth_state

import jetpack.tutorial.firstattempt.core.FlowResultUseCase
import jetpack.tutorial.firstattempt.domain.model.auth.AuthStateModel

interface AuthStateUseCase: FlowResultUseCase<Any, AuthStateModel>