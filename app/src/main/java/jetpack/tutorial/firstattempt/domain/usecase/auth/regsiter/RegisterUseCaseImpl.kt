package jetpack.tutorial.firstattempt.domain.usecase.auth.regsiter

import jetpack.tutorial.firstattempt.core.ResultModel
import jetpack.tutorial.firstattempt.domain.data_source.AuthDataSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class RegisterUseCaseImpl @Inject constructor(
    private val authDataSource: AuthDataSource
) : RegisterUseCase{

    override fun execute(param: RegisterParam): Flow<ResultModel<Unit>> {
        return authDataSource.registerWithEmailPassword(param).map {
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