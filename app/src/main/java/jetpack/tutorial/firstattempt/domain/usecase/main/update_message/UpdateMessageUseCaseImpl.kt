package jetpack.tutorial.firstattempt.domain.usecase.main.update_message

import jetpack.tutorial.firstattempt.core.ResultModel
import jetpack.tutorial.firstattempt.domain.data_source.ConversationDataSource
import jetpack.tutorial.firstattempt.domain.model.main.MessageModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class UpdateMessageUseCaseImpl @Inject constructor(
    private val dataSource: ConversationDataSource
): UpdateMessageUseCase {

    override fun execute(param: MessageParam): Flow<ResultModel<MessageModel>> {
        return dataSource.updateMessage(param).map {
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