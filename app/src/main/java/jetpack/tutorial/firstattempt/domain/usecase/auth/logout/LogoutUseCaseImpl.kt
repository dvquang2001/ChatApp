package jetpack.tutorial.firstattempt.domain.usecase.auth.logout

import jetpack.tutorial.firstattempt.core.ResultModel
import jetpack.tutorial.firstattempt.domain.data_source.AuthDataSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class LogoutUseCaseImpl @Inject constructor(
    private val dataSource: AuthDataSource
) : LogoutUseCase{

    override fun execute(param: Any): Flow<ResultModel<Unit>> {
        return dataSource.logout().map {
            when(it) {
                is ResultModel.Success -> {
                    ResultModel.Success(it.result)
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