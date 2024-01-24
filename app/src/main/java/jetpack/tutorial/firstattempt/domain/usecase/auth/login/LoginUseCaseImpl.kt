package jetpack.tutorial.firstattempt.domain.usecase.auth.login

import jetpack.tutorial.firstattempt.core.ResultModel
import jetpack.tutorial.firstattempt.domain.data_source.AuthDataSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class LoginUseCaseImpl @Inject constructor(
    private val authDataSource: AuthDataSource
) : LoginUseCase{

    override fun execute(param: LoginParam): Flow<ResultModel<Unit>> {
        return authDataSource.loginWithEmailPassword(param).map {
            when(it) {
                is ResultModel.Success -> {
                    ResultModel.Success(Unit)
                }
                is ResultModel.Error -> {
                    ResultModel.Error(it.t)
                }
                else -> {
                    ResultModel.Error(t = UnknownError())
                }
            }
        }
    }
}