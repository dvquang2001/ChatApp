package jetpack.tutorial.firstattempt.domain.usecase.main.get_current_user

import jetpack.tutorial.firstattempt.core.ResultModel
import jetpack.tutorial.firstattempt.domain.data_source.ConversationDataSource
import jetpack.tutorial.firstattempt.domain.model.main.UserModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GetCurrentUserUseCaseImpl @Inject constructor(
    private val dataSource: ConversationDataSource
) : GetCurrentUserUseCase{

    override fun execute(param: Any): Flow<ResultModel<UserModel>> {
        return dataSource.getCurrentUser().map {
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