package jetpack.tutorial.firstattempt.domain.usecase.main.update_conversation

import jetpack.tutorial.firstattempt.core.ResultModel
import jetpack.tutorial.firstattempt.domain.data_source.ConversationDataSource
import jetpack.tutorial.firstattempt.domain.model.main.ConversationModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class UpdateConversationUseCaseImpl @Inject constructor(
    private val dataSource: ConversationDataSource
) : UpdateConversationUseCase{

    override fun execute(param: ConversationParam): Flow<ResultModel<ConversationModel>> {
        return dataSource.updateConversation(param).map {
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