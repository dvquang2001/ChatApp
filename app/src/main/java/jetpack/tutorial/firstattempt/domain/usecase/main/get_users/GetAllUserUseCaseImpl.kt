package jetpack.tutorial.firstattempt.domain.usecase.main.get_users

import jetpack.tutorial.firstattempt.core.ResultModel
import jetpack.tutorial.firstattempt.domain.data_source.ConversationDataSource
import jetpack.tutorial.firstattempt.domain.model.main.UserModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GetAllUserUseCaseImpl @Inject constructor(
    private val dataSource: ConversationDataSource
) : GetAllUserUseCase{

    override fun execute(param: GetUsersParam): Flow<ResultModel<List<UserModel>>> {
        return  dataSource.getAllUsers(param).map {
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