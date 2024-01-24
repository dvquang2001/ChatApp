package jetpack.tutorial.firstattempt.domain.usecase.main.get_all_messages

import jetpack.tutorial.firstattempt.core.ResultModel
import jetpack.tutorial.firstattempt.domain.data_source.ConversationDataSource
import jetpack.tutorial.firstattempt.domain.model.main.MessageModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GetAllMessagesUseCaseImpl @Inject constructor(
    private val dataSource: ConversationDataSource
) : GetAllMessagesUseCase{

    override fun execute(param: String): Flow<ResultModel<List<MessageModel>>> {
        return dataSource.getAllMessages(param).map {
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