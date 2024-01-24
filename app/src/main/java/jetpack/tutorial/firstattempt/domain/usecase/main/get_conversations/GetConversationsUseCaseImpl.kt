package jetpack.tutorial.firstattempt.domain.usecase.main.get_conversations

import jetpack.tutorial.firstattempt.core.ResultModel
import jetpack.tutorial.firstattempt.domain.data_source.ConversationDataSource
import jetpack.tutorial.firstattempt.domain.model.main.ConversationModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

class GetConversationsUseCaseImpl @Inject constructor(
    private val dataSource: ConversationDataSource
) : GetConversationsUseCase {

    override fun execute(param: String): Flow<ResultModel<List<ConversationModel>>> {
        return dataSource.getAllConversations(param).map {
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
