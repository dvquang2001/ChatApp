package jetpack.tutorial.firstattempt.domain.usecase.auth.auth_state

import jetpack.tutorial.firstattempt.core.ResultModel
import jetpack.tutorial.firstattempt.domain.data_source.AuthManager
import jetpack.tutorial.firstattempt.domain.model.auth.AuthStateModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class AuthStateUseCaseImpl @Inject constructor(
    private val authManager: AuthManager
) : AuthStateUseCase{

    override fun execute(param: Any): Flow<ResultModel<AuthStateModel>> {
        return authManager.getAuthState().map {
            ResultModel.Success(it)
        }
    }
}