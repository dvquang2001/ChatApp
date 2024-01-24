package jetpack.tutorial.firstattempt.domain.usecase.main.get_conversation

import jetpack.tutorial.firstattempt.core.ResultModel
import jetpack.tutorial.firstattempt.domain.data_source.ConversationDataSource
import jetpack.tutorial.firstattempt.domain.model.main.ConversationModel
import jetpack.tutorial.firstattempt.domain.usecase.main.update_conversation.ConversationParam
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GetConversationUseCaseImpl @Inject constructor(
    private val dataSource: ConversationDataSource
): GetConversationUseCase{

    override fun execute(param: String): Flow<ResultModel<ConversationModel>> {
        return dataSource.getConversation(param).map {
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