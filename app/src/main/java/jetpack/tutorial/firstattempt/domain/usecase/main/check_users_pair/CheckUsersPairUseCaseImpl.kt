package jetpack.tutorial.firstattempt.domain.usecase.main.check_users_pair

import jetpack.tutorial.firstattempt.core.ResultModel
import jetpack.tutorial.firstattempt.domain.data_source.ConversationDataSource
import jetpack.tutorial.firstattempt.domain.model.main.ConversationModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class CheckUsersPairUseCaseImpl @Inject constructor(
    private val dataSource: ConversationDataSource
) : CheckUsersPairUseCase {
    override fun execute(param: CheckUserPairParam): Flow<ResultModel<ConversationModel>> {
        return dataSource.checkTwoUserPair(
            userParam1 = param.userParam1,
            userParam2 = param.userParam2
        ).map {
            when (it) {
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

